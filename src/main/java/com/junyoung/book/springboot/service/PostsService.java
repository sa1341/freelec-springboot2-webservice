package com.junyoung.book.springboot.service;

import com.junyoung.book.springboot.domain.posts.Posts;
import com.junyoung.book.springboot.domain.posts.PostsRepository;
import com.junyoung.book.springboot.web.dto.PostsResponseDto;
import com.junyoung.book.springboot.web.dto.PostsSaveRequestDto;
import com.junyoung.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public List<PostsResponseDto> findAllDesc(){

        return postsRepository.findAllDesc().stream().map(PostsResponseDto::new).collect(Collectors.toList());
    }


    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {

        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {

        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
        postsRepository.delete(posts);
    }

}
