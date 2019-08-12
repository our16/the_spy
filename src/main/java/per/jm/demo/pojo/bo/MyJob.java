package per.jm.demo.pojo.bo;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import per.jm.demo.util.GameRoomFactory;
import per.jm.demo.util.GameRoomIdForPlayer;

import java.util.List;
import java.util.Map;

public class MyJob implements Job{

    //@Scheduled(cron = "0/2 * * * * ? ") //使用xml配置不需要，使用注解需要
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时器开始工作：清除超时的房间");
        //获取当前指针位置
        QueesList queesList = RingQuees.getRooms().get("timer");
        System.out.println("当前节点:"+queesList.getNum());
        /**
         *
         * 判断当前指针的下一指针room集合是否为空，不为空则清空（删除超时房间（无动作超时））*/
        QueesList queesListNext = queesList;
        System.out.println("进行操作的节点："+queesListNext.getNum());
        if(queesListNext.getGameRooms().size() > 0)
        {
            //获取这个节点所用房间信息,用来"清空"这个区域的信息
           Map<String,GameRoom> gameRooms = queesListNext.getGameRooms();
            if(gameRooms.size() > 0) {
                for (GameRoom gameRoom : gameRooms.values()) {
                    //获取该房间用户信息
                    List<GameUserInfor> userInfors = gameRoom.getUsers();
                    if (userInfors != null && userInfors.size() > 0) {
                        for (GameUserInfor gameUserInfor : userInfors) {
                            try {
                                //根据用户openId 删除用户加入的房间信息
                                GameRoomIdForPlayer.getRoomId().remove(gameUserInfor.getOpenId());
                                System.out.println("删除用户加入房间信息");
                            } catch (Exception e) {
                                System.out.println("该用户没有加入房间");
                            }
                        }
                    }
                    //删除这个房间
                    try {
                        GameRoomFactory.getRooms().remove(gameRoom.getRoomId());
                        System.out.println("删除房间:"+gameRoom.getRoomId()+",删除该房间的玩家："+gameRoom.getUsers());
                    } catch (Exception e) {
                        System.out.println("删除房间失败");
                    }
                }
                //清除在队列中的信息
                gameRooms.clear();
            }

        }else
        {
            System.out.println("本次没有超时的房间,本次节点位置"+queesListNext.getNum());
        }

        /**
         * 前面的不一定会执行
         * 设置新的指针指向
         * */
        RingQuees.getRooms().put("timer",queesListNext.getNext());
        // 一直循环不判断是否结束那些
    }
}
