package per.jm.demo.util;

public class WxUtil {
    private  static final  String Redict_url ="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URL&response_type=code&scope=snsapi_base&state=redict&connect_redirect=1#wechat_redirect";
    private static final String Ticket = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    private static  final String QR_url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
    private static final String APPID = "********";
    private static final String APPSECRET = "************";

    public static String getQR_url() {
        return QR_url;
    }

    public String getRedict_url(String url){
        String content = Redict_url.replace("APPID", APPID);
        return content.replace("REDIRECT_URL",url);
    }

    public static String getTicket() {
        return Ticket;
    }
}
