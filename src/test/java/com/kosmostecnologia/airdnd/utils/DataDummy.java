package com.kosmostecnologia.airdnd.utils;

import com.kosmostecnologia.airdnd.dto.BookingDto;
import com.kosmostecnologia.airdnd.dto.RoomDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DataDummy {

    private DataDummy(){

    }
    public static final Map<RoomDto,Boolean> DEFAULT_ROOMS = Map.ofEntries(

            Map.entry(new RoomDto("A", 2),true),
            Map.entry(new RoomDto("B", 2),true),
            Map.entry(new RoomDto("C", 3),true),
            Map.entry(new RoomDto("D", 2), false),
            Map.entry(new RoomDto("E", 2),false),
            Map.entry(new RoomDto("F", 3),false)
   );

    public static final List<RoomDto> DEFAULT_ROOMS_LIST = List.of(
            new RoomDto("A",2),
            new RoomDto("B",2),
            new RoomDto("C",3),
            new RoomDto("D",2),
            new RoomDto("E",2),
            new RoomDto("F",3)
    );

    public static final BookingDto DEFAULT_BOOKING_REQ_1 = new BookingDto("18318", LocalDate.of(2023,6,10),
            LocalDate.of(2023,6,20),2,false);


    public static final BookingDto DEFAULT_BOOKING_REQ_2 = new BookingDto("3784193", LocalDate.of(2023,6,10),
            LocalDate.of(2023,6,20),2,false);

    public static final BookingDto DEFAULT_BOOKING_REQ_3 = new BookingDto("589585", LocalDate.of(2023,6,10),
            LocalDate.of(2023,6,26),2,false);

}
