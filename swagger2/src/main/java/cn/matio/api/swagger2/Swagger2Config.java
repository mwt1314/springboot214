package cn.matio.api.swagger2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author mawt
 * @description
 * @date 2019/12/27
 */
@Component
@ConditionalOnProperty(name = "swagger2.enable", havingValue = "true",  matchIfMissing = true)
public class Swagger2Config {



}
