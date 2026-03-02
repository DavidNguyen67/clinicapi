package com.clinicsystem.clinicapi.repository;

import com.clinicsystem.clinicapi.model.InvoiceItem;
import com.clinicsystem.clinicapi.model.InvoiceItem.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, UUID> {

    List<InvoiceItem> findByInvoiceId(UUID invoiceId);

    List<InvoiceItem> findByItemType(ItemType itemType);

    List<InvoiceItem> findByInvoiceIdAndItemType(UUID invoiceId, ItemType itemType);

    @Query("SELECT ii FROM InvoiceItem ii WHERE ii.invoice.id = :invoiceId " +
            "ORDER BY ii.itemType, ii.itemName")
    List<InvoiceItem> findByInvoiceIdOrderByType(@Param("invoiceId") UUID invoiceId);
}
