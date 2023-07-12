package com.sparta.levelthree.repository;

import com.sparta.levelthree.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
