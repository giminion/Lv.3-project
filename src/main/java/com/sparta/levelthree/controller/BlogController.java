package com.sparta.levelthree.controller;

import com.sparta.levelthree.dto.BlogRequestDto;
import com.sparta.levelthree.dto.BlogResponseDto;
import com.sparta.levelthree.dto.MessageResponseDto;
import com.sparta.levelthree.jwt.JwtUtil;
import com.sparta.levelthree.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
//@Slf4j(topic = "Blog Controller") //로그 확인용
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    // 전체 게시글 목록 조회 API
    @GetMapping("/blog")
    public List<BlogResponseDto> getBlogs() {
        return blogService.getBlogs();
    }

    // 특정 게시물 조회
    @GetMapping("/blog/{id}")
    public BlogResponseDto getBlog(@PathVariable Long id){
        return blogService.getBlog(id);
    }

    // 게시글 작성 API
    @PostMapping("/blog")
    public BlogResponseDto createBlog(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue, @RequestBody BlogRequestDto requestDto) {
        return blogService.createBlog(tokenValue, requestDto);
    }

    // 게시물 수정 API
    @PutMapping("/blog/{id}")
    public BlogResponseDto updateBlog(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue, @PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
        return blogService.updateBlog(tokenValue, id, requestDto);
    }

    // 게시물 삭제 API
    @DeleteMapping("/blog/{id}")
    public ResponseEntity<MessageResponseDto> deleteBlog(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue, @PathVariable Long id) {
        return blogService.deleteBlog(tokenValue, id);
    }
}