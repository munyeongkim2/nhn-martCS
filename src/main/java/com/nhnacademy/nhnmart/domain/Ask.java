package com.nhnacademy.nhnmart.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class Ask {
//    @Min(2)
//    @Max(200)
    @NotBlank
    @Size(min = 2, max = 200)
    private String title;
    private String category;
    @NotBlank
    @Size(min = 0, max = 40000)
    private String content;
    private String date;
    private List<String> fileList;
    private Answer answer;

    public Ask(String title, String category, String content, String date) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.date = date;
        this.answer = null;
        this.fileList = new ArrayList<>();
    }

    public Optional<Answer> getAnswer() {
        return Optional.ofNullable(answer);
    }


}
