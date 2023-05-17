package com.bilgeadam.rabbitmq.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    @NotBlank(message = "Kullanıcı adı boş geçilemez")
    @Size(min=3,max=20,message = "Kullanici adi en az 3, en fazla 20 karakter olmali")
    private String username;
    @Email
    private String email;
    @NotBlank
    @Size(min=5,max=32,message = "Kullanici adi en az 5, en fazla 32 karakter olmali")
    private String password;
}
