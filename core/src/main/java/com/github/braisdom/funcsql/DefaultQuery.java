package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DefaultQuery<T> extends AbstractQuery<T> {

    public DefaultQuery(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public List<T> execute(Relationship... relationships) throws SQLException {
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        String sql = sqlGenerator.createQuerySQL(getTableName(domainModelClass), projection, filter, groupBy,
                having, orderBy, offset, limit);

        List<T> rows = executeInternally(connection, domainModelClass, sql);

        if (relationships.length > 0)
            return new RelationshipNetwork(connection, domainModelClass, rows, relationships).process();

        return rows;
    }

    @Override
    public List<Row> executeRawly() throws SQLException {
        return null;
    }

    @Override
    public <C extends Class> List<C> execute(C relevantDomainClass, Relationship... relationships) throws SQLException {
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String sql = sqlGenerator.createQuerySQL(getTableName(relevantDomainClass), projection, filter, groupBy,
                having, orderBy, offset, limit);

        return null;
    }
}