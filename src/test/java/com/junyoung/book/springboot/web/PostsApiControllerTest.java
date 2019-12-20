package com.junyoung.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junyoung.book.springboot.domain.posts.Posts;
import com.junyoung.book.springboot.domain.posts.PostsRepository;
import com.junyoung.book.springboot.web.dto.PostsSaveRequestDto;
import com.junyoung.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //@Api 컨트롤러 테스트하는데 @WebMvcTest를 사용하지 않았습니다. @WebMvc의 경우 JPA 기능이 작동하지 않음
public class PostsApiControllerTest {

    // WAS를 구동시킬 때 사용하는 임의의 포트 번호를 부여합니다.
    @LocalServerPort
    private int port;

    // jpa 기능까지 한번에 테스트할 때 사용합니다.
    // 직접 WAS를 구동시키고 REST API를 테스트할 때 유용한 객체이지만, 실제 서버를 실행하기 때문에 테스트를 진행하는데 비용이 커서 차라리 MockMvc 객체를 사용하는게 나을 때도 있습니다.
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    // 인증 된 모의 사용자를 만들어서 사용합니다.
    // roles에 권한을 추가할 수 있습니다.
    @Test
    @WithMockUser(roles = "USER")
    public void Posts_등록된다() throws Exception{

        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }


    @Test
    @WithMockUser(roles = "USER")
    public void Posts_수정된다() throws Exception{

        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updatedId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updatedId;


        // PUT 방식으로 HTTP 요청을 할 때는 HttpEntity를 함께 보내는것을 주의해야합니다. 사용자가 직접 전달하는 것을 정의하여 보냅니다.
        //HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
        // Htpp Header를 수정할 수 있고 결과를 Http ResponseEntity로 받습니다.
        // RestTemplate에서 DELETE, PUT에 관한 요청은 반환값을 가지지 않을 뿐만 아니라 파라미터 전송도 없습니다. 컨트롤러에서 반환값을 갖기 위해 exchange()를 사용합니다.
        //ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //when
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());




        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }



}
