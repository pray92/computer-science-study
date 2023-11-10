package jpabook.oopquerylanguage.dto;

import jpabook.oopquerylanguage.entity.Member;
import jpabook.oopquerylanguage.entity.Team;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTeamDTO {

    private Member member;

    private Team team;

    public MemberTeamDTO(Member member, Team team) {
        this.member = member;
        this.team = team;
    }

    @Override
    public String toString() {
        return "MemberTeamDTO{" +
                "member=" + member +
                ", team=" + team +
                '}';
    }
}
