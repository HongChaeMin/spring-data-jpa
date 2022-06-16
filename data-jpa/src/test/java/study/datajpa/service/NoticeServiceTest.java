package study.datajpa.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import study.datajpa.dto.NoticeDTO;

@Slf4j
@SpringBootTest
class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;

    @Test
    @DisplayName("중복 등록(멀티 쓰레드) 테스트")
    void createMultiThreadTest() throws InterruptedException {
        Long Id = 100L;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch count = new CountDownLatch(10);
        AtomicInteger result = new AtomicInteger();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            service.execute(() -> {
                try {
                    NoticeDTO noticeDTO = NoticeDTO.builder().noticeId((long) finalI).title("title" + finalI).content("content" + finalI).build();
                    noticeService.saveNotice(Id, noticeDTO);
                    result.getAndIncrement(); // 결과 값 증가
                    log.info("success save");
                } catch (CannotAcquireLockException e) {
                    log.error("collision detection");
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