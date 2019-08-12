package per.jm.demo.util;


import per.jm.demo.pojo.bo.GameRoom;

import java.util.Iterator;

public class GetGameRoomByMakerOpenId {
    public static GameRoom getGameRoomByOpenIg(String openId){

        Iterator it = GameRoomFactory.getRooms().entrySet().iterator();
        for (Object o : GameRoomFactory.getRooms().keySet()) {
            System.out.println("key=" + o + " value=" + GameRoomFactory.getRooms().get(o).getRoomMaker());
            System.out.println(openId.equals(GameRoomFactory.getRooms().get(o).getRoomMaker()));
            if(openId.equals(GameRoomFactory.getRooms().get(o).getRoomMaker()))
            {
               return  GameRoomFactory.getRooms().get(o);
            }
        }
        return null;
    }
}
