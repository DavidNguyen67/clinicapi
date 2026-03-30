# Kafka Clinic — Lộ trình học Kafka qua hệ thống phòng khám

> Stack: **TypeScript + KafkaJS + Next.js**  
> Mục tiêu: Học Kafka từ cơ bản đến nâng cao qua 4 case thực tế trong clinic

---

## Tổng quan lộ trình

| Tuần | Case                 | Concept chính                        |
| ---- | -------------------- | ------------------------------------ |
| 1    | Appointment Events   | Producer, Consumer, Topic, Partition |
| 2    | Notification Fan-out | Consumer Groups                      |
| 3    | Audit Log            | Log Retention, Replay, Offset        |
| 4    | Queue số thứ tự      | Partition Key, Ordering Guarantee    |

---

## Setup môi trường

```bash
# Chạy Kafka local bằng Docker
docker-compose up -d
```

```yaml
# docker-compose.yml
version: "3"
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
```

```bash
# Cài KafkaJS
npm install kafkajs
```

```typescript
// lib/kafka.ts — Kafka client dùng chung
import { Kafka } from "kafkajs";

export const kafka = new Kafka({
  clientId: "clinic-app",
  brokers: ["localhost:9092"],
});
```

---

## Case 1 — Appointment Events

> **Concept:** Producer, Consumer cơ bản, Topic, Partition

### Bài toán

Khi bệnh nhân đặt lịch hẹn, hệ thống cần xử lý nhiều tác vụ sau đó:
gửi email xác nhận, tạo reminder, cập nhật lịch bác sĩ.

### Thiết kế Topic

```
Topic: clinic.appointments
Partitions: 3

Events:
  appointment.created   → gửi email confirm + tạo reminder
  appointment.updated   → notify bác sĩ + bệnh nhân
  appointment.cancelled → hoàn tiền, giải phóng slot
  appointment.completed → tạo invoice, yêu cầu feedback
```

### Code

```typescript
// types/appointment.ts
export type AppointmentEvent = {
  eventType: "created" | "updated" | "cancelled" | "completed";
  appointmentId: string;
  patientId: string;
  doctorId: string;
  scheduledAt: string;
  payload?: Record<string, unknown>;
};
```

```typescript
// producers/appointment.producer.ts
import { kafka } from "../lib/kafka";
import { AppointmentEvent } from "../types/appointment";

const producer = kafka.producer();

export async function publishAppointmentEvent(event: AppointmentEvent) {
  await producer.connect();

  await producer.send({
    topic: "clinic.appointments",
    messages: [
      {
        key: event.appointmentId,
        value: JSON.stringify(event),
        headers: {
          eventType: event.eventType,
          timestamp: new Date().toISOString(),
        },
      },
    ],
  });

  await producer.disconnect();
}
```

```typescript
// consumers/appointment.consumer.ts
import { kafka } from "../lib/kafka";

const consumer = kafka.consumer({ groupId: "appointment-handler" });

async function run() {
  await consumer.connect();
  await consumer.subscribe({
    topic: "clinic.appointments",
    fromBeginning: false,
  });

  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      const event = JSON.parse(message.value!.toString());

      console.log(`[Partition ${partition}] Received:`, event.eventType);

      switch (event.eventType) {
        case "created":
          await sendConfirmationEmail(event);
          await scheduleReminder(event);
          break;
        case "cancelled":
          await processRefund(event);
          break;
        case "completed":
          await createInvoice(event);
          break;
      }
    },
  });
}

run();
```

### Chạy thử

```bash
# Terminal 1 — chạy consumer
npx ts-node consumers/appointment.consumer.ts

# Terminal 2 — publish event
npx ts-node -e "
import { publishAppointmentEvent } from './producers/appointment.producer';
publishAppointmentEvent({
  eventType: 'created',
  appointmentId: 'appt-001',
  patientId: 'patient-123',
  doctorId: 'doctor-456',
  scheduledAt: '2026-04-01T09:00:00Z',
});
"
```

### Bạn học được gì

- Cách tạo Producer và gửi message
- Cách tạo Consumer và đọc message
- Topic là gì, Partition là gì
- Message key dùng để làm gì

---

## Case 2 — Notification Fan-out

> **Concept:** Consumer Groups — nhiều service nhận cùng 1 event, xử lý độc lập

### Bài toán

Khi `appointment.created` xảy ra, cần notify qua **4 kênh độc lập**:
Email, SMS, Push Notification, In-App. Nếu SMS bị lỗi, Email không bị ảnh hưởng.

### Thiết kế

```
Topic: clinic.notifications

1 event → 4 consumer groups độc lập:
  group: email-service       → gửi email
  group: sms-service         → gửi SMS
  group: push-service        → push notification mobile
  group: in-app-service      → hiển thị trong app
```

### Code

```typescript
// types/notification.ts
export type NotificationEvent = {
  notificationId: string;
  patientId: string;
  channel: "all";
  title: string;
  message: string;
  metadata?: Record<string, unknown>;
};
```

```typescript
// producers/notification.producer.ts
import { kafka } from "../lib/kafka";
import { NotificationEvent } from "../types/notification";

const producer = kafka.producer();

export async function publishNotification(event: NotificationEvent) {
  await producer.connect();
  await producer.send({
    topic: "clinic.notifications",
    messages: [{ key: event.patientId, value: JSON.stringify(event) }],
  });
  await producer.disconnect();
}
```

```typescript
// consumers/email.consumer.ts — mỗi service là 1 consumer group riêng
import { kafka } from "../lib/kafka";

const consumer = kafka.consumer({ groupId: "email-service" }); // group riêng

async function run() {
  await consumer.connect();
  await consumer.subscribe({ topic: "clinic.notifications" });

  await consumer.run({
    eachMessage: async ({ message }) => {
      const event = JSON.parse(message.value!.toString());
      console.log("[Email] Sending email to patient:", event.patientId);
      // await sendEmail(event);
    },
  });
}

run();
```

```typescript
// consumers/sms.consumer.ts — group khác, nhận đủ message, fail riêng lẻ
import { kafka } from "../lib/kafka";

const consumer = kafka.consumer({ groupId: "sms-service" }); // group khác!

async function run() {
  await consumer.connect();
  await consumer.subscribe({ topic: "clinic.notifications" });

  await consumer.run({
    eachMessage: async ({ message }) => {
      const event = JSON.parse(message.value!.toString());
      console.log("[SMS] Sending SMS to patient:", event.patientId);
      // await sendSMS(event);
    },
  });
}

run();
```

### Chạy thử

```bash
# Chạy 4 consumer song song
npx ts-node consumers/email.consumer.ts &
npx ts-node consumers/sms.consumer.ts &
npx ts-node consumers/push.consumer.ts &
npx ts-node consumers/in-app.consumer.ts &

# Publish 1 notification — cả 4 consumer đều nhận
npx ts-node -e "
import { publishNotification } from './producers/notification.producer';
publishNotification({
  notificationId: 'notif-001',
  patientId: 'patient-123',
  channel: 'all',
  title: 'Lịch hẹn đã xác nhận',
  message: 'Bạn có lịch hẹn vào 9:00 AM ngày 01/04/2026',
});
"
```

### Bạn học được gì

- Consumer Group là gì và tại sao cần
- Cùng topic, khác group → mỗi group nhận **đầy đủ** message
- Cùng topic, cùng group → các instance **chia nhau** message (load balancing)
- Tại sao Kafka tốt hơn gọi trực tiếp cho fan-out pattern

---

## Case 3 — Audit Log

> **Concept:** Log Retention, Replay Events, Offset Management

### Bài toán

Ngành y tế yêu cầu lưu toàn bộ lịch sử thao tác (ai xem hồ sơ ai, ai kê đơn gì)
trong ít nhất 1 năm. Cần có khả năng replay lại toàn bộ lịch sử khi cần.

### Thiết kế Topic

```
Topic: clinic.audit-log
Retention: 365 ngày  ← giữ message lâu, khác các topic khác
Partitions: 6        ← volume cao hơn

Events:
  patient.record.viewed      → ai xem hồ sơ bệnh nhân nào
  prescription.created       → bác sĩ nào kê đơn gì
  prescription.dispensed     → dược sĩ nào cấp thuốc
  payment.processed          → thanh toán
  diagnosis.updated          → cập nhật chẩn đoán
```

### Tạo topic với retention dài

```typescript
// scripts/create-audit-topic.ts
import { kafka } from "../lib/kafka";

const admin = kafka.admin();

async function createAuditTopic() {
  await admin.connect();

  await admin.createTopics({
    topics: [
      {
        topic: "clinic.audit-log",
        numPartitions: 6,
        replicationFactor: 1,
        configEntries: [
          { name: "retention.ms", value: String(365 * 24 * 60 * 60 * 1000) }, // 1 năm
          { name: "cleanup.policy", value: "delete" },
        ],
      },
    ],
  });

  await admin.disconnect();
  console.log("Audit topic created with 365-day retention");
}

createAuditTopic();
```

### Code

```typescript
// types/audit.ts
export type AuditEvent = {
  eventId: string;
  eventType: string;
  actorId: string; // ai thực hiện
  actorRole: "doctor" | "nurse" | "pharmacist" | "admin";
  targetId: string; // đối tượng bị tác động (patientId, prescriptionId...)
  targetType: string;
  action: string;
  metadata?: Record<string, unknown>;
  timestamp: string;
};
```

```typescript
// producers/audit.producer.ts
import { kafka } from "../lib/kafka";
import { AuditEvent } from "../types/audit";
import { v4 as uuid } from "uuid";

const producer = kafka.producer({ idempotent: true }); // đảm bảo không duplicate

export async function publishAuditEvent(
  event: Omit<AuditEvent, "eventId" | "timestamp">
) {
  await producer.connect();

  const fullEvent: AuditEvent = {
    ...event,
    eventId: uuid(),
    timestamp: new Date().toISOString(),
  };

  await producer.send({
    topic: "clinic.audit-log",
    messages: [
      {
        key: event.actorId, // partition theo actorId
        value: JSON.stringify(fullEvent),
      },
    ],
  });

  await producer.disconnect();
}
```

```typescript
// consumers/audit.consumer.ts — với offset control
import { kafka } from "../lib/kafka";

const consumer = kafka.consumer({
  groupId: "audit-processor",
  sessionTimeout: 30000,
});

async function run() {
  await consumer.connect();
  await consumer.subscribe({
    topic: "clinic.audit-log",
    fromBeginning: true, // đọc từ đầu để replay
  });

  await consumer.run({
    autoCommit: false, // tự quản lý offset
    eachMessage: async ({ topic, partition, message, heartbeat }) => {
      const event: AuditEvent = JSON.parse(message.value!.toString());

      await saveAuditToDatabase(event);

      // Commit offset thủ công sau khi xử lý thành công
      await consumer.commitOffsets([
        {
          topic,
          partition,
          offset: (Number(message.offset) + 1).toString(),
        },
      ]);

      await heartbeat(); // báo cho Kafka biết consumer còn sống
    },
  });
}

run();
```

```typescript
// scripts/replay-audit.ts — Replay lại toàn bộ audit log từ một thời điểm
import { kafka } from "../lib/kafka";

async function replayFrom(fromTimestamp: Date) {
  const admin = kafka.admin();
  await admin.connect();

  // Tìm offset tương ứng với timestamp
  const offsets = await admin.fetchTopicOffsetsByTime(
    "clinic.audit-log",
    fromTimestamp.getTime()
  );

  await admin.disconnect();

  // Tạo consumer mới để replay
  const consumer = kafka.consumer({ groupId: `audit-replay-${Date.now()}` });
  await consumer.connect();

  // Seek đến offset của timestamp đó
  consumer.on("consumer.group_join", async () => {
    for (const { partition, offset } of offsets) {
      await consumer.seek({
        topic: "clinic.audit-log",
        partition,
        offset: offset ?? "0",
      });
    }
  });

  await consumer.subscribe({ topic: "clinic.audit-log" });
  await consumer.run({
    eachMessage: async ({ message }) => {
      const event = JSON.parse(message.value!.toString());
      console.log("[Replay]", event.timestamp, event.eventType, event.actorId);
    },
  });
}

// Replay từ 1 tuần trước
replayFrom(new Date(Date.now() - 7 * 24 * 60 * 60 * 1000));
```

### Bạn học được gì

- `retention.ms` — cách giữ message lâu trong Kafka
- `fromBeginning: true` — đọc lại toàn bộ history
- `autoCommit: false` — tự kiểm soát offset để không mất data
- Replay theo timestamp — seek đến thời điểm bất kỳ

---

## Case 4 — Queue số thứ tự phòng khám

> **Concept:** Partition Key, Ordering Guarantee, Kafka Scaling

### Bài toán

Mỗi bệnh nhân check-in sẽ vào hàng chờ theo thứ tự. Trạng thái của 1 bệnh nhân
phải được xử lý **đúng thứ tự**: `waiting → called → in-consultation → done`.
Nếu xử lý sai thứ tự, hệ thống sẽ hiển thị sai trạng thái.

### Kafka đảm bảo thứ tự thế nào

```
Kafka chỉ đảm bảo thứ tự TRONG 1 partition.
→ Dùng patientId làm partition key
→ Tất cả event của 1 bệnh nhân đều vào cùng 1 partition
→ Thứ tự được đảm bảo
```

### Thiết kế Topic

```
Topic: clinic.queue
Partitions: 10  ← nhiều partition để scale

Partition key = patientId
→ patient-001 luôn vào partition 3
→ patient-002 luôn vào partition 7
→ Thứ tự các event của mỗi bệnh nhân được đảm bảo
```

### Code

```typescript
// types/queue.ts
export type QueueEvent = {
  patientId: string;
  patientName: string;
  queueNumber: number;
  status: "checked-in" | "waiting" | "called" | "in-consultation" | "done";
  doctorId?: string;
  room?: string;
  timestamp: string;
};
```

```typescript
// producers/queue.producer.ts
import { kafka } from "../lib/kafka";
import { QueueEvent } from "../types/queue";

const producer = kafka.producer();

export async function publishQueueEvent(event: QueueEvent) {
  await producer.connect();

  await producer.send({
    topic: "clinic.queue",
    messages: [
      {
        key: event.patientId, // ← PARTITION KEY quan trọng nhất
        value: JSON.stringify(event),
        headers: {
          status: event.status,
        },
      },
    ],
  });

  await producer.disconnect();
}
```

```typescript
// consumers/queue-display.consumer.ts — cập nhật màn hình hiển thị
import { kafka } from "../lib/kafka";
import { QueueEvent } from "../types/queue";

const consumer = kafka.consumer({ groupId: "queue-display" });

// State in-memory (thực tế dùng Redis)
const queueState = new Map<string, QueueEvent>();

async function run() {
  await consumer.connect();
  await consumer.subscribe({ topic: "clinic.queue", fromBeginning: false });

  await consumer.run({
    eachMessage: async ({ partition, message }) => {
      const event: QueueEvent = JSON.parse(message.value!.toString());

      // Vì cùng patientId → cùng partition → đúng thứ tự
      queueState.set(event.patientId, event);

      console.log(
        `[Partition ${partition}] Patient ${event.patientName}:`,
        event.status,
        `(Queue #${event.queueNumber})`
      );

      // Cập nhật UI realtime
      await broadcastQueueUpdate(event);
    },
  });
}

run();
```

```typescript
// Simulate một bệnh nhân đi qua toàn bộ flow
async function simulatePatientFlow(patientId: string) {
  const statuses: QueueEvent["status"][] = [
    "checked-in",
    "waiting",
    "called",
    "in-consultation",
    "done",
  ];

  for (const status of statuses) {
    await publishQueueEvent({
      patientId,
      patientName: "Nguyen Van A",
      queueNumber: 5,
      status,
      doctorId: "doctor-001",
      room: "Room 3",
      timestamp: new Date().toISOString(),
    });

    await new Promise((resolve) => setTimeout(resolve, 1000)); // delay 1s
  }
}

simulatePatientFlow("patient-001");
```

### Kiểm tra partition assignment

```typescript
// scripts/check-partitions.ts — xem message nào vào partition nào
import { kafka } from "../lib/kafka";

const admin = kafka.admin();

async function checkPartitions() {
  await admin.connect();

  const offsets = await admin.fetchTopicOffsets("clinic.queue");
  console.log("Partition offsets:", offsets);

  await admin.disconnect();
}

checkPartitions();
```

### Bạn học được gì

- Tại sao cần partition key để đảm bảo ordering
- Kafka hash partition key → cùng key luôn vào cùng partition
- Tăng số partitions để scale throughput
- Trade-off: nhiều partition hơn → ordering chỉ trong partition đó

---

## Checklist học tập

### Tuần 1 — Case 1

- [ ] Setup Docker + Kafka local thành công
- [ ] Viết Producer gửi được message
- [ ] Viết Consumer đọc được message
- [ ] Hiểu Topic và Partition là gì
- [ ] Biết message key dùng để làm gì

### Tuần 2 — Case 2

- [ ] Chạy 2+ consumer với groupId khác nhau
- [ ] Verify cả 2 consumer đều nhận được message
- [ ] Thử cùng groupId với 2 instance → thấy load balancing
- [ ] Hiểu sự khác biệt giữa cùng group vs khác group

### Tuần 3 — Case 3

- [ ] Tạo topic với retention 365 ngày
- [ ] Thử `fromBeginning: true` để đọc lại history
- [ ] Tắt `autoCommit`, tự commit offset thủ công
- [ ] Replay events từ một timestamp cụ thể

### Tuần 4 — Case 4

- [ ] Publish nhiều event với cùng `patientId`
- [ ] Verify tất cả đều vào cùng 1 partition
- [ ] Thử đổi key → thấy partition thay đổi
- [ ] Hiểu ordering guarantee chỉ trong 1 partition

---

## Tài liệu tham khảo

- [KafkaJS Documentation](https://kafka.js.org/)
- [Kafka: The Definitive Guide (free)](https://www.confluent.io/resources/kafka-the-definitive-guide/)
- [Confluent Developer Tutorials](https://developer.confluent.io/tutorials/)
