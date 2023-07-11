package com.sparta.levelthree.service;

import com.sparta.levelthree.dto.LoginRequestDto;
import com.sparta.levelthree.dto.MessageResponseDto;
import com.sparta.levelthree.dto.SignUpRequestDto;
import com.sparta.levelthree.entity.User;
import com.sparta.levelthree.jwt.JwtUtil;
import com.sparta.levelthree.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 서비스
    public ResponseEntity<MessageResponseDto> createUser(SignUpRequestDto signUpRequestDto){
        String username = signUpRequestDto.getUsername();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponseDto("회원가입 되었습니다.", HttpStatus.OK.toString()));
    }

    //  로그인 서비스
    public ResponseEntity<MessageResponseDto> loginUser(LoginRequestDto requestDto, HttpServletResponse response){
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")       );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성
        String token = jwtUtil.createToken(user.getUsername());


        Cookie cookie = jwtUtil.tokenToCookie(token);

        if(cookie == null)
            throw new IllegalArgumentException("쿠키 생성 실패");

        response.addCookie(cookie);

        return ResponseEntity.ok(new MessageResponseDto("로그인했습니다.", HttpStatus.OK.toString()));
    }
}