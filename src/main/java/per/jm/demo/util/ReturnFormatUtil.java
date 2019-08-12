package per.jm.demo.util;

import com.alibaba.fastjson.JSONObject;

public class ReturnFormatUtil {

    public static JSONObject getJSON(int status,String msg,Object data){
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("status",status);
        jsonObject2.put("msg",msg);
        jsonObject2.put("data",data);
        return jsonObject2;
    }
}
