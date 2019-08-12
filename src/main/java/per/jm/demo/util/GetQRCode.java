package per.jm.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class GetQRCode {
    public static String getQRCode(String roomId) throws IOException, ParseException {
        String accessToken = MenuUtil.getAccessToken();
        System.out.println(accessToken);
        String url = WxUtil.getTicket().replace("TOKEN",accessToken); //获取ticket
       /*String params = "{\n" + "   " +
               " \"expire_seconds\": 604800, \n" + " " +
               "   \"action_name\": \"QR_SCENE\", \n" +
               "    \"action_info\": {\n" +
               "        \"scene\": {\n" +
               "            \"scene_id\": 123\n" +
               "        }\n" +
               "    }\n" +
               "}";*/
        com.alibaba.fastjson.JSONObject jO =new com.alibaba.fastjson.JSONObject();
        jO.put("expire_seconds",604800);
        jO.put("action_name","QR_SCENE");
        JSONObject jo1 = new JSONObject();
        //二维码参数
        jo1.put("scene_id",roomId);
        JSONObject jo2 = new JSONObject();
        jo2.put("scene",jo1);
        jO.put("action_info",jo2);
        //System.out.println(jO);
        String re = HttpGetUtil.httpRequest(url,jO);
       // System.out.println(re);
        JSONObject jo = JSONObject.parseObject(re);
        String ticket = jo.get("ticket").toString();
        System.out.println(ticket);
        return WxUtil.getQR_url().replace("TICKET",ticket);//获取零时二维码
    }
    public static Map<String,String> getUserInfor(String openId) throws IOException, ParseException {
       final   String  url = " https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
        String access_token =  MenuUtil.getAccessToken();
       String result = HttpGetUtil.sendGet(url.replace("OPENID",openId).replace("ACCESS_TOKEN",access_token),null);
        System.out.println(result);
        JSONObject jo = JSONObject.parseObject(result);
        if(jo == null)
            return null;

        Map<String,String> map = new HashMap<>();
        if(jo.get("nickname") != null) {
            map.put("wxName", jo.get("nickname").toString());
        }
        else {
            map.put("wxName", "自动id" + System.currentTimeMillis() / 10000);
        }
        return map;
    }
    @Test
    public void thisIsATest() throws IOException, ParseException {
        //getQRCode();
        getUserInfor("ofTwW1pQ9gvaEMQCQ1P5xeN_RfpY");
    }
}
