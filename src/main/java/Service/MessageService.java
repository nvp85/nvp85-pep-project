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
        return messageDAO.deleteMessageById(id);
    }
}