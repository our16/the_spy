package per.jm.demo.demo;


import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.service.impl.GameServiceImpl;
import per.jm.demo.util.MenuUtil;

import java.io.IOException;
import java.text.ParseException;

public class test {
    GameRoom gr = new GameRoom();
    @Test
    public void TTtest(){
        int size = 3;

        GameServiceImpl gameService = new GameServiceImpl();


    }
    @Test
    public void pollTest(){
        GameServiceImpl gameService = new GameServiceImpl();
        //gameService.poll("123",12,"BABBC");
       gameService.speak("1dsss","BADCC","你好");

    }

    public static void main(String[] args) throws IOException, ParseException {
        String menu = JSONObject.parseObject(MenuUtil.initMenu()).toString();
        int result = MenuUtil.creatMenu(MenuUtil.getAccessToken(), menu);
        if (result == 0) {
            System.out.println("创建菜单成功");
        }else {
            System.out.println("错误码："+result);
        }
    }
}
