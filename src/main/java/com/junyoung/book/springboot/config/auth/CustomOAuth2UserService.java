package com.junyoung.book.springboot.config.auth;


import com.junyoung.book.springboot.config.auth.dto.OAuthAttributes;
import com.junyoung.book.springboot.config.auth.dto.SessionUser;
import com.junyoung.book.springboot.domain.user.User;
import com.junyoung.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

// 이 구현체는 클라이언트에게 허가된 access token을 사용하여  리소스 오너의 user 속성 값들을 얻는데 책임이 있는 클래스입니다.
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final UserRepository userRepository;
    private final HttpSession httpSession;


    // 소셜 로그인 성공 시 수행되는 메소드로 사용자의 정보를 가져오도록 구현, OAuth2UserRequest 객체에는 access token, client id, client secret 정보가 들어있음.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 진행중인 서비스를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //OAuth2 로그인 진행 시 키가 되는 필드 값을 의미합니다. primary key와 같은 의미 입니다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        String redirect_url = userRequest.getClientRegistration().getRedirectUriTemplate();


        System.out.println("registrationId : " + registrationId);
        System.out.println("userNameAttributeName : " + userNameAttributeName);
        System.out.println("redirect_url: " +redirect_url);
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());


        User user = saveOrUpdate(attributes);

        // httpSession 인스턴스는 WAS에서 생성하는 인스턴스로 getAttribute, setAttribute 메소드 호출 시점에 생성합니다.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new
                        SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {

        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes
                .getName(), attributes.getPicture())).orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
