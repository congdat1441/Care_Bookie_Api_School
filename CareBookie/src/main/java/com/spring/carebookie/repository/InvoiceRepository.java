package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.InvoiceEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.repository.projection.InvoiceMedicineAmountProjection;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    @Query(value = "select  s \n" +
            "from InvoiceEntity i \n" +
            "left join InvoiceServiceEntity iser on i.id = iser.invoiceId \n" +
            "join ServiceEntity s on iser.serviceId = s.id \n" +
            "where i.id = :invoiceId")
    List<ServiceEntity> getAllServiceByInvoiceId(Long invoiceId);

    @Query(value = "select  me, ime.amount \n" +
            "from InvoiceEntity i \n" +
            "left join InvoiceMedicineEntity ime on i.id = ime.invoiceId \n" +
            "join MedicineEntity me on ime.medicineId = me.id \n" +
            "where i.id = :invoiceId")
    List<InvoiceMedicineAmountProjection> getAllMedicineByInvoiceId(Long invoiceId);
}
