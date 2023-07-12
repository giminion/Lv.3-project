package com.sparta.levelthree.service;

import com.sparta.levelthree.dto.CommentRequestDto;
import com.sparta.levelthree.dto.CommentResponseDto;
import com.sparta.levelthree.dto.MessageResponseDto;
import com.sparta.levelthree.entity.Blog;
import com.sparta.levelthree.entity.Comment;
import com.sparta.levelthree.entity.User;
import com.sparta.levelthree.jwt.JwtUtil;
import com.sparta.levelthree.repository.CommentRepository;
import com.sparta.levelthree.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepositoy;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BlogService blogService;

    public  CommentResponseDto createComment(String tokenValue, CommentRequestDto requestDto, Long blogid) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog targetBlog = blogService.findBlog(blogid);

        String token = jwtUtil.substringToken(tokenValue);

        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        Claims info = jwtUtil.getUserInfoFromToken(token);

        String username = info.getSubject();

        User currentUser = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음"));

        // RequestDto -> Entity
        Comment comment = new Comment(requestDto,targetBlog, currentUser);

        Comment saveComment = commentRepositoy.save(comment);

        // Entity -> ResponseDto
        return new CommentResponseDto(saveComment);
    }

    @Transactional
    public CommentResponseDto updateComment(String tokenValue, Long commentid, CommentRequestDto requestDto) {
        //find Comment
        Comment comment = findComment(commentid);

        // Read User Token
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        Claims info = jwtUtil.getUserInfoFromToken(token);

        // find user to userRepository by Token in username 토큰으로 유저 이름 찾아오기
        User currentUser = userRepository.findByUsername(info.getSubject()).orElseThrow(() -> new IllegalArgumentException("Not Find User"));

        // user Auth check
        checkUsername(comment, currentUser);

        // update for comment
        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    public  ResponseEntity<MessageResponseDto> deleteComment(String tokenValue, Long commentid) {
        Comment comment = findComment(commentid);

        // Read User Token
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        Claims info = jwtUtil.getUserInfoFromToken(token);

        // find user to userRepository by Token in username 토큰으로 유저 이름 찾아오기 find user in repository by token
        User currentUser = userRepository.findByUsername(info.getSubject()).orElseThrow(() -> new IllegalArgumentException("Not Find User"));

        // user Auth check
        checkUsername(comment, currentUser);

        commentRepositoy.delete(comment);
        return new ResponseEntity<MessageResponseDto>(new MessageResponseDto("댓글 삭제 성공", "200"), HttpStatus.OK);
    }


    private  Comment findComment(Long id) {
        return commentRepositoy.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    public void checkUsername (Comment comment, User user){
        // admin 확인
        if (!user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            // 작성자 본인 확인
            if (!(comment.getUser().getId() == user.getId())) {
                throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
            }
        }
    }
}


