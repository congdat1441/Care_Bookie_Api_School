package com.spring.carebookie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.carebookie.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Modifying
    @Query("update BookEntity b set b.status = 'CANCEL', b.message = :message where b.id = :id")
    void cancelBook(Long id, String message);

    @Modifying
    @Query("update BookEntity b set b.status = 'ACCEPT' where b.id = :id")
    void acceptBook(Long id);

    @Modifying
    @Query("update BookEntity b set b.status = 'CONFIRM' where b.id = :id")
    void confirmBook(Long id);

    @Query("select sb.serviceId from BookEntity b \n" +
            "join ServiceBookEntity sb on b.id = sb.bookId \n" +
            "where b.id = :bookId")
    List<Long> getServiceIdsByBookId(Long bookId);

    @Query("select ish.invoiceId from BookEntity b \n" +
            "join InvoiceShareEntity ish on b.id = ish.bookId \n" +
            "where b.id = :bookId")
    List<Long> getInvoiceShareIdsByBookId(Long bookId);

    @Query("select b from BookEntity b where b.doctorId = ?1 and b.status ='ACCEPT' ")
    List<BookEntity> getBookAcceptByDoctorId(String doctorId);

    @Query("select b from BookEntity b where b.doctorId = ?1 and b.status ='CONFIRM' ")
    List<BookEntity> getBookConfirmByDoctorId(String doctorId);
}
