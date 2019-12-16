package com.junyoung.book.springboot.config.auth;

import com.junyoung.book.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

//HandlerMethodArgumentResolver이란? 컨트롤러에 특정 조건에 맞는 파라미터가 있을 경우 원하는 값을 바인딩 해주는 인터페이스 입니다.
// 클라이언트의 요청이 담긴 파라미터를 컨트롤러보다 먼저 받아서 작업을 수행합니다.
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    // 파라미터가 해당 리졸버에 의해 수행될 수 있는 타입인지 true/ false로 리턴
    // 현재 파라미터를 resolver이 지원하는지에 대한 boolean을 리턴
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean isLoginUserAnnotation =  parameter.getParameterAnnotation(LoginUser.class) != null;

        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }
    // supportsParameter이 true일 시에 수행됩니다. 공통 작업 수행 후 리턴
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 실제로 바인딩 할 객체 리턴
        return httpSession.getAttribute("user");
    }
}
