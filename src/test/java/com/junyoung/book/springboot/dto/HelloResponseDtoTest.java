package com.junyoung.book.springboot.dto;


import com.junyoung.book.springboot.web.dto.HelloResponseDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트(){
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        //then
        // assertj라는 테스트 검증 라이브러리의 검증 메소드입니다.
        // 메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어서 사용 가능합니다.
        // JUnit의 assertThat()은 is()와 같이 Matchers 라이브러리가 추가로 필요하기 때문에 자동완성이 더 확실한 assertj의 assertThat을 쓰는게 더 좋습니다.
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);


    }

}
