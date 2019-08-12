package per.jm.demo.util;

import java.util.HashMap;
import java.util.Map;

public class GameRoomIdForPlayer {
    private final static class roomID{
        private static final Map<String,String> userJoinedRoom = new HashMap<>();//用来保存用户加入了的房间的id，确保唯一性
    }
    public static Map<String, String> getRoomId(){
        return roomID.userJoinedRoom;
    }
    public void GameRoomIdForPlayer(){};

}
