package cn.matio.api.config;

import cn.matio.api.config.config1.YamlProperties;
import cn.matio.api.config.config2.YamlProperties2;
import cn.matio.api.config.config3.YamlProperties3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestYamlLoader {

    @Autowired
    private YamlProperties yamlProperties;

    @Autowired
    private YamlProperties2 yamlProperties2;

    @Autowired
    private YamlProperties3 yamlProperties3;

    @Test
    public void test() {
        System.out.println(yamlProperties.toString());
        System.out.println(yamlProperties2.toString());
        System.out.println(yamlProperties3.toString());
    }

}