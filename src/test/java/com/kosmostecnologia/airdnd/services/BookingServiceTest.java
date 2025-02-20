package com.kosmostecnologia.airdnd.services;


import com.kosmostecnologia.airdnd.dto.BookingDto;
import com.kosmostecnologia.airdnd.helpers.MailHelper;
import com.kosmostecnologia.airdnd.repositories.BookingRepository;
import com.kosmostecnologia.airdnd.utils.CurrencyConverter;
import com.kosmostecnologia.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private PaymentService paymentServiceMock;
    @Mock
    private RoomService roomServiceMock;
    @Spy //El SPY ES UN MOCK QUE SI NO SE LE INDICA EL COMPORTAMIENTO MANDA LLAMAR AL MÉTODO REAL
    private BookingRepository bookingRepositoryMock;
    @Mock
    private MailHelper mailHelperMock;
    @InjectMocks
    private BookingService bookingService;
   @Captor
    private ArgumentCaptor<String> stringCaptor;//Le agrega más granularidad a los test

    @Test
    @DisplayName("getAvailablePlaceCount should works")
    void getAvailablePlaceCount(){

        //when con un solo return
//         when(this.roomServiceMock.findAllAvailableRooms()).thenReturn(DataDummy.DEFAULT_ROOMS_LIST);
//        int expected = 14;
//        int actual = this.bookingService.getAvailablePlaceCount();
//        assertEquals(14,actual);

        //when con múltiples returns
        when(this.roomServiceMock.findAllAvailableRooms())
                .thenReturn(DataDummy.DEFAULT_ROOMS_LIST)
                .thenReturn(Collections.emptyList())
                .thenReturn(DataDummy.SINGLE_ROOMS_LIST);

        int expected1 = 14;
        int expected2 = 0;
        int expected3 = 5;

        int actual1 = this.bookingService.getAvailablePlaceCount();
        int actual2 = this.bookingService.getAvailablePlaceCount();
        int actual3 = this.bookingService.getAvailablePlaceCount();

       assertAll(
               ()-> assertEquals(expected1,actual1),
               ()-> assertEquals(expected2,actual2),
               ()->assertEquals(expected3,actual3)
       );

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

        doNothing().when(this.mailHelperMock).sendMail(anyString(),anyString());

        String actual = this.bookingService.booking(DataDummy.DEFAULT_BOOKING_REQ_2);
        assertEquals(expected,actual);

        //PARA VERIFICAR QUE UN MÉTODO SE HA LLAMADO CORRECTAMENTE, si no se llaman aparece un error.
        verify(this.roomServiceMock,times(1)).findAvailableRoom(any(BookingDto.class));
        verify(this.bookingRepositoryMock).save(any(BookingDto.class));
        verify(this.mailHelperMock,times(1)).sendMail(anyString(),anyString());


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

    @Test
    @DisplayName("unbook should works")
    void unbook(){
        String id1 = "id1";
        String id2 = "id2";

        BookingDto bookingRes1 = DataDummy.DEFAULT_BOOKING_REQ_1;
        bookingRes1.setRoom(DataDummy.DEFAULT_ROOMS_LIST.get(3));

        BookingDto bookingRes2 = DataDummy.DEFAULT_BOOKING_REQ_2;
        bookingRes2.setRoom(DataDummy.DEFAULT_ROOMS_LIST.get(4));

        when(this.bookingRepositoryMock.findById(anyString()))
                .thenReturn(bookingRes1)
                .thenReturn(bookingRes2);

        doNothing().when(this.roomServiceMock).unbookRoom(anyString());
        doNothing().when(this.bookingRepositoryMock).deleteById(anyString());

        this.bookingService.unbook(id1);
        this.bookingService.unbook(id2);

        verify(this.roomServiceMock,times(2)).unbookRoom(anyString());
        verify(this.bookingRepositoryMock,times(2)).deleteById(anyString());

        //Le agrega mas granuralidad a los test y permite usar los asserEquals a un método void
        /*
        ArgumentCaptor es útil para verificar valores pasados a mocks cuando no podemos
         o no queremos usar verify(mock).method(valorExacto) directamente.
        */
        verify(this.bookingRepositoryMock,times(2)).findById(this.stringCaptor.capture());

        assertEquals(List.of(id1,id2),this.stringCaptor.getAllValues());

    }

    @Test
    @DisplayName("currencyConverter should works")
    void currencyConverter(){
        //Un método estático siempre se debe mockear con un try resource
        try(MockedStatic<CurrencyConverter>mockedStatic = mockStatic(CurrencyConverter.class)){
            Double expected = 900.0;
            mockedStatic.when(()-> CurrencyConverter.toMx(anyDouble())).thenReturn(expected);
            Double actual = this.bookingService.calculateInMxn(DataDummy.DEFAULT_BOOKING_REQ_1);
            assertEquals(expected,actual);
        }
        //PRUEBA

    }

}
