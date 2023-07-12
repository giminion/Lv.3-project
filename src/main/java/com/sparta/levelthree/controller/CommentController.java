package com.sparta.levelthree.controller;

import com.sparta.levelthree.dto.CommentRequestDto;
import com.sparta.levelthree.dto.CommentResponseDto;
import com.sparta.levelthree.dto.MessageResponseDto;
import com.sparta.levelthree.jwt.JwtUtil;
import com.sparta.levelthree.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {this.commentService = commentService;}

    // 댓글 작성 API
    @PostMapping("/{blogid}")
    public CommentResponseDto createComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue, @RequestBody CommentRequestDto requestDto, @PathVariable Long blogid) {
        return commentService.createComment(tokenValue, requestDto, blogid);
    }

    // 댓글 수정 API
    @PutMapping("/{commentid}")
    public CommentResponseDto updateComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue, @PathVariable Long commentid, @RequestBody CommentRequestDto requestDto) {
        return commentService.updateComment(tokenValue, commentid, requestDto);
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentid}")
    public ResponseEntity<MessageResponseDto> deleteComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue, @PathVariable Long commentid) {
        return commentService.deleteComment(tokenValue, commentid);
    }
}
