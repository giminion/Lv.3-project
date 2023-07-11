package com.sparta.levelthree.entity;

import com.sparta.levelthree.dto.BlogRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA가 관리할 Entitu 클래스 저장
@Getter
@Table(name = "blog") //  매핑할 블로그 명 지정
@NoArgsConstructor
public class Blog extends Timestamped {

    @Id // 식별자
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 걸어주기
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name ="author", nullable = false)
    private String author;

    @Column(name = "content", nullable = false, length = 500) // length의 디폴트는 255
    private String content;


    public Blog(BlogRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        this.author = username;
        this.content = requestDto.getContent();
    }

    public void update(BlogRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
