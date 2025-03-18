package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.*;

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
        Account account = accountDAO.geAccountById(message.getMessage_id());
        if (message.getMessage_text().length() > 0 && message.getMessage_text().length() < 255 && account != null) {
            return messageDAO.createMessage(message);
        }
        return null;
    }
}