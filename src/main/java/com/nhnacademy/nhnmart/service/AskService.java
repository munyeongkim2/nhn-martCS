package com.nhnacademy.nhnmart.service;

import com.nhnacademy.nhnmart.domain.Answer;
import com.nhnacademy.nhnmart.domain.Ask;
import com.nhnacademy.nhnmart.repository.AskRepository;
import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AskService {
    private final AskRepository askRepository;
    private final List<String> CATEGORTY_LIST = Arrays.asList("불만 접수", "제안", "환불/교환", "칭찬해요", "기타 문의");
    private final String UPLOAD_DIR = "uploads/";

    public AskService(AskRepository askRepository) {
        this.askRepository = askRepository;
    }

    public List<Ask> getAsksForCustomer(String id) {
        List<Ask> asks = askRepository.getAsksForCustomer(id);
        Comparator<Ask> reverseDateComparator = Comparator.comparing(Ask::getDate).reversed();
        asks.sort(reverseDateComparator);
        return asks;
    }

    public Map<String, List<Ask>> getAsksForAgent() {
        Map<String, List<Ask>> askMap = askRepository.getAsksForAgent();
        for (String s : askMap.keySet()) {
            System.out.println("Key: " + askMap.size());
            System.out.println("  Ask: " + askMap.get(s).size());

        }

        return askMap;
    }


    public Ask getAsk(String id, String title) {
        return askRepository.getAsk(id, title);
    }

    public List<Ask> getAskForSearch(String id, String category) {
        return askRepository.getAskForSearch(id, category);
    }


    public List<String> getCategory() {
        return CATEGORTY_LIST;
    }

    public void addAsk(String id, String title, String category, String content) {
        Ask ask = new Ask(title, category, content, now());
        askRepository.save(id, ask);
    }

    public void addAnswer(String customerId, String title, String content, String agentId) {
        Answer answer = new Answer(content, now(), agentId);
        askRepository.saveAnswer(customerId, title, answer);
    }

    public void addAsk(String id, String title, String category, String content, List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("파일을 선택해 주세요.");
        }

        List<String> fileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException("파일 이름이 잘못되었습니다.");
            }

            // 파일의 MIME 타입 확인
            Path tempFile = Files.createTempFile("temp", fileName);
            file.transferTo(tempFile);
            String contentType = Files.probeContentType(tempFile);
            Files.delete(tempFile);

            // 허용된 이미지 파일 형식
            List<String> allowedTypes = List.of("image/gif", "image/jpeg", "image/png");
            if (contentType == null || !allowedTypes.contains(contentType)) {
                throw new IllegalArgumentException("GIF, JPG, JPEG, PNG 형식의 이미지 파일만 업로드 가능합니다.");
            }

            Path uploadPath = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(uploadPath.getParent());
            file.transferTo(uploadPath);

            fileNames.add(fileName);
        }

        Ask ask = new Ask(title, category, content, LocalDate.now().toString());
        ask.setFileList(fileNames);

        askRepository.save(id, ask);
    }

    public String now() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return now.format(formatter);
    }


}
