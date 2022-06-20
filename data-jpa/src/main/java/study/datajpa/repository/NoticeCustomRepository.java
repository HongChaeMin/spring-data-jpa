package study.datajpa.repository;

import study.datajpa.dto.NoticeDTO;

public interface NoticeCustomRepository {

    void duplicateCheck(NoticeDTO noticeDTO);

}
