package ru.ssau.course_project.entity.dto;

import lombok.Data;

@Data
public class TokenResponseDto {

    private String accessToken;

    private String refreshToken;
}
