package com.junyoung.book.springboot.web;


import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile(){

        // 현재 활성화 된 profile을 모두 가져옵니다. real, real-db, oauth 등이 활성화되어 있다면 모두 가져옵니다.
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");

        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);


        return profiles.stream()
                .filter(realProfiles :: contains)
                .findAny()
                .orElse(defaultProfile);
    }

}
