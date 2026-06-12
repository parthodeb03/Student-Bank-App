package StudentBankApp;

import java.io.Serializable;

public class Account implements Serializable {
    private String password;
    private double balance;
    private boolean deleted;

    public Account(String password) {
        this.password = password;
        this.balance = 0;
        this.deleted = false;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void deposit(double amt) {
        balance += amt;
    }

    public boolean withdraw(double amt) {
        if (balance >= amt) {
            balance -= amt;
            return true;
        }
        return false;
    }
}