package com.yadan.saleticket.base.hibernate;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * hibernate 表名策略
 */
public class TableNamingStrategy extends org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return Identifier.toIdentifier(addPrefix(super.toPhysicalTableName(name, context).getText()));
    }

    public String addPrefix(final String composedTableName) {
        return "yd_st_" + composedTableName.toLowerCase();

    }
}
