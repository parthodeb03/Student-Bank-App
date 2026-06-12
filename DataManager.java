package StudentBankApp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<StudentAccountLink> links;
    private final String DATA_FILE = "student_data.ser";

    public DataManager() {
        links = loadData();
    }    
    
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(links);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLink(Student s, Account a) {
        links.add(new StudentAccountLink(s, a));
        save();
    }

    public boolean isStudentIdUnique(String id) {
        for (StudentAccountLink l : links) {
            if (l.getStudent().getStudentId().equals(id))
                return false;
        }
        return true;
    }

    public boolean isPasswordUnique(String pass) {
        for (StudentAccountLink l : links) {
            if (l.getAccount().getPassword().equals(pass))
                return false;
        }
        return true;
    }

    public StudentAccountLink getByIdAndPass(String id, String pass) {
        for (StudentAccountLink l : links) {
            if (l.getStudent().getStudentId().equals(id) &&
                    l.getAccount().getPassword().equals(pass))
                return l;
        }
        return null;
    }

    public List<StudentAccountLink> getAllAccounts() {
        return links;
    }

    @SuppressWarnings("unchecked")
    private List<StudentAccountLink> loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists())
            return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (List<StudentAccountLink>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}