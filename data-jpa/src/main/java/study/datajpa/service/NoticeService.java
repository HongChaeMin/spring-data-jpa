package study.datajpa.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.NoticeDTO;
import study.datajpa.entity.Notice;
import study.datajpa.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // @Transactional/*(isolation = Isolation.SERIALIZABLE)*/
    // - 트랜잭션에서 일관성 없는 데이터를 어떻게 허용할지에 대한 허용 수준 옵션
    // - 다른 사용자가 해당 영역에 있는 모든 데이터에 대한 수정과 입력이 불가능하게 만들어 Phantom Read를 방지
    // @Transactional(propagation = Propagation.NEVER)
    @Transactional
    public void saveNotice(NoticeDTO noticeDTO) {
        // findAll 이기 때문에 모든 행을 다 찾아서 모든 행(전체 엔티티)에 락이 걸린듯
        // 원인 찾음
        // -> mysql의 경우 오라클과 다르게 유니크 인덱스나 일반 인덱스가 걸리지 않았다면 전체 테이블을 읽기 때문에 전체에 락이 걸리게 된다
        // -> 유니크 인덱스는 완벽한 row 단위 락이 걸림
        // -> 일반 인덱스는 참조 했던 row가 모두 락이 걸림
        // -> 인덱스가 없어 테이블 전체를 읽으면 모든 row가 락이 걸림

        // ... 테스트 실패
        // 지금까지 테스트를 성공했던 이유가 내가 데이터가 많아서 계속 지우고 테스트 해서 성공했던 거였음
        // 데이터를 생성한 채 테스트를 진행하니 인덱스가 걸려서 전체 테이블을 못읽어서 락이 안걸림
        // -> 테이블 전체 락 방법 필요...

        // noticeRepository.findAllByIdIn(getNoticeIds(noticeRepository.findAll()));
        Notice findNotice = noticeRepository.findByMemberIdAndTeamIdAndTitle(
            noticeDTO.getMemberId(),
            noticeDTO.getTeamId(),
            noticeDTO.getTitle()
        );
        if (findNotice != null) throw new IllegalStateException("중복 데이터");

        // 벨리데이션 체크하니까 잘 됨... 코드 개판이고만

        noticeRepository.save(NoticeDTO.of(noticeDTO));
    }

    private List<Long> getNoticeIds(List<Notice> notices) {
        List<Long> noticeIds = new ArrayList<>();
        for (Notice notice : notices) {
            noticeIds.add(notice.getId());
        }
        return noticeIds;
    }

}
