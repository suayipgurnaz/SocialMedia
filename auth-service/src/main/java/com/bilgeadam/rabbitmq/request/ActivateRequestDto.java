package com.bilgeadam.rabbitmq.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateRequestDto {
    private Long id;
    private String activationCode;
}
