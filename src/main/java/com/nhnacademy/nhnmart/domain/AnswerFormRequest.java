package com.nhnacademy.nhnmart.domain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AnswerFormRequest {
    @Size(min = 2, max = 200)
    String title;
    @Size(min = 0, max = 40000)
    String content;

}

