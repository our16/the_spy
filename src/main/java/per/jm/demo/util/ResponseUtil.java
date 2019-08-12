package per.jm.demo.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Map<String,Object> getSuccess(Object data,String msg ){
        Map<String,Object> map = new HashMap<>();
        map.put("status",1);
        map.put("msg",msg);
        map.put("data",data);
        return map;
    }

    public static Map<String,Object> getFail(String msg ){
        Map<String,Object> map2 = new HashMap<>();
        map2.put("status",0);
        map2.put("msg",msg);
        map2.put("data",null);
        return map2;
    }
}
