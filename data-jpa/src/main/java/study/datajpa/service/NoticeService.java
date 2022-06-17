package study.datajpa.service;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.NoticeDTO;
import study.datajpa.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional // 필요
    public void saveNotice(Long Id, NoticeDTO noticeDTO) {
        // lock을 걸면 row나 entity에 락이 걸리기 때문에 지금은 엔티티가 같이 걸린거임
        noticeRepository.findAllByIdIn(Collections.singletonList(Id));
        noticeRepository.save(NoticeDTO.of(noticeDTO));
    }

}
