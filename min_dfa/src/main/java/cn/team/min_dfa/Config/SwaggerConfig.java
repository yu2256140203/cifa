package cn.team.min_dfa.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(Environment environment){

        Profiles of = Profiles.of("dev", "test");
        boolean flag = environment.acceptsProfiles(of);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("2084team-Yu")
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.team.min_dfa.Controller"))
                .build();
    }
    private ApiInfo apiInfo() {
        Contact contact = new Contact("2084team", "https://2084team.cn", "123456@qq.com");
        return new ApiInfo(
                "NFA_defined",
                "SysY的NFA确定化",
                "v1.0", // 版本
                "http://2084team.cn/",
                contact,
                "Apach 2.0 许可",
                "许可链接",
                new ArrayList<>()
        );
    }
}