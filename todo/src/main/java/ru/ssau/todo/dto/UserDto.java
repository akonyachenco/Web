package ru.ssau.todo.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    Long id;

    String username;

    String password;

    List<String> roles;
}
