package com.clinicsystem.clinicapi.model;

import com.clinicsystem.clinicapi.constant.MessageCode;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items", indexes = {
        @Index(name = "idx_invoice_id", columnList = "invoice_id"),
        @Index(name = "idx_item_type", columnList = "item_type")
})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class InvoiceItem extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_invoice"))
    @NotNull(message = MessageCode.VALIDATION_INVOICE_REQUIRED)
    @JsonBackReference("invoice-items")
    private Invoice invoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false, length = 20)
    @NotNull(message = MessageCode.VALIDATION_ITEM_TYPE_REQUIRED)
    private ItemType itemType;

    @NotBlank(message = MessageCode.VALIDATION_ITEM_NAME_REQUIRED)
    @Column(name = "item_name", nullable = false, length = 255)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity = 1;

    @NotNull(message = MessageCode.VALIDATION_UNIT_PRICE_REQUIRED)
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = MessageCode.VALIDATION_TOTAL_PRICE_REQUIRED)
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    public enum ItemType {
        service, medication, lab_test, other
    }

    // Helper method to calculate total price
    public void calculateTotalPrice() {
        if (this.unitPrice != null && this.quantity != null) {
            this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        }
    }
}
