package jpabook.oopquerylanguage.dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class H2DialectImpl extends H2Dialect {

    public H2DialectImpl() {
        super();
        registerFunction("SUM", new StandardSQLFunction("SUM"));
    }
}
