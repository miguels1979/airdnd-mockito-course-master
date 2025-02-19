package com.kosmostecnologia.airdnd;

import com.kosmostecnologia.airdnd.Services.BookingService;
import com.kosmostecnologia.airdnd.Services.PaymentService;
import com.kosmostecnologia.airdnd.Services.RoomService;
import com.kosmostecnologia.airdnd.dto.BookingDto;
import com.kosmostecnologia.airdnd.helpers.MailHelper;
import com.kosmostecnologia.airdnd.repositories.BookingRepository;
import com.kosmostecnologia.airdnd.repositories.PaymentRepository;
import com.kosmostecnologia.airdnd.repositories.RoomRepository;

import java.time.LocalDate;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        final var paymentService = new PaymentService(new PaymentRepository());
        final var roomService = new RoomService(new RoomRepository());
        final var bookingRepository = new BookingRepository();
        final var mailHelper = new MailHelper();
        final var bookingService = new BookingService(
                paymentService,
                roomService,
                bookingRepository,
                mailHelper
        );

        var randomId = UUID.randomUUID().toString();
        var bookingDto = new BookingDto(
                randomId,
                LocalDate.of(2023, 6, 10),
                LocalDate.of(2023, 6, 20),
                2,
                true
        );
        var bookingResult = bookingService.booking(bookingDto);
        System.out.println(bookingResult);

        bookingService.unbook(bookingResult);

        var price = bookingService.calculatePrice(bookingDto);

        System.out.println(price);

        var priceMxn = bookingService.calculateInMxn(bookingDto);
        System.out.println(priceMxn);

        var roomsAvailable = bookingService.getAvailablePlaceCount();

        System.out.println(roomsAvailable);
    }
}