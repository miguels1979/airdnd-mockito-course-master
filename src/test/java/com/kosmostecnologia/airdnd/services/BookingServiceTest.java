package com.kosmostecnologia.airdnd.services;


import com.kosmostecnologia.airdnd.dto.BookingDto;
import com.kosmostecnologia.airdnd.helpers.MailHelper;
import com.kosmostecnologia.airdnd.repositories.BookingRepository;
import com.kosmostecnologia.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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
    @DisplayName("booking without exceptions")
    void bookingWithoutException(){
        final String expected = UUID.randomUUID().toString();

        //when(this.roomServiceMock.findAvailableRoom(DataDummy.DEFAULT_BOOKING_REQ_2)).thenReturn(DataDummy.DEFAULT_ROOMS_LIST.get(0));

        //NO SE LLAMA AL MÉTODO REAL
        doReturn(DataDummy.DEFAULT_ROOMS_LIST.get(0)).when(this.roomServiceMock).findAvailableRoom(DataDummy.DEFAULT_BOOKING_REQ_2);

        //SI SE LLAMA AL MÉTODO REAL, PODRÍA LANZAR UNA EXCEPCIÓN SI EL MÉTODO LA TIENE
        //Cuando se tiene que SER MUY ESTRICTO con los parámetros se tiene que realizar lo siguiente(evalúa argumentos específicos)
        when(this.bookingRepositoryMock.save(DataDummy.DEFAULT_BOOKING_REQ_2)).thenReturn(expected);

        //Cuando NO ES NECESARIO SER MUY ESTRICTO con los parámetros se puede realizar lo siguiente
//        when(this.roomServiceMock.findAvailableRoom(any(BookingDto.class))).thenReturn(DataDummy.DEFAULT_ROOMS_LIST.get(0));
//        when(this.bookingRepositoryMock.save(any(BookingDto.class))).thenReturn(expected);

        //Mockear un método void y luego hacer un verify
        doNothing().when(this.roomServiceMock).bookRoom(anyString());

        String actual = this.bookingService.booking(DataDummy.DEFAULT_BOOKING_REQ_2);
        assertEquals(expected,actual);

        //PARA VERIFICAR QUE UN MÉTODO SE HA LLAMADO CORRECTAMENTE, si no se llaman aparece un error.
        verify(this.roomServiceMock,times(1)).findAvailableRoom(any(BookingDto.class));
        verify(this.bookingRepositoryMock).save(any(BookingDto.class));

        //SE VERIFICA EL MÉTODO MOCK VOID
        verify(this.roomServiceMock,times(1)).bookRoom(anyString());

    }


    @Test
    @DisplayName("booking with exceptions")
    void bookingWithException(){

        //when(this.roomServiceMock.findAvailableRoom(DataDummy.DEFAULT_BOOKING_REQ_2)).thenReturn(DataDummy.DEFAULT_ROOMS_LIST.get(0));
        //NO SE LLAMA AL MÉTODO REAL
        doReturn(DataDummy.DEFAULT_ROOMS_LIST.get(0)).when(this.roomServiceMock).findAvailableRoom(DataDummy.DEFAULT_BOOKING_REQ_4);

        //MOCK CON EXCEPCIONES ,MODO 1 - TOTALMENTE MOCK
        //doThrow(new IllegalArgumentException("Max 3 guest")).when(this.paymentServiceMock).pay(any(BookingDto.class),anyDouble());

        //MOCK CON EXCEPCIONES ,MODO 1 Y PARÁMETROS EXACTOS
        doThrow(new IllegalArgumentException("Max 3 guest")).when(this.paymentServiceMock).pay(eq(DataDummy.DEFAULT_BOOKING_REQ_4),eq(320.0));

        //MOCK CON EXCEPCIONES ,MODO 2 - PARCIALMENTE MOCK
        //when(this.paymentServiceMock.pay(any(BookingDto.class),anyDouble())).thenThrow(new IllegalArgumentException("Max 3 guest"));

        //Guardamos la excepción
        Executable executable =()-> this.bookingService.booking(DataDummy.DEFAULT_BOOKING_REQ_4);

        //Usamos el assertTrows()
        assertThrows(IllegalArgumentException.class,executable);

    }

}
