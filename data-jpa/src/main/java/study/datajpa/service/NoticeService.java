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
        // method 1개라도 걸리면 전체 적용하나봄
        noticeRepository.findAllByIdIn(Collections.singletonList(Id));
        noticeRepository.save(NoticeDTO.of(noticeDTO));
    }

}
