package com.kosmostecnologia.airdnd.services;


import com.kosmostecnologia.airdnd.dto.BookingDto;
import com.kosmostecnologia.airdnd.helpers.MailHelper;
import com.kosmostecnologia.airdnd.repositories.BookingRepository;
import com.kosmostecnologia.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private PaymentService paymentServiceMock;
    @Mock
    private RoomService roomServiceMock;
    @Mock
    private BookingRepository bookingRepositoryMock;
    @Mock
    private MailHelper mailHelperMock;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("getAvailablePlaceCount should works")
    void getAvailablePlaceCount(){

        when(this.roomServiceMock.findAllAvailableRooms()).thenReturn(DataDummy.DEFAULT_ROOMS_LIST);
        int expected = 14;
        int actual = this.bookingService.getAvailablePlaceCount();
        assertEquals(14,actual);
    }

    @Test
    @DisplayName("booking should works")
    void booking(){
        final String expected = UUID.randomUUID().toString();
        //Cuando se tiene que ser muy estricto con los parámetros se tiene que realizar lo siguiente
//        when(this.roomServiceMock.findAvailableRoom(DataDummy.DEFAULT_BOOKING_REQ_2)).thenReturn(DataDummy.DEFAULT_ROOMS_LIST.get(0));
//        when(this.bookingRepositoryMock.save(DataDummy.DEFAULT_BOOKING_REQ_2)).thenReturn(expected);

        //Cuando no se tiene que ser muy estricto con los parametros se puede realizar lo siguiente
        when(this.roomServiceMock.findAvailableRoom(any(BookingDto.class))).thenReturn(DataDummy.DEFAULT_ROOMS_LIST.get(0));
        when(this.bookingRepositoryMock.save(any(BookingDto.class))).thenReturn(expected);

        String actual = this.bookingService.booking(DataDummy.DEFAULT_BOOKING_REQ_2);
        assertEquals(expected,actual);
    }

}
