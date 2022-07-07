package study.datajpa.repository.notice;

import study.datajpa.dto.NoticeDTO;

public interface NoticeCustomRepository {

    void duplicateCheck(NoticeDTO noticeDTO);

}
