package per.jm.demo.service;

import com.alibaba.fastjson.JSONObject;


public interface AddGameAntistop {
    int addNewAntistop(String fisrt, String second, String openid);
    int deleteAntistop(int id);
   JSONObject getAntistop(String openId, int page);
}
