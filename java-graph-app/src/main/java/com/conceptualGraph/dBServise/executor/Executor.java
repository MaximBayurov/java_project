package com.conceptualGraph.dBServise.executor;

import java.sql.*;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String update) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(update);
        stmt.close();
    }

    public <T> T execQuery(String query,
                           ResultHandler<T> handler)
            throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();

        return value;
    }

    private PreparedStatement preparedStatement;

    public void preparePrepQuery(String SQLupdate) throws SQLException {
        preparedStatement = connection.prepareStatement(SQLupdate);
    }

    public <T> void setPrepString(int pos, T value) throws SQLException {
        if (value instanceof String) {
            preparedStatement.setString(pos, (String) value);
        } else if (value instanceof  Integer) {
            preparedStatement.setInt(pos, (int) value);
        } else {
            System.out.println("Неподдерживаемый тип переменной");
        }
    }

    public <T> T execPrerpQuery(String query,
                           ResultHandler<T> handler)
            throws SQLException {
        preparedStatement.execute(query);
        ResultSet result = preparedStatement.getResultSet();
        T value = handler.handle(result);
        result.close();
        return value;
    }
}
