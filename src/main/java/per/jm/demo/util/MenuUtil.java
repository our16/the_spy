package per.jm.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.text.ParseException;
import java.util.Map;

/*
import com.weixin.menu.Button;
import com.weixin.menu.ClickButton;
import com.weixin.menu.Menu;
import com.weixin.menu.ViewButton;
*/

public class MenuUtil {
    private static final String APPID = "wxd81ea542b764f58f";
    private static final String APPSECRET = "87525592ee6fbcc42d46ea5807c60fed";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static JSONObject doGetStr(String url) throws ParseException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);//第三方服务器通过接口与微信服务器交互
        JSONObject jsonObject = null;
        HttpResponse response = httpClient.execute(httpGet);//接收微信服务器返回的消息
        HttpEntity entity = response.getEntity(); //获取微信返回的信息
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");//把获取的信息转格式。这里，微信发来的信息是json格式的。
            jsonObject = JSONObject.parseObject(result);
        }
        return jsonObject;
    }

    /**
     * post请求
     *
     * @param url
     * @param outStr
     * @return
     */
    public static JSONObject doPostStr(String url, String outStr) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
        try {
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 通过判断获取AccessToken：本地-网络
     *
     * @return
     * @throws IOException
     */
    public static String getAccessToken() throws IOException, ParseException {
        File filePathDirector = new File("");
        String filePath = "\\www\\server\\token.txt";
        System.out.println(filePath);
        File file = new File(filePath);
        if(!file.exists())
        {
            System.out.println("路径不存在创建路径");
            file.mkdirs();
        }
        String access_token = "";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        String read = "";
        long creatTime = 0;
        try {
            byte[] bytes = new byte[10 * 1024];
            int len = bis.read(bytes, 0, bytes.length);
           // System.out.println(len);
            if(len > -1) {
                read = new String(bytes, 0, len);
                try {
                    creatTime = Long.valueOf(read.split("&jm&&")[1]).longValue();
                   // System.out.println(creatTime+"：时间:"+System.currentTimeMillis());
                } catch (NumberFormatException e) {
                   read = "";
                }
            }
            else
            {
                read = "";

            }
        } catch (IOException e) {
            System.out.println("出问题了");
           read = "";
        } finally {
            bis.close();
        }
        if (read.equals("")) {
            System.out.println("new");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath, false));
            access_token = "";
            access_token = getAccessTokenHttp();
            String str = access_token + "&jm&&" + System.currentTimeMillis();
            bos.write(str.toString().getBytes());
            str = "";
            bos.flush();
            bos.close();

        } else if ((System.currentTimeMillis() - creatTime) > 7199000) {
            System.out.println("update");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath, false));
            access_token = "";
            access_token = getAccessTokenHttp();
            String str = access_token + "&jm&&" + System.currentTimeMillis();
            System.out.println(str);
            bos.write(str.toString().getBytes());
            str = "";
            bos.flush();
            bos.close();

        } else {
            access_token = read.split("&jm&&")[0];
            System.out.println("show");
        }
        return access_token;
    }

    /**
     * 获取微信的AccessToken
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static String getAccessTokenHttp() throws ParseException, IOException {
        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        Map<String, String> maps = null;
        if (jsonObject != null) {
            maps = (Map<String, String>) JSON.parse(jsonObject.toString());
        }
        String access_token = maps.get("access_token");
        return access_token;
    }

    /**
     * 初始化菜单
     *
     * @return
     */
    public static String initMenu() {
       String menu = "";
        JSONArray button = new JSONArray();
        JSONObject jo_button = new JSONObject();
        JSONArray sub_button = new JSONArray();
        JSONObject child1 = new JSONObject();
        JSONObject child2 = new JSONObject();
        JSONObject child3 = new JSONObject();
        JSONObject child4 = new JSONObject();
        JSONObject child5 = new JSONObject();
        JSONObject view1 = new JSONObject();
        //*******************************
        view1.put("type","view");
        view1.put("name","在线创建房间");
        view1.put("url",new WxUtil().getRedict_url("http://duifenyi.our16.op/spy"));
        //*******************************
        child1.put("type","click");
        child1.put("name","管理房间");
        child1.put("key","managerRoom");
        //*****************************
        child2.put("type","click");
        child2.put("name","创建房间");
        child2.put("key","createRoom");
        //*******************************
        child3.put("type","click");
        child3.put("name","开始游戏");
        child3.put("key","startGame");
        //*******************************
        child4.put("type","click");
        child4.put("name","结束游戏");
        child4.put("key","gameOver");
        //*********************************
        child5.put("type","click");
        child5.put("name","退出当前房间");
        child5.put("key","outGameRoom");
        //*******************************
        sub_button.add(child1);
        sub_button.add(child2);
        sub_button.add(child3);
        sub_button.add(child4);
        sub_button.add(child5);
        //*******************************
        jo_button.put("name","谁是卧底");
        jo_button.put("sub_button",sub_button);
        //*******************************
        button.add(jo_button);
        button.add(view1);
        JSONObject result = new JSONObject();
        result.put("button",button);
        menu = result.toString();
        System.out.println(menu);
      /* menu = "{\n" +
               "     \"button\":[\n" +
               "     { \n" +
               "          \"type\":\"click\",\n" +
               "          \"name\":\"签到记录\",\n" +
               "          \"key\":\"click1\"\n" +
               "      },\n" +
               "      {\n" +
               "           \"name\":\"菜单\",\n" +
               "           \"sub_button\":[\n" +
                            "{ \"type\":\"click\",\n" +
               "            \"name\":\"注册\",\n" +
               "            \"key\":\"register\"},"+
               "           {   \n" +
               "               \"type\":\"view\",\n" +
               "               \"name\":\"我的博客\",\n" +
               "               \"url\":\"http://jm.our16.top/MyBlog/\"\n" +
               "            },\n" +
               "{" +
               "\"type\":\"click\"" +
               ",\"name\":\"创建房间\"," +
               "\"key\":\"creatRoom\"" +
               "},"+
               "{" +
               "\"type\":\"click\"" +
               ",\"name\":\"开始游戏\"," +
               "\"key\":\"startGame\"" +
               "},"+
               "            {\n" +
               "               \"type\":\"location_select\",\n" +
               "               \"name\":\"发送位置\",\n" +
               "               \"key\":\"location_select\"\n" +
               "            }]\n" +
               "       },\n" +
               "        {" +
               "          \"type\":\"view\",\n" +
               "          \"name\":\"进入对分易\",\n" +
               "          \"url\":\""+new WxUtil().getRedict_url("http://pmq737.natappfree.cc/duifenyi")+"\"\n}" +
               "]}";*/

        return menu;
    }

    public static int creatMenu(String token, String menu) {
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = doPostStr(url, menu);
        if (jsonObject != null) {
            result = jsonObject.getInteger("errcode");
        }
        return result;
    }
}