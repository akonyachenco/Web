package ru.ssau.todo.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {

    private String username;

    private List<String> roles;
}
