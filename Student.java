package StudentBankApp;

import java.io.Serializable;

public class Student implements Serializable {
    private String studentId, name, department, batch, email, phone, dob;

    public Student(String studentId, String name, String department, String batch,
            String email, String phone, String dob) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.batch = batch;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getBatch() {
        return batch;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDob() {
        return dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}