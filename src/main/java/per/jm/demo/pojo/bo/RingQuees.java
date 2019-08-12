package per.jm.demo.pojo.bo;

import per.jm.demo.util.GameRoomFactory;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * 保存当前指针指到的节点
 * */
public class RingQuees {
        //让它一直在内存中切唯一
    private final static class get{
        //新建一个总的房间，其他房间都放在这里面,key值是房间号
        private static  final Map<String,QueesList> rooms = new HashMap<>() ;
    }
    public static Map<String, QueesList> getRooms() {
        return get.rooms;
    }
    public void RingQuees(){};
/**
 *
 * 更新这个房间在队列中的位置
 * 有就更新，没有就添加
 *
 * */
    public static void setNewPot(String roomId){
        GameRoom room = GameRoomFactory.getRooms().get(roomId);
        QueesList queesList = get.rooms.get("timer");//当前节点
        System.out.println("玩家有操作前，当前节点："+queesList.getNum());
        QueesList point = queesList; //定义指针
        if(room != null) {
            if(room.getPosition() != null) {
                //更新信息
                while (true) {
                    if (room.getPosition() == point.getNum()) {
                        //删除前房间在所节点的信息
                        point.getGameRooms().remove(room.getRoomId());
                        System.out.println("移除了一个位置:" + room.getPosition());
                        System.out.println("玩家有操作中，当前节点："+queesList.getNum());
                        //把房间信息放到当前节点的前一个节点
                        queesList.getBefore().getGameRooms().put(room.getRoomId(),room);
                        //重新设置当前房间所在节点位置
                        room.setPosition(queesList.getBefore().getNum());
                        System.out.println("更新一个位置:" + room.getPosition());
                        break;
                    }
                    point = point.getNext();
                    if(point == null){
                        break;
                    }
                }
            }else {
                //添加信息
                room.setPosition(queesList.getBefore().getNum());
                queesList.getBefore().getGameRooms().put(room.getRoomId(), room);
                System.out.println("新增到一个位置:" + room.getPosition());
            }
            System.out.println("玩家有操作后，当前节点："+queesList.getNum());
            //point = queesList; //还原位置
        }
    }
}
