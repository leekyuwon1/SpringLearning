package helloo.coree.member;

public class MemberServiceImpl implements MemberService{ // 관례상 구현체가 하나만 존재할땐 XxxImpl 이렇게 사용

    private final MemberRepository memberRepository = new MemoryMemberRepository(); // 추상화를 의존하지 않는다. DIP 위반!!

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
