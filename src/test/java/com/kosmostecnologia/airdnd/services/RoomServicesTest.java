package com.kosmostecnologia.airdnd.services;

import com.kosmostecnologia.airdnd.dto.RoomDto;
import com.kosmostecnologia.airdnd.repositories.RoomRepository;
import com.kosmostecnologia.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServicesTest {

    @Mock
    private RoomRepository roomRepositoryMock;

    @InjectMocks
    private RoomService roomService;

//    @BeforeEach
//    void init (){
//        this.roomRepositoryMock = mock(RoomRepository.class);
//        this.roomService=new RoomService(roomRepositoryMock);
//    }

    @Test
    @DisplayName("Should get all rooms available in room repository")
    void findAllAvailableRooms(){
        when(roomRepositoryMock.findAll()).thenReturn(DataDummy.DEFAULT_ROOMS);
        int expected = 3;
        List<RoomDto> result = this.roomService.findAllAvailableRooms();
        assertEquals(expected,result.size());

    }

}
