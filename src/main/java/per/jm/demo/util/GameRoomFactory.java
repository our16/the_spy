package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;

import java.util.HashMap;
import java.util.Map;

public class GameRoomFactory {
    //让它一直在内存中切唯一
    private final static class get{
        private static  final Map<String, GameRoom> rooms = new HashMap<>() ; //新建一个总的房间，其他房间都放在这里面,key值是房间号
    }
    public static Map<String, GameRoom> getRooms() {
        return get.rooms;
    }
    public void GameRoomFactory(){};
}

