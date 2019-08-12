package per.jm.demo.mapper.bo;

import per.jm.demo.pojo.autp.AntistopUserSet;

import java.util.List;

public interface AntistopUserSetCustomMapper {
    List<AntistopUserSet> selectByExample(Integer page, Integer size);
}