package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

@Data
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    private String userName;

    private String teamName;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.userName = member.getUserName();
        this.teamName = member.getTeam().getName();
    }

}
