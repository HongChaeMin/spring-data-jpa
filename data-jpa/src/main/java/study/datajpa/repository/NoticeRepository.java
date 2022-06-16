package study.datajpa.repository;

import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Notice> findAllByIdIn(@Param("Id") List<Long> Id);

}
