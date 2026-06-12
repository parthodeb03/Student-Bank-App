package StudentBankApp;

import java.io.Serializable;

public class StudentAccountLink implements Serializable {
    private Student student;
    private Account account;

    public StudentAccountLink(Student student, Account account) {
        this.student = student;
        this.account = account;
    }

    public Student getStudent() {
        return student;
    }

    public Account getAccount() {
        return account;
    }
}