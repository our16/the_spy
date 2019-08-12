package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

import java.util.HashMap;
import java.util.Map;

public class GamePollUtil {
    public  static int polling(String roomId,String openId,int pollWho){
        GameRoom room = GameRoomFactory.getRooms().get(roomId); //进入房间
        for(GameUserInfor user:room.getUsers())
        {
            if(user.getOpenId().equals(openId) && user.isNotPoll())
            {
                System.out.println(openId+"投给:"+pollWho);
                int falg = 1;
                int has = 0;
                for(GameUserInfor user2:room.getUsers())
                {
                    //存在这个玩家
                    if(pollWho == user2.getGameId()) {
                        has = 1;
                        break;
                    }
                }
                if(pollWho == user.getGameId())
                    falg = 0;
                if(falg == 0)
                {
                    //不能投给自己
                    return -1;
                }
                if(has != 1)
                {
                    return -2; //不存在这个玩家
                }
                user.setPollWho(pollWho);
                user.setNotPoll(false);
                int num = room.getPolledNum();
                room.setPolledNum(++num); //投票人数加一
                System.out.println("polledNum:"+num+",liveNum:"+room.getLiveNum());
                if(num == room.getLiveNum())
                {
                    System.out.println("投票结束，等待结算结果");
                    Map<String,String> map = new HashMap<>();
                    map.put("first","投票结束等待结果...");
                    WxMessageUtil.sendToAllPlayer(room,"",map);
                    room.setPoll(true); //投票结束
                    room.setPolling(false); //关闭投票
                    room.getGth().setWait(false); //投票结束时执行一次
                }
                break;
            }else{
                System.out.println("投过票了");
            }
        }
        //投票完成
        return 1;
    }
}
