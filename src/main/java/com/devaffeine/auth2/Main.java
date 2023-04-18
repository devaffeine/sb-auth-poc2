package com.devaffeine.auth2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) throws SQLException {
        DataSource slave = DataSourceFactory.createDataSource("jdbc:mysql://localhost:6447/auth_poc", "root", "root_pass", true);
        DataSource master = DataSourceFactory.createDataSource("jdbc:mysql://localhost:6446/auth_poc", "root", "root_pass", false);


        insertUser(master,"user1", "asd");
        var user = readUser(master,"user1");
        System.out.println("User from master: " + user.getUsername());
        var user1 = readUser(slave,"user1");
        System.out.println("User from slave: " + user1.getUsername());
    }

    private static void insertUser(DataSource ds, String user, String password) throws SQLException {
        String query = "INSERT INTO tb_users (username, password) VALUES (?, ?)";
        executeUpdate(ds, query, user, password);
    }

    private static User readUser(DataSource ds, String username) throws SQLException {
        String query = "SELECT * FROM tb_users WHERE username = ?";
        var callback = (Function<ResultSet, User>)(rs) -> {
            try {
                var user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                return user;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return executeQuery(ds, query, callback, username).stream().findFirst().orElse(null);
    }

    private static <T> List<T> executeQuery(DataSource ds, String query, Function<ResultSet, T> callback, Object... args) throws SQLException {
        try(var conn = ds.getConnection()) {
            try(var stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < args.length; i++) {
                    stmt.setObject(i + 1, args[i]);
                }
                List<T> result = new ArrayList<>();
                try(var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.add(callback.apply(rs));
                    }
                }
                return result;
            }
        }
    }

    private static int executeUpdate(DataSource ds, String query, Object... args) throws SQLException {
        try(var conn = ds.getConnection()) {
            try(var stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < args.length; i++) {
                    stmt.setObject(i + 1, args[i]);
                }
                return stmt.executeUpdate();
            }
        }
    }
}
