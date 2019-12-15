package com.junyoung.book.springboot.web;

import com.junyoung.book.springboot.config.auth.LoginUser;
import com.junyoung.book.springboot.config.auth.dto.SessionUser;
import com.junyoung.book.springboot.service.PostsService;
import com.junyoung.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;


    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user){

        model.addAttribute("posts", postsService.findAllDesc());

        // 아래 코드처럼 세션값이 필요하면 그때마다 직접 세션에서 값을 가져와야하기 때문에  같은 코드가 반복되는 것은 불필요 합니다.
        /*SessionUser user = (SessionUser) httpSession.getAttributxxe("user");*/

        if(user != null){
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){

        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }


}
