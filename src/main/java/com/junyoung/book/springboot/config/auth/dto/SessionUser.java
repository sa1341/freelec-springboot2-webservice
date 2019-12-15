package com.junyoung.book.springboot.config.auth.dto;

import com.junyoung.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;


// 도메인 모델을 직접 세션에 저장하면 다른 엔티티와 연관관계를 가질 때 대상에 자식들이 포함되어 성능이슈, 부수 효과가 발생할 확률이 있어 Dto로 직렬화를 구현해서 세션에 저장하는게 좋습니다.
@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;


    public SessionUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
