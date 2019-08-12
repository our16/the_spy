package per.jm.demo.pojo.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 队列中每个节点的一些信息
 * 1.房间信息
 * 2.房间中玩家信息(可不写)
 * */
public class QueesList {
    private Integer num; //当前节点的标记值
    private Integer beforeNum;// 前一个结点的标记值
    private QueesList before;//前一个结点
    private QueesList next;//下一个节点
    private Map<String,GameRoom> gameRooms = new HashMap<>(); //当前节点有哪些房间

    public Map<String, GameRoom> getGameRooms() {
        return gameRooms;
    }

    public void setGameRooms(Map<String, GameRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }

    public  QueesList(Integer num, Integer beforeNum, QueesList before, QueesList next, GameRoom gameRoom){
        this.before = before;
        this.beforeNum = beforeNum;
        this.next = next;
        this.num = num;
        if(gameRoom != null) {
            this.gameRooms.put(gameRoom.getRoomId(),gameRoom);
        }
    }
    public QueesList(){
//无参构造器
    }
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(Integer beforeNum) {
        this.beforeNum = beforeNum;
    }

    public QueesList getBefore() {
        return before;
    }

    public void setBefore(QueesList before) {
        this.before = before;
    }

    public QueesList getNext() {
        return next;
    }

    public void setNext(QueesList next) {
        this.next = next;
    }

}
