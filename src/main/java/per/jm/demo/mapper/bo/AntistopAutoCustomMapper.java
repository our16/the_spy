package per.jm.demo.mapper.bo;

import per.jm.demo.pojo.autp.AntistopAuto;

import java.util.List;

public interface AntistopAutoCustomMapper {

    List<AntistopAuto> selectByExample(Integer page, Integer size);

}