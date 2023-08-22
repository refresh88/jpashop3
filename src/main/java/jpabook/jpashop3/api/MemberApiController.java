package jpabook.jpashop3.api;

import jakarta.validation.Valid;
import jpabook.jpashop3.domain.Member;
import jpabook.jpashop3.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 분명 API에 엔티티 노출시키지 말라 했다!!
    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    /**
     * Result 클래스를 사용하지 않고
     * 이런 리스트가 리턴으로 넘어오면,
     * 예를들어 저 값에 count 값을 추가 하고 싶다해도 추가할수 없음.
     * 리스트는 비슷한 구조의 값만 들어올수 있기 때문에.
     * 따라서 Result 클래스를 생성해서 한번 감싸주어 해결.
     * */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }



    @PostMapping("/api/v1/members")
    /**
     * @RequestBody는 들어온 json 및 xml 데이터를 Member에 매핑해줌.
     * API 스펙을 위한 DTO를 만들어야함. Member같은 엔티티를 이렇게 외부에서 Json을 바인딩 하는데 사용하면 안됨.
     * 엔티티의 내부 속성을 변경해버리면 API 스펙이 변경되는것을 의미함. 절대 금지.
     * API를 만들때는 항상 엔티티를 파라미터로 받지 말것. 엔티티를 외부에 노출해서도 안됨.
     *
     * */
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     *이렇게 생성하면 좋은점은
     * 1. 혹여나 엔티티나 변경되도 set메서드만 바꿔주면 되서 api 스펙에 영향을 끼치지 않는다(하지만 엔티티는 바꾸면 안됨.)
     * 2. 기본적으로 API 문서를 열어보지 않는 이상 Member에서 무슨 값이 넘어오는지 알 방법이 없음.
     *     하지만 API 전용 DTO를 만들어주면 해당 DTO만 열어보면 무슨값이 넘어오는지 바로 알 수 있음.
     * 3. @NotEmpty 같은 validation 설정을 API 스펙에 맞게 마음껏 해도됨.
     *     하지만 이걸 엔티티에서 하면 특정 값이 어떤 API에선 null이 허용되고 어떤 API에선 null이 허용 안될수도 있음.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
