package com.example.easymrp.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class SignupRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;


}
