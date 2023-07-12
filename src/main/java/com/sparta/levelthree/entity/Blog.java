package com.sparta.levelthree.entity;

import com.sparta.levelthree.dto.BlogRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "content", nullable = false, length = 500) // length의 디폴트는 255
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // blog 삭제시 comment가 같이 삭제되도록 cascade 추가
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Blog(BlogRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.user = user;
        this.content = requestDto.getContent();
    }

    public void update(BlogRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
