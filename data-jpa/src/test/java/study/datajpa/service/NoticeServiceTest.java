package study.datajpa.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.internal.util.exceptions.MariaDbSqlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import study.datajpa.dto.NoticeDTO;

@Slf4j
@SpringBootTest
class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;

    @Test
    @DisplayName("중복 등록(멀티 쓰레드) 테스트")
    void createMultiThreadTest() throws InterruptedException {
        // 아아아ㅏㅇ아아아ㅏ앙아아아아ㅏ 머리 아파아아ㅏ아아ㅏ아아아아ㅏ아아아ㅏ아아악
        ExecutorService service = Executors.newFixedThreadPool(100);
        CountDownLatch count = new CountDownLatch(100);
        AtomicInteger result = new AtomicInteger();

        for (int i = 0; i < 100; i++) {
            service.execute(() -> {
                try {
                    NoticeDTO noticeDTO = NoticeDTO.builder()
                        .memberId(1L)
                        .teamId(1L)
                        .title("title133")
                        .content("content")
                        .build();

                    noticeService.saveNotice(noticeDTO);
                    result.getAndIncrement(); // 결과 값 증가
                    log.info("success save");
                } catch (PessimisticLockException e) {
                    log.error("락 획득 실패", e);
                } catch (LockTimeoutException e) {
                    log.error("락 대기 시간 초과", e);
                } catch (CannotAcquireLockException e) {
                    log.error("트랜잭션 롤백을 마킹", e);
                } catch (IllegalStateException e) {
                    log.error("중복 데이터", e);
                } catch (Exception e) {
                    log.error("e", e);
                }
                // Deadlock found when trying to get lock; try restarting transaction
                // 1. 요청 1, 요청 2...들이 동시에 같은 row에 Shared Lock을 잡게 된다
                // 2. 요청 1과 요청 2...들이 동시에 write 작업을 할 때 요청들은 서로 Shared Lock을 놓아줄 때 까지 기다린다
                // 3. 인셉션 팡팡
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