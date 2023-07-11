package com.sparta.levelthree.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    @Pattern(regexp = "[a-z0-9]{4,10}")
    private String username;
    @Pattern(regexp = "[a-zA-Z0-9]{8,15}")
    private String password;
}