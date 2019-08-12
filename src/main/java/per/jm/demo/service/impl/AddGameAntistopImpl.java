package per.jm.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.jm.demo.mapper.auto.AntistopUserSetMapper;
import per.jm.demo.mapper.bo.AntistopAutoCustomMapper;
import per.jm.demo.mapper.bo.AntistopUserSetCustomMapper;
import per.jm.demo.pojo.autp.AntistopAuto;
import per.jm.demo.pojo.autp.AntistopUserSet;
import per.jm.demo.service.AddGameAntistop;
import java.util.List;


@Service
public class AddGameAntistopImpl implements AddGameAntistop {

    @Autowired
    AntistopUserSetCustomMapper antistopUserSetCustomMapper;

    @Autowired
    AntistopAutoCustomMapper antistopAutoCustomMapper;

    @Autowired
    AntistopUserSetMapper antistopUserSetMapper;

    /**
     *
     * 先查看玩家自己有没有保存过，再添加数据库默认的
     * */
    @Override
    public int addNewAntistop(String fisrt, String second, String openid) {
        AntistopUserSet antistopUserSet = new AntistopUserSet();
        antistopUserSet.setFirst(fisrt);
        antistopUserSet.setSecond(second);
        antistopUserSet.setOpenid(openid);
        int result = antistopUserSetMapper.insert(antistopUserSet);
        if(result > 0)
            return 1;
        return 0;
    }

    @Override
    public int deleteAntistop(int id) {
        int result = antistopUserSetMapper.deleteByPrimaryKey(id);
        if(result > 0)
            return 1;
        return 0;
    }
    @Override
    public JSONObject getAntistop(String openId,int page) {

        List<AntistopUserSet> all  = antistopUserSetCustomMapper.selectByExample(0,10000);
        List<AntistopUserSet> antistopUserSetList = antistopUserSetCustomMapper.selectByExample(page,1);
        JSONObject jo = new JSONObject();
        if(antistopUserSetList.size() > 0) {

            jo.put("total",all.size());
            jo.put("first",antistopUserSetList.get(0).getFirst());
            jo.put("second",antistopUserSetList.get(0).getSecond());
        }else
        {
            page = page - all.size();
            if(page < 0)
                page = 0;
            List<AntistopAuto> all2 = antistopAutoCustomMapper.selectByExample(0,10000);
            if(page > all2.size() -1)
                page = all2.size() -1;
            List<AntistopAuto> antistopAutoList = antistopAutoCustomMapper.selectByExample(page,1);

            jo.put("total",all2.size());
            jo.put("first",antistopAutoList.get(0).getFisrt());
            jo.put("second",antistopAutoList.get(0).getSecond());
        }
        return jo;
    }
}
