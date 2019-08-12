package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSpeakUtil {
    /**
     * 这个工具类用来玩家对关键字的描述
     *
     * **/
    public static void translareMessage(GameRoom room){
       int userId = room.getUserId(); //当前发言人的游戏id
        List<GameUserInfor> list = room.getUsers();
        GameUserInfor userInfor = null;
        for(GameUserInfor user: list)
        {
            if(userId == user.getGameId())
            {
                userInfor = user;
                userInfor.setThisTurnIsSpoken(true); //本次阐明过观点了
                break;
            }
        }
        //避免错发
        if(userInfor.getContent() != null) {
            //调用消息转发接口
            Map<String, String> map = new HashMap<>();
            map.put("keyword1", userInfor.getWxName() + "说：" + userInfor.getContent());
            map.put("remark", new Date() + "");
            WxMessageUtil.translateSpeakContent(room, userInfor, map);
            //消息发送之后才重置
            userInfor.setContent(null);
            //消息发送之后选取下一位发言,前提是:不是自由发言时间
            if (!room.isFreeSpeakTime()) GameTranslateTurnUtil.translate(room, userInfor);
        }
    }
}
