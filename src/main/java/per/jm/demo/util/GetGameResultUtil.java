package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

public class GetGameResultUtil {

 public static GameUserInfor getResult(GameRoom room){
     // 找出票数最多的人，假如有重票数，则返回让他们重新投票

     //不然就找出票数最多的把他设置为阵亡
     int max = 0;
     GameUserInfor userOut = null;
     for(GameUserInfor user: room.getUsers())
     {
         if(user.getPoll() > max && user.isStatus())
         {
             max = user.getPoll(); //获取用户被投票数
             userOut = user; //标记票数最多的人,指针
         }
     }
     //检验最大票数是否有重票
     boolean rePoll = false;
     for(GameUserInfor user: room.getUsers())
     {
         if(user.isStatus()) {
             if (user.getPoll() == max && user != userOut) {
                 rePoll = true;
             }
         }
     }
     if(rePoll)
     {
         return null; //重新投票
     }else {

         //投票结束，判断user身份
         if(userOut.isTheSpy())
         {
          int num =   room.getTheSpyNum();//获取卧底当前存活人数
          int liveNum = room.getLiveNum(); //获取总存活人数
          room.setLiveNum(--liveNum);
          room.setTheSpyNum(--num); //卧底人数减一
         }else {
             int num =   room.getThePoorNum();//获取平民当前存活人数
             int liveNum = room.getLiveNum();
             room.setLiveNum(--liveNum);
             room.setThePoorNum(--num); //平民人数减一
         }
         userOut.setStatus(false); //设置身份为阵亡
         room.setPoll(false);//设置投票结束
         //重置本局相关参数
          GetGameResultUtil.resetUserSpeakStatus(room);
         //设置新发言人，避免最后一个发言人被投出后的bug
          GameTranslateTurnUtil.translate(room, userOut);
         System.out.println("本轮判决："+userOut.getWxName()+",他的身份为卧底吗："+userOut.isTheSpy());
         return userOut; //完成本轮投票,返回出局玩家信息
     }
 }
/**
 *
 * 计算票数
 *
 * */
 public static void countPoll(GameRoom room){
     int num = 0; //累计票数
     int pollWho = 0; //投给谁
     for(GameUserInfor user: room.getUsers())
     {
         pollWho = user.getPollWho();
         //玩家没有阵亡才能投票
        if(user.isStatus())
         for(GameUserInfor usr_se:room.getUsers())
         {
             //先看是不是本人，再比对，再相加
             if(user != usr_se && usr_se.getGameId() == pollWho  )
             {
                 num =usr_se.getPoll();//被投票数
                 usr_se.setPoll(++num);
             }
         }

     }
 }
 //重置说话状态 为没有说话
 public static int resetUserSpeakStatus(GameRoom room){
     room.setThisRurnAt(0);//当前轮次重置
     room.setPolledNum(0); //以投票人数重置为零
     for(GameUserInfor user: room.getUsers())
     {
         //没有阵亡
         if(user.isStatus())
         {
             //重置说话权限
             user.setThisTurnIsSpoken(false);
             //重置票数
             user.setPoll(0);
             //重置投票对象
             user.setPollWho(-1);
             //重置为没投过票
             user.setNotPoll(true);
         }
     }
     return 0;
 }
    public static int resetUserPollStatus(GameRoom room){
        room.setThisRurnAt(0);//当前轮次重置
        room.setPolledNum(0); //以投票人数重置为零
        for(GameUserInfor user: room.getUsers())
        {
            //没有阵亡
            if(user.isStatus())
            {
                //重置票数
                user.setPoll(0);
                //重置投票对象
                user.setPollWho(-1);
                //重置为没投过票
                user.setNotPoll(true);
                //重置为没有说过话
                user.setThisTurnIsSpoken(false);
            }
        }
        return 0;
    }
}
