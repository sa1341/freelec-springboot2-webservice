package com.junyoung.book.springboot;

import com.junyoung.book.springboot.config.auth.SecurityConfig;
import com.junyoung.book.springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// JUnit에 내장된 실행자 외에 다른 실행자를 실행시킵니다.
// 즉, 스프링 부트 테스트와 JUnit 사이에 연결고리 역할을 합니다.
@RunWith(SpringRunner.class)
// Spring MVC에 집중할 수 있는 어노테이션, 선언할 경우 WebSecurityConfigureAdapter, WebMvcConfigurer @Controller, @ControllerAdvice 등을 사용할 수 있습니다. 단, @Service, @Component, @Repository 등은 사용불가
// 일반적인 Configuration은 스캔하지 않습니다.
@WebMvcTest(controllers =  HelloController.class,
            excludeFilters =  {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
            }
)
public class HelloControllerTest {

    // 웹 API를 테스트할 때 사용합니다.
    // GET, POST 등에 대한 API 테스트 가능, 스프링 MVC 테스트의 시작점입니다.
    @Autowired
    private MockMvc mvc;

    @WithMockUser(roles="USER")
    @Test
    public void hello_리턴된다() throws Exception{

        //given
        String hello = "hello";

        //when
        // MockMvc를 통해 /hello 주소로 HTTP GET 요청을 합니다. 체이닝이 지원됩니다.
        mvc.perform(get("/hello"))
                .andExpect(status().isOk()) // mvc.perform의 결과를 검증하고, Status를 검증합니다. 여기선 OK 즉, 200이 아닌지를 검증
                .andExpect(content().string(hello)); // 응답 본문의 내용을 검증합니다. "hello"를 리턴하기 때문에 이 값이 맞는지 검증합니다.
    }

    @WithMockUser(roles="USER")
    @Test
    public void helloDto가_리턴된다() throws Exception{

        //given
        String name = "junyoung";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                        .param("name", name) // 값은 String 타입만 허용됩니다. 숫자/날짜 등의 데이터를 등록할 때 문자열로 변경해야만 가능합니다.
                        .param("amount", String.valueOf(amount))
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$.name", is(name))) // json 응답값을 필드별로 검증할 수 있는 메소드 $을 기준으로 필드명을 명시합니다.
         .andExpect(jsonPath("$.amount", is(amount)));


        //when

        //then

    }
}
