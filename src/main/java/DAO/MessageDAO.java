package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isPostedByExistingUser(int userId) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT COUNT(*) FROM Account WHERE account_id= ? ;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch(SQLException e) {
            System.out.println();
        }
        return false;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        Message message;
        try {
            String sql = "SELECT * FROM Message Where message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()) {
                message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessage(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        Message message;
        try {
            message = getMessageById(messageId);
            String sql = "DELETE FROM Message Where message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            int rowsEffected = preparedStatement.executeUpdate();

            if(rowsEffected > 0) { 
                return message;
            }
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessageById(int messageId, String newMessageText) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;

        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, messageId);
            

            int rowsUpdate = preparedStatement.executeUpdate();

            if(rowsUpdate > 0) {
                message = getMessageById(messageId);
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean doesMessageExist(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT COUNT(*) FROM MESSAGE WHERE message_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, messageId);

        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch(SQLException e) {
        System.out.println(e.getMessage());
    }

    return false;
    }

    public List<Message> getAllMessagesByUserId(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }
}
