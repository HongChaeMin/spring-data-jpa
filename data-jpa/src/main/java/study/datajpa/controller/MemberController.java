package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();

        return member.getUserName();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        // 사용할때는 데이터 변경하면 안됨 조회용으로만 사용해야됨
        // 근데 안쓰는게 좋은듯
        return member.getUserName();
    }

    @GetMapping("/members")
    public Page<MemberDTO> list(@PageableDefault(size = 5) Pageable pageable) {
        // /members?page=0&size=3&sort=id,desc&sort=username,desc
        // https://velog.io/@youns1121/JPA-java.lang.IllegalStateException-Cannot-call-sendError-after-the-response-has-been-committed
        return memberRepository.findAll(pageable).map(MemberDTO::new);
    }

}
