package com.nhnacademy.nhnmart.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class FormRequest {
    @NotBlank
    String title;
    @Size(min = 1, max = 3)
    String content;

}
