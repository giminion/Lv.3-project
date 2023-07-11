package com.sparta.levelthree.dto;

import lombok.Getter;

@Getter
public class MessageResponseDto {
    private String message;
    private String statusCode;

    public MessageResponseDto(String message, String statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
