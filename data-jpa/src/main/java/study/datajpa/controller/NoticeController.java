package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.NoticeDTO;
import study.datajpa.service.NoticeService;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("notice")
    public void saveNotice(@RequestBody NoticeDTO noticeDTO) {
        noticeService.saveNotice(noticeDTO);
    }

}
