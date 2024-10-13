package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account(username, password) VALUES(?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()) {
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    };

    public boolean doesUsernameExist(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM Account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {    
                return rs.getInt(1) > 0;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account resultAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return resultAccount;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
