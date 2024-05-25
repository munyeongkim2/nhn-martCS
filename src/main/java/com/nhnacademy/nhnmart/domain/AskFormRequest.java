package com.nhnacademy.nhnmart.domain;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AskFormRequest {
    @Size(min = 2, max = 200)
    String title;
    @Size(min = 1, max = 40000)
    String content;

}
