package com.sparta.levelthree.entity;

import com.sparta.levelthree.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA가 관리할 Entitu 클래스 저장
@Getter
@Table(name = "comment") //  매핑할 테이블명 지정
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id // 식별자
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 걸어주기
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false) // length의 디폴트는 255
    private String content;

    @ManyToOne
    private Blog blog;


    public Comment(CommentRequestDto requestDto, Blog blog, User user) {
        this.user = user;
        this.blog = blog;
        this.content = requestDto.getContent();
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

}
