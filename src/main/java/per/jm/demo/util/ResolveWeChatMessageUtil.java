package per.jm.demo.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import per.jm.demo.pojo.bo.GameRoom;
import per.jm.demo.pojo.bo.GameUserInfor;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolveWeChatMessageUtil {
    public final static String resp_message_type_text = "text"; //文本
    public final static String resp_message_type_event = "event"; // 事件
    public final static String resp_message_type_event_view = "VIEW"; // 跳转网页
    public final static String resp_message_type_event_thespay = "thespy"; // 谁是卧底
    public final static String resp_message_type_location = "location_select"; // 发送地址
    public final static String resp_message_type_event_click = "CLICK";
    public final static String resp_message_type_token = "jm12315";
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws  Exception {
        Map<String, String> map = new HashMap<String,String>();
        InputStream inputStream = request.getInputStream();
        // 解析微信信息
        SAXReader reader = new SAXReader();
        // 获取元素
        Document read = reader.read(inputStream);
        Element rootElement = read.getRootElement();
        List<Element> elements = rootElement.elements();
        System.out.println("获取到微信的消息：");
        System.out.println("*******************************");
        for (Element element : elements) {
            // key value 固定的
            System.out.println(element.getName()+":"+ element.getText());
            map.put(element.getName(), element.getText());
        }
        System.out.println("*******************************");
        inputStream.close();
        //查看是否已经有微信用户信息,避免过度请求
        GameRoom room = GameRoomFactory.getRooms().get(GameRoomIdForPlayer.getRoomId().get(map.get("FromUserName")));

        GameUserInfor userInfor1 = null;
        if(room != null)
        for(GameUserInfor user: room.getUsers())
        {
            if(user.getOpenId().equals(map.get("FromUserName")) && user.getWxName() != null && !user.getWxName().equals(""));
            {
               userInfor1 = user;
                break;
            }
        }
        if(userInfor1 == null) {
            Map<String, String> userInfor = GetQRCode.getUserInfor(map.get("FromUserName"));
            if (userInfor == null) {
                return null;
            }
            map.put("wxName", userInfor.get("wxName"));
            System.out.println("新获取一条用户信息");
        }else {
            map.put("wxName", userInfor1.getWxName());
            System.out.println("已经有这个用户信息了");
        }
        return map;
    }
    public static  String getXmlText(Map<String, String> map,String text){
        String  resXmlStr =
                "<xml>" +
                "<ToUserName><![CDATA[" + map.get("FromUserName") + "]]></ToUserName>" +//此处要填写 发送方帐号（一个OpenID）
                "<FromUserName><![CDATA[" + map.get("ToUserName") + "]]></FromUserName>" +//此处填写开发者微信号
                "<CreateTime>" + System.currentTimeMillis() / 1000 + "</CreateTime>" +
                "<MsgType><![CDATA[" + "text" + "]]></MsgType>" +
                "<Content><![CDATA[" + text + "]]></Content>" +
                "</xml>";
        return resXmlStr;
    }
}
