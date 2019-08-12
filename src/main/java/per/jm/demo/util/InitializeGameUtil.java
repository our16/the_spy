package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

/**
 * 游戏开始，一轮结束
 * 重新初始化游戏
 *
 * */
public class InitializeGameUtil {
    public static void init(GameRoom room){
        room.getGth().setOver(false);
        for(GameUserInfor user: room.getUsers())
        {
            //重置说话权限
            user.setThisTurnIsSpoken(false);
            //重置票数
            user.setPoll(0);
            //重置投票对象
            user.setPollWho(-1);
            //重置为都不是卧底
            user.setTheSpy(false);
            //重置为没投过票
            user.setNotPoll(true);
            //设置玩家都为存活状态
            user.setStatus(true);
        }
    }
}
