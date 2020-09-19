package per.jm.demo.controller.GameController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import per.jm.demo.service.AddGameAntistop;
import per.jm.demo.service.GameService;
import per.jm.demo.util.GetQRCode;
import per.jm.demo.util.ResponseUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/spy/test")
public class GameController {

    @Autowired
    GameService service;

    @Autowired
    AddGameAntistop addGameAntistop;
    @RequestMapping(value="/careate")
    @ResponseBody
    public Map<String,Object> createGame(int size,String openid,String fisrt,String second){
        int theSpy = 1;
        if(size <= 5)
        {
            theSpy = 1;
        }
        else if(size >= 6 && size <= 10)
            theSpy = 2;
        else
            theSpy = 3;
        if(size > 15)
            return ResponseUtil.getFail("人太多了");
        Map<String,String> userInfor = null;
        try {
            userInfor = GetQRCode.getUserInfor(openid);
        } catch (IOException e) {
            System.out.println(e.getCause());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        addGameAntistop.addNewAntistop(fisrt,second,openid);
        List<String> re = null;
        try {
            re = service.createGameRoom(size,openid,userInfor.get("wxName"),theSpy,fisrt,second);
        } catch (IOException e) {
            System.out.println("生成二维码错误");
        }
        Map<String,Object> map = new HashMap<>();
        if(re == null)
        {
           return ResponseUtil.getFail("失败");
        }else
        {
            return ResponseUtil.getSuccess(re,"成功");
        }
    }

    @RequestMapping(value = "/joinGame")
    @ResponseBody
    public Map<String,Object> joinGame(String openId,String roomId,String name){
        int re = service.joinGame(openId,name,roomId);
        if(re == 1)
        {
            return ResponseUtil.getSuccess(null,"成功");
        }
        else if(re == -1)
            return ResponseUtil.getFail("房间id 错误");
        return ResponseUtil.getFail("shiabi");
    }

    @RequestMapping(value="/satrtGame")
    @ResponseBody
    public Map<String,Object> startGame(String roomId, String openid){
         service.startGame(roomId,openid);
            return ResponseUtil.getSuccess(null,"成功");
    }
    @RequestMapping(value="/poll")
    @ResponseBody
    public Map<String,Object> poll(String fromId, int toId,String roomId){
        int re = service.poll(fromId,toId,roomId);
        if(re == 1)
            return ResponseUtil.getSuccess(null,"成功");
        else if(re == 0)
            return ResponseUtil.getFail("当前房间没有这个id");
        return ResponseUtil.getFail("失败");
    }

    @RequestMapping(value="/speak")
    @ResponseBody
    public Map<String,Object> speak (String fromId, String roomId,String content){
        int re = service.speak(fromId,roomId,content);
        if(re == 1)
            return ResponseUtil.getSuccess(null,"成功");
        return ResponseUtil.getFail("失败");
    }

    @RequestMapping(value = "/getAntistop")
    @ResponseBody
    public Map<String,Object> getAntis(String openId,Integer page){
        return  ResponseUtil.getSuccess(addGameAntistop.getAntistop(openId,page),"成功获取");
    }
}
