package study.datajpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Version;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
// @OptimisticLocking(type = OptimisticLockType.ALL) -> 얘는 낙관적 락임
// @DynamicUpdate -> 실제 값이 변경된 컬럼으로만 update 쿼리를 만드는 기능
public class Notice extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    // 엔티티를 수정할 때마다 해당 version 필드가 하나씩 자동으로 증가
    // 엔티티를 수정할 때 조회 시점의 버전과 수정 시점의 버전이 다르면 예외를 발생
    // 예를 들어 앞서의 A와 B의 수정 상황이라면, B가 수정을 할 때 버전이 다르므로(A가 수정하면서 버전이 하나 증가) 예외가 발생
    @Version // 최초 커밋만 인정
    Integer version;
    // UPDATE BOARD
    // SET
    //     TITLE=?,
    //     VERSION=? (버전 + 1 증가)
    // WHERE
    //     ID=?
    //     AND VERSION=? (버전 비교)

}
