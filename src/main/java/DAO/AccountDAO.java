package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    public Account registerAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into Account (username, password) values (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.username);
            preparedStatement.setString(2, account.password);
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generatedId = pkeyResultSet.getInt(1);
                account.setAccount_id(generatedId);
                return account;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    };

    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from Account where username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Account(resultSet.getInt("account_id"),
                resultSet.getString("username"),
                resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from Account where account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Account(resultSet.getInt("account_id"),
                resultSet.getString("username"),
                resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
};

