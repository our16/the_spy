package per.jm.demo.mapper.auto;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import per.jm.demo.pojo.autp.AntistopAuto;
import per.jm.demo.pojo.autp.AntistopAutoExample;

public interface AntistopAutoMapper {
    long countByExample(AntistopAutoExample example);

    int deleteByExample(AntistopAutoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AntistopAuto record);

    int insertSelective(AntistopAuto record);

    List<AntistopAuto> selectByExample(AntistopAutoExample example);

    AntistopAuto selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AntistopAuto record, @Param("example") AntistopAutoExample example);

    int updateByExample(@Param("record") AntistopAuto record, @Param("example") AntistopAutoExample example);

    int updateByPrimaryKeySelective(AntistopAuto record);

    int updateByPrimaryKey(AntistopAuto record);
}