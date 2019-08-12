package per.jm.demo.demo;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.quartz.impl.jdbcjobstore.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class  Demo {
    public void generator() throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException, org.mybatis.generator.exception.InvalidConfigurationException {
        List<String> warnings = new ArrayList<String>();
        boolean overWrite = true;
        File configFile = new File("src/main/java/com/thespy/demo/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration configuration =  cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overWrite);
        MyBatisGenerator batisGenerator = new MyBatisGenerator(configuration,callback,warnings);
        batisGenerator.generate(null);
    }
    @Test
    public  void test() throws Exception{
        Demo demo = new Demo();
        demo.generator();
    }
}
