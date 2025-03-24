package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.*;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }
    
    public Message createMessage(Message message) {
        // check if the posted_by is a valid account_id
        Account account = accountDAO.getAccountById(message.getPosted_by());
        if (message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255 && account != null) {
            return messageDAO.createMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {
        Message message = messageDAO.getMessageById(id);
        if (message == null) {
            return null;
        }
        int affectedRows = messageDAO.deleteMessageById(id);
        // doublecheck if the message existed
        if (affectedRows == 1) {
            return message;
        }
        return null;
    }

    public List<Message> getMessagesByPosted_by(int posted_by) {
        return messageDAO.getMessagesByPosted_by(posted_by);
    }

    public Message updateMessageById(int id, String text) {
        if ((text.length() > 255) || (text.length() == 0)) {
            return null;
        }
        Message message = messageDAO.getMessageById(id);
        if (message != null) {
            int affectedRows = messageDAO.updateMessageById(id, text);
            if (affectedRows == 1) {
                // retrieve the updated message to ensure accuracy (downside: additional db request)
                message = messageDAO.getMessageById(id);
                return message; 
            }
        }
        return null;
    }
}