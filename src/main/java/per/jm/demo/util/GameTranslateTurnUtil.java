package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameTranslateTurnUtil {


    public static void translate(GameRoom room, GameUserInfor oldUser){
        System.out.println("选取新发言人");
        boolean hasNext = false; //查看是否轮完了,这里默认表示没有下一个人了
        //当前用户说完了，才进行下一个玩家发言
        //System.out.println("room :"+room);
        if(room != null && oldUser.isThisTurnIsSpoken())
        {
            for(GameUserInfor user : room.getUsers())
            {
                if(!user.isThisTurnIsSpoken() && user.isStatus()) //还没有说过话,并且要是存活的人
                {
                    room.setUserId(user.getGameId()); //设置新的发言人
                    room.setOpenId(user.getOpenId());
                    System.out.println("新发言人id"+user.getOpenId());
                    hasNext = true;
                    break; //一次只设置一位
                }
            }
            //没有下一位了
            if(!hasNext)
            {
                room.setSpeak(false); //关闭独自发言
               // room.setFreeSpeakTime(true); //开启自由发言
                System.out.println("//本轮结束,开始投票");
                room.setSpeak(false);
                room.setThisRurnAt(0); //重置人数
                //gameRoom.setFreeSpeakTime(true);
                   /* Map<String,String> map = new HashMap<>();
                    map.put("first","开始投票");*/
                //推送消息
                //gameRoom.setPolling(true);
                Map<String,String> map = new HashMap<>();
                map.put("first","开始投票");
                String user = "";
                for(GameUserInfor userInfor: room.getUsers())
                {
                    //没有阵亡才显示
                    if(userInfor.isStatus())
                    user += "id:"+userInfor.getGameId()+"姓名："+userInfor.getWxName()+"\n";
                }
                map.put("keyword1",user);
                map.put("remark",new Date()+"");
                WxMessageUtil.sendToAllPlayer(room,"",map);
                //this.wait = true;
                room.getGth().setWait(true);
                room.setPolling(true);
            }
            else
            {
                //继续游戏
                System.out.println("继续游戏");
            }
        }
    }
}
