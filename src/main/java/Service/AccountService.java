package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account registerAccount(Account account) {
        //check if username is blank
        String username = account.getUsername();
        if(username.isEmpty() || username == null) return null;

        //check if password is at least 4 characters
        String password = account.getPassword();
        if(password.length() < 4) return null;

        //check if username exist
        if(accountDAO.doesUsernameExist(username)) return null;

        return accountDAO.insertAccount(account);
    }

    public Account login(Account account) {
        Account resultAccount = accountDAO.login(account);

        if(resultAccount == null) return null;

        if(resultAccount.getPassword().equals(account.getPassword()) ) return resultAccount;

        return null;
    }
}
