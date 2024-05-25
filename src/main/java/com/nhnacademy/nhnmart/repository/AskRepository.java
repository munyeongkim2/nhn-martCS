package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.Ask;
import com.nhnacademy.nhnmart.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface AskRepository {
    boolean existsAsk(String id);
    void save(String id, Ask ask);

    void saveAnswer(String id, String title, Answer answer) ;

    void delete(String id, Ask ask);

    List<Ask> getAsksForCustomer(String id);
    List<Ask> getAskForSearch(String id, String category);
    Map<String, List<Ask>> getAsksForAgent();

    Ask getAsk(String id, String title);

    void addAnswer(String id, String title , Answer answer);
}
