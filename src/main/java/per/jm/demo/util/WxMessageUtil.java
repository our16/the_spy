package per.jm.demo.util;




import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;
import per.jm.demo.pojo.bo.TemplateData;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WxMessageUtil {
    /**
     * 发送微信消息(模板消息)
     * @param touser 用户 OpenID
     * @param templatId 模板消息ID
     * @param clickurl URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）。
     * @param topcolor 标题颜色
     * @param data 详细内容
     * @return
     */

    public static String sendWechatMsgToUser(String touser, String templatId, String clickurl, String topcolor, JSONObject data) {
        String tmpurl = null;
        try {
            tmpurl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ MenuUtil.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("touser", touser);
        json.put("template_id", templatId);
        json.put("url", clickurl);
        json.put("topcolor", topcolor);
        json.put("data", data);
        try{
            JSONObject resultJson = MenuUtil.doPostStr(tmpurl,json.toString());
          //  JSONObject resultJson = new JSONObject(result);
            System.out.println("发送微信消息返回信息：" + resultJson.get("errmsg"));
            String errmsg = (String) resultJson.get("errmsg");
            if(!"ok".equals(errmsg)){  //如果为errmsg为ok，则代表发送成功，公众号推送信息给用户了。
                return "error";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }finally {
            if(templatId!=null) {
                //删除新增的 微信模板
                //deleteWXTemplateMsgById(templatId);
            }
        }
        return "success";
    }

    public static String getWXTemplateMsgId(String templateSerialNumber){
        String tmpurl = null;
        try {
            tmpurl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ MenuUtil.getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("template_id_short", templateSerialNumber);
        JSONObject resultJson = MenuUtil.doPostStr(tmpurl ,json.toString());
        String errmsg = (String) resultJson.get("errmsg");
        System.out.println("获取模板编号返回信息：" + errmsg);
        if(!"ok".equals(errmsg)){
            return "error";
        }
        String templateId = (String) resultJson.get("template_id");
        return templateId;
    }

    public static void sendMessage(String openIdIs,String modleType,Map<String,String> map,String clickUrl){
        String openId = "****************************";
        Map<String, TemplateData> param = new HashMap<>();
        param.put("first",new TemplateData(map.get("first"),"#696969"));
        param.put("keyword1",new TemplateData(map.get("keyword1"),"#696969"));
        param.put("keyword2",new TemplateData(map.get("keyword2"),"#696969"));
        param.put("remark",new TemplateData(map.get("remark"),"#696969"));
        //注册的微信-模板Id
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(param));
        //谁是卧底消息模板id;
        String modelId = "IM8wWvAiPH4yiv2oXbfOaVn0icL66ajEfUh67wnS-Hk";
        if(modleType.equals(""))
            modleType = modelId;
        //调用发送微信消息给用户的接口
        WxMessageUtil.sendWechatMsgToUser(openIdIs,modleType, clickUrl, "#000000", jsonObject);
    }

    @Test
    public void sets(){
        Map<String,String> map = new HashMap<>();
        map.put("first","消息提示");
        map.put("keyword1","游戏开始");
        map.put("keyword2","当前人数。。。");
        map.put("remark",new Date()+"");
        this.sendMessage("","",map,"");
    }
    private enum WxType{
       // TempType1("pxHZgdmzk77o52ItcVv-ua79KGMnoSGvH8Deh2bEHec");

    }

    public static void startGame(GameRoom gameRoom){
        if(gameRoom.getUsers() != null) System.out.println("房间id:"+gameRoom.getRoomId());
            for(GameUserInfor user : gameRoom.getUsers())
            {
                Map<String,String> map = new HashMap<>();
                map.put("fisrt","游戏开始啦");
                if(user.isTheSpy())
                {
                    map.put("keyword1","你的关键词为："+gameRoom.getFirst());
                }else
                {
                    map.put("keyword1","你的关键词为："+gameRoom.getSecond());
                }
                map.put("remark",new Date()+"");
                //调用微信接口
                WxMessageUtil.sendMessage(user.getOpenId(),"",map,"");
            }
    }

    public static void translateSpeakContent(GameRoom gameRoom,GameUserInfor userInfor,Map<String,String> map1){
        String toUserOpenId = null;
        String fromUserOpenId = null;
        String content = null;
       /* Map<String,String> map1 = new HashMap<>();
        map1.put("first","玩家发言");
        map1.put("keyword1","昵称为："+userInfor.getWxName());
        map1.put("keyword2","说："+userInfor.getContent());
        map1.put("remark",new Date()+"");*/
        if(gameRoom.getUsers() != null)
        {
            for(GameUserInfor user : gameRoom.getUsers())
            {
                if(user != userInfor && user.isReciveInfor() != false)
                {

                    if(userInfor != null)
                    {
                        content = userInfor.getContent();
                    }else
                    {
                       continue;
                    }
                    toUserOpenId = user.getOpenId();
                     fromUserOpenId = userInfor.getOpenId();
                    if(toUserOpenId != null && fromUserOpenId != null && content != null)
                    {
                        //推送消息
                        System.out.println("消息推送："+userInfor.getWxName()+"说:"+userInfor.getContent()+"发送给："+user.getWxName());
                            //调用微信消息接口
                            sendMessage(user.getOpenId(),"",map1,"");
                    }
                }
            }
            userInfor.setContent(null);
        }

    }

    public static void sendToAllPlayer(GameRoom gameRoom, String name, Map<String,String> map){

       for(GameUserInfor userInfor: gameRoom.getUsers())
       {
           //调用微信消息接口
          sendMessage(userInfor.getOpenId(),"",map,"");
       }
    }
}
