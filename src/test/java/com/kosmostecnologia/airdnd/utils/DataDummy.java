package com.kosmostecnologia.airdnd.utils;

import com.kosmostecnologia.airdnd.dto.RoomDto;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DataDummy {

    private DataDummy(){

    }
    public static final Map<RoomDto,Boolean> DEFAULTS_ROOMS = Map.ofEntries(

            Map.entry(new RoomDto("A", 2),true),
            Map.entry(new RoomDto("B", 2),true),
            Map.entry(new RoomDto("C", 3),true),
            Map.entry(new RoomDto("D", 2), false),
            Map.entry(new RoomDto("E", 2),false),
            Map.entry(new RoomDto("F", 3),false)
   );

}
