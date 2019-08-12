package per.jm.demo.service.impl;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;
import per.jm.demo.pojo.bo.RingQuees;
import per.jm.demo.service.AddGameAntistop;
import per.jm.demo.service.GameService;
import per.jm.demo.service.WeChatService;
import per.jm.demo.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static per.jm.demo.util.ResolveWeChatMessageUtil.getXmlText;


@Service
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    GameService gameService; //调用游戏接口，直接调用的服务层
    @Autowired
    AddGameAntistop addGameAntistop;
    private final String text = "1. 点击菜单开始谁是卧底游戏\n";

    public  String ProcessWeChatUserRequests(HttpServletRequest request,HttpServletResponse response) {
        String content = request.getQueryString();
        Map<String, String> map = null;
       /* 返回的内容*/
        String reback = "";
       /* 检查消息是否来自微信服务器*/
        if (content != null && content.startsWith("signature")) {
            try {
                /*用户信息*/
                map = ResolveWeChatMessageUtil.xmlToMap(request);
                if(map == null)
                {
                    return "获取用户信息失败";
                }
                String roomId = GameRoomIdForPlayer.getRoomId().get(map.get("FromUserName"));
                if(roomId != null){
                    RingQuees.setNewPot(roomId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (map.get("MsgType").equals(ResolveWeChatMessageUtil.resp_message_type_text)) {
                /*处理文本消息*/
                reback = resolveTextMessage(map);

            }
            else if (map.get("MsgType").equals(ResolveWeChatMessageUtil.resp_message_type_event)) {
                //处理事件消息
                reback = resolveEvent(map);
            }else
            {
                // 跳转到网页了
                return "网页信息";
            }
            if(reback == null)
                return "success";
            return getXmlText(map,reback);
        }else
        {
            System.out.println("不是微信消息");
            return null;
        }
    }
    private String getRoomUSers(Map<String,String> map){
        GameRoom gameRoom = null;
        gameRoom = GetGameRoomByMakerOpenId.getGameRoomByOpenIg(map.get("FromUserName"));
        if(gameRoom == null)
        {
            return null;
        }
        List<GameUserInfor> list = gameRoom.getUsers();
        String nowUser = "";
        int i = 1;

        for (GameUserInfor user:list)
        {
            nowUser += i +"、 id:"+user.getGameId()+ ",昵称:"+user.getWxName()+"\n";
            i++;
        }
        return nowUser;
    }
    /**
     * 处理文本消息
     *
     * */
    private String resolveTextMessage(Map<String,String> map){
        /**
         * 获取游戏梦工厂
         * **/

        GameRoom gameRoom;
         boolean changeAuto = false; //修改
         /*map 要用来获取用户openid 进行进一步操作*/
        gameRoom = GetGameRoomByMakerOpenId.getGameRoomByOpenIg(map.get("FromUserName"));
        if(gameRoom != null && gameRoom.getRoomMaker().equals(map.get("FromUserName"))&&!gameRoom.isStart()) {
            String content = map.get("Content");
            if (content.equals("改")) {
                gameRoom.setChangeWord(true);
                return "输入新的关键词";
                /**调用微信接口**/
            }
            if (content.equals("换")) {
                changeAuto = true;
            }
            if (content.equals("关闭")) {
                //关闭游戏房间
            }
            if (changeAuto) {
                System.out.println("自动切换关键词");
                int page = gameRoom.getAntiPo();
                JSONObject jo =addGameAntistop.getAntistop(map.get("FromUserName"),page);
                page = page + 1 ;
                /*jo.put("total",all2.size());
                jo.put("first",antistopAutoList.get(0).getFisrt());
                jo.put("second",antistopAutoList.get(0).getSecond());*/
                if(page > Integer.parseInt(jo.get("total").toString()))
                {
                    page = 1;
                }
                gameRoom.setAntiPo(page);
                gameRoom.setFirst(jo.get("first").toString());
                gameRoom.setSecond(jo.get("second").toString());
                return "切换完成：\n" +
                        "新的关键词为：\n" +
                        gameRoom.getFirst()+","+gameRoom.getSecond();
            }
            if (gameRoom.isChangeWord()) {
              //  System.out.println("修改关键词");
                gameRoom = GetGameRoomByMakerOpenId.getGameRoomByOpenIg(map.get("FromUserName"));
                if (gameRoom == null) {
                    return "你还没有创建房间";
                } else {
                    if(!gameRoom.isStart()) {
                        String first = "";
                        String second = "";
                        String[] contents = content.split(",");
                        if (contents.length == 2) {
                            first = contents[0];
                            second = contents[1];
                            gameRoom.setFirst(first);
                            gameRoom.setSecond(second);
                            gameRoom.setChangeWord(false);// 置为修改完成
                            return "修改成功";
                        } else {
                            String[] cont = content.split(" ");
                            if (cont.length == 2) {
                                first = cont[0];
                                second = cont[1];
                                gameRoom.setFirst(first);
                                gameRoom.setSecond(second);
                                gameRoom.setChangeWord(false);// 置为修改完成
                                return "修改成功";
                            }
                        }
                        return "请输入正确格式";
                    }
                }
            }
        }
        /**以上情况都不满足则查看游戏内情况，正式接入游戏接口**/
        /**加入房间**/
        String  roomId = "";
        String testContent = map.get("Content").toUpperCase(); //全部转换成大写

         roomId = getPM("\\b[0-9]{5}\\b",testContent);
        //加入游戏
        if(roomId != "" )
        {
            gameRoom = GameRoomFactory.getRooms().get(roomId); //这里用来修改某些信息
            if(gameRoom == null)
                return "没有这个房间";
            if(gameRoom.isStart())
                return "游戏已经开始了";
           else {
                String room =  GameRoomIdForPlayer.getRoomId().get(map.get("FromUserName"));
                if(room != null)
                {
                    return  "你已经在房间里面了";
                }
                //调用游戏加入接口
                int result = gameService.joinGame(map.get("FromUserName"), map.get("wxName"), roomId);
                if (result == 1) {
                    return "加入成功";
                }
                if (result == -1) {
                    return "游戏已经开始不能中途加入";
                }
                if (result == -4) return "你已经加入游戏了";
            }
        }
        //逐个描述关键词，此时其他玩家不能发言
        //有一个最简单的方法，用openId存入数据库

            gameRoom = GameRoomFactory.getRooms().get(GameRoomIdForPlayer.getRoomId().get(map.get("FromUserName")));
           if(gameRoom == null)
            return "房间信息获取失败";
           // System.out.println(gameRoom+"关键词阐述时间");
            if(gameRoom.isSpeak())
            {
                String userOpenId = gameRoom.getOpenId();
                if(userOpenId == null)
                    return "房间初始化失败";
               // System.out.println("当前应由："+gameRoom.getOpenId());
                //只转发当前该发言人的信息
                if(userOpenId.equals(map.get("FromUserName"))) {
                    //System.out.println("发言人说："+map.get("Content"));
                    gameService.speak(map.get("FromUserName"), GameRoomIdForPlayer.getRoomId().get(map.get("FromUserName")), map.get("Content"));
                    return null;
                }else
                {
                    return "现在还没轮到你发言，请等待...";
                }
            }
            //玩家自由发言
            if( gameRoom.isFreeSpeakTime())
            {
                gameService.speak(map.get("FromUserName"),gameRoom.getRoomId(),map.get("Content"));
            }
            //投票
            if(gameRoom.isPolling())
            {
                System.out.println("进行投票");
                int pollWho = 0;
                try {
                     pollWho = Integer.parseInt(map.get("Content"));
                } catch (NumberFormatException e) {
                    return "请输入正确的编号";
                }
                int re = gameService.poll(map.get("FromUserName"),pollWho,gameRoom.getRoomId());
                System.out.println("返回的状态码为:"+re);
                if(re == 1)
                {
                    return "投票成功";
                }
                 //不存在这个玩家
                if(re == -2)
                {
                    return "不存在这个玩家";
                }
                //不能投给自己
                if(re == -3)
                {
                    return "不能投给自己";
                }
                //还没有开始投票
                if(re == -4)
                {
                    return "还没有开始投票";
                }
            }
        return  text;
    }
    /**
     *
     * 处理事件
     *
     * */
    private  String resolveEvent(Map<String,String> map){
        /**
         * 视图事件
         * */
        if(map.get("Event").equals(ResolveWeChatMessageUtil.resp_message_type_event_view))
        {

            String openId = map.get("FromUserName");
            //System.out.println(openId);
            return getXmlText(map,openId);
        }
        /**
         * 扫描二维码事件
         * **/
        if(map.get("Event").equals("SCAN"))
        {
            //加入房间
            int result = gameService.joinGame(map.get("FromUserName"),map.get("wxName"),map.get("EventKey"));
            if(result == 1)
            {
                return  "加入成功";
            }
            if (result == -1)
            {
                return "没有这个房间";
            }
            if (result == -2)
            {
               return "游戏已经开始不能中途加入";
            }
            if(result == -4)
               return  "你已经加入游戏了";
        }
        if(map.get("Event").equals("subscribe"))
        {
            String roomIdIs = null;
            if(map.get("EventKey").contains("qrscene_")){
             roomIdIs = map.get("EventKey").split("qrscene_")[1];
             }
            System.out.println("获取到的房间信息为:"+roomIdIs);
            if(roomIdIs == null)
                return text; //第一次加入又不是进入房间的话返回欢迎词
            //加入房间
            int result = gameService.joinGame(map.get("FromUserName"),map.get("wxName"),roomIdIs);
            if(result == 1)
            {
                return  "加入成功";
            }
            if (result == -1)
            {
                return "没有这个房间";
            }
            if (result == -2)
            {
                return "游戏已经开始不能中途加入";
            }
        }
        /**
         * 点击事件
         * */
        if(map.get("Event").equals("CLICK")){
            if(map.get("EventKey") != null) {
                if (map.get("EventKey").equals("managerRoom")) {
                    String result = null;
                    result = getRoomUSers(map);
                    if (result == null) return  "你不是房主";
                    return result;
                }
                if (map.get("EventKey").equals("startGame")) {
                    int re = gameService.startGame(null, map.get("FromUserName"));
                    if (re == 1) return  null;
                    if(re == -1)
                        return "你好没有加入房间";
                    else if (re == -2) return  "不是房主";
                    else if (re == -3) return  "至少得有3个人才能开始游戏";
                    else if (re == -5) return "游戏已经开始了";
                    System.out.println("执行完了");
                }
                if (map.get("EventKey").equals("createRoom")) {
                    System.out.println("创建房间");
                    String room = GameRoomIdForPlayer.getRoomId().get(map.get("FromUserName"));
                    if (room != null) {
                      return  "你已经在房间里面了";
                    } else
                    {
                        Map<String,String> map1 = new HashMap<>();
                        map1.put("first","创建房间");
                        map1.put("remark",new Date()+"");
                        WxMessageUtil.sendMessage(map.get("FromUserName"),"",map1,"http://duifenyi.our16.top/spy?" + map.get("FromUserName"));
                        return  null;
                    }
                }
                //关闭房间
                if (map.get("EventKey").equals("gameOver")) {

                    int re = gameService.closeGameRoom(map.get("FromUserName"));
                    if (re == -1) return  "你还没有创建房间";
                    else if(re == 0) return "你不是房主";
                    else return  "关闭成功";
                }
                if(map.get("EventKey").equals("outGameRoom"))
                {
                    int re = gameService.outGameRoom(map.get("FromUserName"));
                    if(re==1) {
                        return "success";
                    }
                    else if(re == 0) {
                        return "你不在房间中";
                    }
                    else if (re == -2){
                        return "现在该你发言不能退出游戏";
                    }
                    else
                        return "你还没有加入房间";
                }
            }
        }
        return text;
    }
     /*获取正则验证结果*/
    private String getPM(String compile,String con){
        String string = "";
        Pattern pattern = Pattern.compile(compile);
        Matcher matcher = pattern.matcher(con);
        while (matcher.find()) {
            string = matcher.group();
        }
        return string;
    }
     /*微信验证接口*/
    public static  void VerifyWeChatInterface(HttpServletRequest request, HttpServletResponse response){
        /* 获取get 请求的参数 */
//        String content = request.getQueryString();
//        if (content != null && content.startsWith("signature")) {
//            String echostr = CheckSignature(content);
//           // System.out.println(echostr);
//            PrintWriter out2 = null;
//            try {
//                out2 = response.getWriter();
//                /*返回echostr给微信服务器*/
//                out2.print(echostr);
//            } catch (IOException e) {
//                System.out.println(e.getCause());
//            }finally {
//                out2.close();
//            }
//        }
    }
}
