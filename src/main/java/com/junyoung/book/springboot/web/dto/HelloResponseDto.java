package com.junyoung.book.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HelloResponseDto {

    // cmd + shift + t 테스트 코드를 만드는 단축키입니다.
    private final String name;
    private final int amount;


}
