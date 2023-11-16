package jpabook.oopquerylanguage.querydsl;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jpabook.oopquerylanguage.entity.Member;
import jpabook.oopquerylanguage.entity.QMember;

public class MemberExpression {
    @QueryDelegate(Member.class)
    public static BooleanExpression isOld(QMember member) {
        return member.age.gt(30);
    }
}
