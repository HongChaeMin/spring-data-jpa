package study.datajpa.repository.notice;

import static study.datajpa.entity.QNotice.notice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import study.datajpa.dto.NoticeDTO;
import study.datajpa.entity.Notice;

@RequiredArgsConstructor
public class NoticeCustomRepositoryImpl implements NoticeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void duplicateCheck(NoticeDTO noticeDTO) {
        Notice duplicateNotice = queryFactory
            .selectFrom(notice)
            .where(
                notice.memberId.eq(noticeDTO.getMemberId()),
                notice.teamId.eq(noticeDTO.getTeamId()),
                notice.title.eq(noticeDTO.getTitle())
            )
            .setLockMode(LockModeType.PESSIMISTIC_WRITE) // 키야...
            .fetchOne();
        if (duplicateNotice != null) throw new RuntimeException("중복된 데이터 입니다.");
    }
}
