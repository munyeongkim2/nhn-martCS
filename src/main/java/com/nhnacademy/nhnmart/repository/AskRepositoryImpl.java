package com.nhnacademy.nhnmart.repository;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.Ask;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AskRepositoryImpl implements AskRepository {


    private final Map<String, List<Ask>> askMap = new HashMap<>();

    public AskRepositoryImpl() {
    }

    @Override
    public boolean existsAsk(String id) {
        return askMap.containsKey(id);
    }

    @Override
    public void save(String id, Ask ask) {
        if (!existsAsk(id)) {
            List<Ask> newAsk = new ArrayList<>();
            newAsk.add(ask);
            askMap.put(id, newAsk);
        } else {
            askMap.get(id).add(ask);
        }
    }


    @Override
    public void saveAnswer(String id, String title, Answer answer) {
        if (!existsAsk(id)) {
            throw new RuntimeException("수정할 게시글 없음");
        }
        for (Ask ask : askMap.get(id)) {
            if (ask.getTitle().equals(title)) {
                ask.setAnswer(answer);
            }
        }
    }

    @Override
    public void delete(String id, Ask ask) {
        askMap.get(id).remove(ask);
    }

    @Override
    public List<Ask> getAsksForCustomer(String id) {
        return askMap.get(id);
    }

    @Override
    public List<Ask> getAskForSearch(String id, String category) {
        List<Ask> searchList = new ArrayList<>();
        for (Ask ask : askMap.get(id)) {
            if (ask.getCategory().equals(category)) {
                searchList.add(ask);
            }
        }
        return searchList;
    }

    @Override
    public Map<String, List<Ask>> getAsksForAgent() {
        Map<String, List<Ask>> agentMap = new HashMap<>();
        for (String id : askMap.keySet()) {
            List<Ask> noAnswerList = new ArrayList<>();
            for (Ask ask : askMap.get(id)) {
                if (ask.getAnswer().isEmpty()) {
                    noAnswerList.add(ask);
                }
            }
            agentMap.put(id, noAnswerList);
        }

        return agentMap;
    }

    @Override
    public Ask getAsk(String id, String title) {
        for (Ask ask : askMap.get(id)) {
            if (ask.getTitle().equals(title)) {
                return ask;
            }
        }
        return null;
    }

    @Override
    public void addAnswer(String id, String title, Answer answer) {
        Answer newAnswer = new Answer(answer.getContent(), answer.getDate(), answer.getAgentName());
        for (Ask ask : askMap.get(id)) {
            if (ask.getTitle().equals(title)) {
                ask.setAnswer(newAnswer);
            }
        }

    }
}
