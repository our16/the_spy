package per.jm.demo.util;

import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

public class RandomTheSpyUtil {
    /**
     *
     * 用来分配卧底的
     *
     * */
    public static GameRoom setTheSpy(GameRoom gameRoom){
        // 随机生成一个卧底或多个
        int theSpyNum = 0;
        if(gameRoom.getNowNum() <= 7)
            theSpyNum = 1;
        else if(gameRoom.getNowNum() <= 12)
            theSpyNum = 2;
        else
            theSpyNum = 3;
        int flag = GetNewRandomId.getRandomNum(theSpyNum);
        int i = 0;
        /**
         * 想一下怎么随机关键字
         *大 o 2
         * */
        System.out.println("卧底编号"+flag);
        do {
            for (GameUserInfor user : gameRoom.getUsers()) {
                System.out.println("user spy:"+user.isStatus()+",now the spy num:"+gameRoom.getThePoorNum());
                if (i == flag && !user.isTheSpy() && gameRoom.getTheSpyNum() < theSpyNum) {
                    user.setAntistop(gameRoom.getFirst());
                    int num = gameRoom.getTheSpyNum();
                    gameRoom.setTheSpyNum(num);
                    user.setTheSpy(true);
                    flag = GetNewRandomId.getRandomNum(theSpyNum+i);
                    i = 0;
                    System.out.println("设置了一个卧底****************************");
                } else {
                    user.setAntistop(gameRoom.getSecond());
                    user.setTheSpy(false);
                }
                ++i;
            }
        }while (gameRoom.getTheSpyNum() < theSpyNum);
        return gameRoom;
    }
}
