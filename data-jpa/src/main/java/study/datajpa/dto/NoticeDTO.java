package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.datajpa.entity.Notice;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {

    private Long noticeId;
    private Long memberId;
    private Long teamId;
    private String title;
    private String content;

    public static Notice of(NoticeDTO noticeDTO) {
        return Notice.builder()
            .memberId(noticeDTO.getMemberId())
            .teamId(noticeDTO.getTeamId())
            .title(noticeDTO.getTitle())
            .content(noticeDTO.getContent())
            .build();
    }

}
