package com.spring.carebookie.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.spring.carebookie.common.constants.BookStatus;
import com.spring.carebookie.common.mappers.BookMapper;
import com.spring.carebookie.dto.edit.BookCancelDto;
import com.spring.carebookie.dto.response.BookResponseDto;
import com.spring.carebookie.dto.response.InvoiceResponseDto;
import com.spring.carebookie.dto.save.BookSaveDto;
import com.spring.carebookie.entity.BookEntity;
import com.spring.carebookie.entity.InvoiceEntity;
import com.spring.carebookie.entity.InvoiceShareEntity;
import com.spring.carebookie.entity.MedicineEntity;
import com.spring.carebookie.entity.ServiceBookEntity;
import com.spring.carebookie.entity.ServiceEntity;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.BookRepository;
import com.spring.carebookie.repository.InvoiceMedicineRepository;
import com.spring.carebookie.repository.InvoiceRepository;
import com.spring.carebookie.repository.InvoiceServiceRepository;
import com.spring.carebookie.repository.InvoiceShareRepository;
import com.spring.carebookie.repository.MedicineRepository;
import com.spring.carebookie.repository.ServiceBookRepository;
import com.spring.carebookie.repository.ServiceRepository;
import com.spring.carebookie.repository.projection.InvoiceMedicineAmountProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final InvoiceShareRepository invoiceShareRepository;

    private final ServiceBookRepository serviceBookRepository;

    private final ServiceRepository serviceRepository;

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMedicineRepository invoiceMedicineRepository;

    private final InvoiceServiceRepository invoiceServiceRepository;

    private final MedicineRepository medicineRepository;

    private static final BookMapper BOOK_MAPPER = BookMapper.INSTANCE;

    @Transactional
    public BookResponseDto saveBook(BookSaveDto dto) {

        BookEntity entity = BOOK_MAPPER.convertSaveDtoToEntity(dto);
        entity.setStatus(BookStatus.PENDING.toString());
        entity.setDateTimeBook(LocalDateTime.now());

        BookEntity saveBook = bookRepository.save(entity);
        List<InvoiceResponseDto> invoiceResponseDtos = new ArrayList<>();
        List<ServiceEntity> serviceBooks = new ArrayList<>();
        //add service in
        List<ServiceBookEntity> serviceBookEntities = new ArrayList<>();
        if (dto.getServices().size() > 0) {
            dto.getServices()
                    .forEach(s -> serviceBookEntities.add(new ServiceBookEntity(null, saveBook.getId(), s)));
            serviceBookRepository.saveAll(serviceBookEntities);
            // Response list of service book entity
            serviceBooks = serviceRepository.findAllById(dto.getServices());
        }

        List<InvoiceShareEntity> invoiceShares = new ArrayList<>();
        if (dto.isShareInvoice() && dto.getInvoices().size() > 0) {
            // add invoice share
            dto.getInvoices()
                    .forEach(s -> invoiceShares.add(new InvoiceShareEntity(null, saveBook.getId(), s)));
            invoiceShareRepository.saveAll(invoiceShares);

            // Response list of invoice share include list of medicine and list of service share
            List<InvoiceEntity> invoices = invoiceRepository.findAllById(dto.getInvoices());

            invoices.forEach(i -> {
                List<ServiceEntity> serviceInvoice = invoiceRepository.getAllServiceByInvoiceId(i.getId());
                List<InvoiceMedicineAmountProjection> medicineInvoice = invoiceRepository.getAllMedicineByInvoiceId(i.getId());
                invoiceResponseDtos.add(new InvoiceResponseDto(i, serviceInvoice, medicineInvoice));
            });
        }

        return new BookResponseDto(saveBook, serviceBooks, invoiceResponseDtos);
    }

    @Transactional
    public BookEntity acceptBook(Long bookId) {
        bookRepository.acceptBook(bookId);
        BookEntity bookEntity = bookRepository.findById(bookId)
                        .orElseThrow(() -> new ResourceNotFoundException("Book {} not found"
                                .replace("{}",bookId.toString())));

        return bookEntity;
    }

    @Transactional
    public BookEntity confirmBook(Long bookId) {
        bookRepository.confirmBook(bookId);
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book {} not found"
                        .replace("{}",bookId.toString())));

        return bookEntity;
    }

    @Transactional
    public BookResponseDto cancelBook(BookCancelDto dto) {

        bookRepository.cancelBook(dto.getBookId(), dto.getMessage());
        BookEntity bookEntity = bookRepository.findById(dto.getBookId())
                .orElseThrow(() ->
                        new ResourceAccessException("Not found book with {}"
                                .replace("{}", dto.getBookId().toString())));

        List<Long> serviceBookIds = bookRepository.getServiceIdsByBookId(dto.getBookId());

        // Service
        List<ServiceEntity> serviceBooks = new ArrayList<>();
        if (serviceBookIds.size() > 0) {
            serviceBooks = serviceRepository.findAllById(serviceBookIds);
        }

        // Invoice share
        List<InvoiceResponseDto> invoiceResponseDtos = new ArrayList<>();
        if (bookEntity.isShareInvoice()) {
            List<Long> invoiceShareIds = bookRepository.getInvoiceShareIdsByBookId(dto.getBookId());

            // Response list of invoice share include list of medicine and list of service share
            List<InvoiceEntity> invoices = invoiceRepository.findAllById(invoiceShareIds);

            invoices.forEach(i -> {
                List<ServiceEntity> serviceInvoice = invoiceRepository.getAllServiceByInvoiceId(i.getId());
                List<InvoiceMedicineAmountProjection> medicineInvoice = invoiceRepository.getAllMedicineByInvoiceId(i.getId());
                invoiceResponseDtos.add(new InvoiceResponseDto(i, serviceInvoice, medicineInvoice));
            });
        }
        return new BookResponseDto(bookEntity, serviceBooks, invoiceResponseDtos);
    }

    public List<BookResponseDto> getAllBookAcceptByDoctorId(String doctorId) {
        List<BookEntity> bookEntities = bookRepository.getBookAcceptByDoctorId(doctorId);
        return getAllBookByStatus(bookEntities);
    }

    public List<BookResponseDto> getAllBookConfirmByDoctorId(String doctorId) {
        List<BookEntity> bookEntities = bookRepository.getBookConfirmByDoctorId(doctorId);
        return getAllBookByStatus(bookEntities);
    }

    /**
     * All status
     *
     * @param bookEntities
     * @return
     */
    private List<BookResponseDto> getAllBookByStatus(List<BookEntity> bookEntities) {
        List<BookResponseDto> bookResponseDtos = new ArrayList<>();

        // ignore book date after date now
        bookEntities.removeIf(b ->b.getDateExamination().isBefore(LocalDate.now()));

        bookEntities.forEach(b -> {
            List<Long> serviceBookIds = bookRepository.getServiceIdsByBookId(b.getId());
            // Service
            List<ServiceEntity> serviceBooks = new ArrayList<>();
            if (serviceBookIds.size() > 0) {
                serviceBooks = serviceRepository.findAllById(serviceBookIds);
            }

            // Invoice share
            List<InvoiceResponseDto> invoiceResponseDtos = new ArrayList<>();
            if (b.isShareInvoice()) {
                List<Long> invoiceShareIds = bookRepository.getInvoiceShareIdsByBookId(b.getId());

                // Response list of invoice share include list of medicine and list of service share
                List<InvoiceEntity> invoices = invoiceRepository.findAllById(invoiceShareIds);

                invoices.forEach(i -> {
                    List<ServiceEntity> serviceInvoice = invoiceRepository.getAllServiceByInvoiceId(i.getId());
                    List<InvoiceMedicineAmountProjection> medicineInvoice = invoiceRepository.getAllMedicineByInvoiceId(i.getId());
                    invoiceResponseDtos.add(new InvoiceResponseDto(i, serviceInvoice, medicineInvoice));
                });
            }

            bookResponseDtos.add(new BookResponseDto(b, serviceBooks, invoiceResponseDtos));
        });

        return bookResponseDtos;
    }

}
