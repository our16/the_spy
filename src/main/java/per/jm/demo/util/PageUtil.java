package per.jm.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
public class PageUtil {
    public static JSONObject translate(PageInfo pageInfo){
        JSONObject data = new JSONObject();
        data.put("total",pageInfo.getTotal());
        data.put("page",pageInfo.getPageNum());
        data.put("list",pageInfo.getList());
        return data;
    }
}
