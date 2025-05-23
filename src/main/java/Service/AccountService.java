package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if (account.username != "" && account.password.length() > 4 && accountDAO.getAccountByUsername(account.username) == null) {
            return accountDAO.registerAccount(account);
        }
        return null;
    }

    public Account loginAccount(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.password.equals(password)) {
            return account;
        }
        return null;
    }
}
