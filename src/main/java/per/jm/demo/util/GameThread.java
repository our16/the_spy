package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameThread implements Runnable {
    private GameRoom gameRoom;
    private volatile boolean isOver ; //volatile 线程间可见
    private volatile boolean wait ;
    @Override
    public void run() {

        //推送给所有加入的用户游戏开始了
       WxMessageUtil.startGame(gameRoom);
        /***
         * 推送关键词等
         * */
       //正式开始游戏
        /***********游戏开始**********/
        while (!this.isOver)
        {
           if(!isOver)
           {
               //等待用户操作，微信接口调用这边。
               System.out.println("等待用户操作。。。。。");
               if(gameRoom.isSpeak()) {
                   Map<String, String> map1 = new HashMap<>();
                   map1.put("first", "裁判通知...");
                   for(GameUserInfor user :gameRoom.getUsers())
                   {
                       if(gameRoom.getOpenId().equals(user.getOpenId()))
                       {
                           map1.put("keyword1", "昵称为：" + user.getWxName() + "的玩家发言");
                           break;
                       }
                   }
                   map1.put("keyword2", "");
                   map1.put("remark", new Date() + "");
                   WxMessageUtil.sendToAllPlayer(gameRoom, gameRoom.getUserId() + "", map1);
               }
               System.out.println("当前关键词描述状态：" + gameRoom.isSpeak());
               System.out.println("当前自由谈论状态：" + gameRoom.isFreeSpeakTime());
               System.out.println("当前投票进行状态：" + gameRoom.isPolling());
               System.out.println("当前投票进度状态：" + gameRoom.isPoll());
               System.out.println("当前游戏状态,是否结算：" + gameRoom.isCountResult());
           }

            while(this.wait){
                //只是一个线程阻塞的作用,就像socket 的阻塞
            }
            System.out.println("//前一个用户操作完了又进入等待状态，这里判断是否达到结束要求");
            if(gameRoom.getThePoorNum() < 2 || gameRoom.getTheSpyNum() < 1)
            {
                //跳出所有循环
                System.out.println("人数达到结束要求。。。。");
                gameRoom.setCountResult(true);
            }else
            {
                   System.out.println("//判断是不是一轮结束");
                //活着的人数 与 轮到的人数指针比较，指到最后一个人者说明本轮结束
                    int at = gameRoom.getThisRurnAt();
                    gameRoom.setThisRurnAt(++at); //人数加一
                   this.wait = true;
                    System.out.println("set wait is true");
            }
            if(gameRoom.isSpeak())
            {
                System.out.println("观点描述");
                GameSpeakUtil.translareMessage(gameRoom); //阐述观点
                gameRoom.setFreeSpeakTime(false);
                this.wait = true;
                //转发一次过后轮到下一个用户转发
            }
            if(gameRoom.isPoll())
            {
                System.out.println("//统计投票");
                GetGameResultUtil.countPoll(gameRoom);

                /**
                 *   //得到投票结果
                 *
                 *   这里已经人数减一了
                 *   //设置玩家阵亡
                 * */
                GameUserInfor userInfor = GetGameResultUtil.getResult(gameRoom);
                Map<String,String> map = new HashMap<>();
                if(userInfor != null) {
                    System.out.println("获取游戏结果,"+"平民人数还有："+gameRoom.getThePoorNum()+",卧底人数还有："+gameRoom.getTheSpyNum()+"!");
                    if (gameRoom.getThePoorNum() < 2 || gameRoom.getTheSpyNum() < 1) {
                        System.out.println("游戏结束//跳出所有循环");
                        gameRoom.setCountResult(true);
                    } else {
                        System.out.println("//游戏继续，推送消息");
                        map.put("first",userInfor.getWxName()+"出局");
                        if(userInfor.isTheSpy())
                        {
                            map.put("keyword1", "他的身份为卧底，游戏继续。");
                        }else {
                            map.put("keyword1", "他的身份为平民，游戏继续。");
                        }
                        gameRoom.setSpeak(true);//设置为继续描述关键字
                        this.wait = true;
                    }
                }else
                {
                    gameRoom.setPolledNum(0);//重置
                    map.put("first","有重票，重新投票");
                    gameRoom.setPolledNum(0); //以投票人数重置为零
                    gameRoom.setThisRurnAt(0);
                    gameRoom.setPoll(false);
                    gameRoom.setPolling(true);
                    GetGameResultUtil.resetUserPollStatus(gameRoom);
                    this.wait = true;
                }
                if(map.size() > 0)
                WxMessageUtil.sendToAllPlayer(gameRoom,"",map);
            }
            if(gameRoom.isCountResult())
            {
                this.wait = false;
                this.isOver = true;
                gameRoom.setStart(false);
            }
        }
        /**********
         *
         *
         * *游戏结束
         *
         *
         * **********/
        Map<String,String> map = new HashMap<>();
        System.out.println("//游戏结束计算结果");
        String theSpy = "";
        for(GameUserInfor userInfor: gameRoom.getUsers())
        {
            if(userInfor.isTheSpy())
            {
                theSpy += userInfor.getWxName()+"\n";
            }
        }
        System.out.println(theSpy);
        map.put("keyword2","卧底名单:\n"+theSpy);
            if(gameRoom.getTheSpyNum() > 0)
            {
                //卧底获胜，并指出卧底是谁
                map.put("keyword1","卧底获胜");

            }else
            {
                map.put("keyword1","平民获胜");
            }
        map.put("first","游戏结束");
        System.out.println("//推送消息---游戏最终结果");
        for(GameUserInfor user : gameRoom.getUsers())
        {
            //调用微信接口
            WxMessageUtil.sendMessage(user.getOpenId(),"",map,"");
        }
        //重置房间相关参数
        //初始化完成开始游戏
    }
    public GameThread(GameRoom gameRoom){
        System.out.println("开始游戏设置了相关参数");
        this.gameRoom = gameRoom;
        this.wait = true;
        this.isOver = false;
    }

    public void setOver(boolean over) {

        this.isOver = over;
    }
    public void setWait(boolean wait){
     this.wait = false;
        System.out.println("setwait"+this.wait);
    }

}
