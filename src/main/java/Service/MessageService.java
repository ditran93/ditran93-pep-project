package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }


    public Message addMessage(Message message) {
        String messageText = message.getMessage_text();

        if(!messageDAO.isPostedByExistingUser(message.getPosted_by())) return null;
        if(messageText.isEmpty() || messageText.length() > 255) return null;

        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessageById(int messageId, String newMessageText) {
        if(!messageDAO.doesMessageExist(messageId)) return null;
        if(newMessageText.isEmpty() || newMessageText.length() > 255) return null;
        return messageDAO.updateMessageById(messageId, newMessageText);
    };

    public List<Message> getAllMessagesByUserId(int userId) {
        return messageDAO.getAllMessagesByUserId(userId);
    }
}
