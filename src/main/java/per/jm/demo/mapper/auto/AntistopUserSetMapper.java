package per.jm.demo.mapper.auto;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import per.jm.demo.pojo.autp.AntistopUserSet;
import per.jm.demo.pojo.autp.AntistopUserSetExample;

public interface AntistopUserSetMapper {
    long countByExample(AntistopUserSetExample example);

    int deleteByExample(AntistopUserSetExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AntistopUserSet record);

    int insertSelective(AntistopUserSet record);

    List<AntistopUserSet> selectByExample(AntistopUserSetExample example);

    AntistopUserSet selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AntistopUserSet record, @Param("example") AntistopUserSetExample example);

    int updateByExample(@Param("record") AntistopUserSet record, @Param("example") AntistopUserSetExample example);

    int updateByPrimaryKeySelective(AntistopUserSet record);

    int updateByPrimaryKey(AntistopUserSet record);
}