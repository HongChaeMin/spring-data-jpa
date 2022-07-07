package study.datajpa.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import study.datajpa.dto.NoticeDTO;
import study.datajpa.service.NoticeService;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class NoticeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    NoticeService noticeService;

    @Test
    @DisplayName("동시 api 요청 테스트")
    void createMultiTreadApiTest() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(1000);
        CountDownLatch count = new CountDownLatch(1000);
        AtomicInteger result = new AtomicInteger();

        for (int i = 0; i < 1000; i++) {
            service.execute(() -> {
                try {
                    NoticeDTO noticeDTO = NoticeDTO.builder()
                        .memberId(1L)
                        .teamId(1L)
                        .title("title")
                        .content("content")
                        .build();


                    noticeService.saveNotice(noticeDTO);
                    result.getAndIncrement(); // 결과 값 증가
                    log.info("success save");
                } catch (Exception e) {
                    log.error("e", e);
                }
                count.countDown(); // 시도 횟수 감소
            });
        }
        count.await();

        // 최종적으로 1개 밖에 저장 안됨
        assertThat(result.get()).isEqualTo(1);
        // 10번 다 돌았으니 0
        assertThat(count.getCount()).isEqualTo(0);
    }

}
