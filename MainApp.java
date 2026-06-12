package StudentBankApp;

import java.awt.*;
import javax.swing.*;

public class MainApp extends JFrame {
    private CardLayout cl = new CardLayout();
    private JPanel mainPanel = new JPanel(cl);
    private DataManager dm = new DataManager();

    public void showPanel(String name) {
        cl.show(mainPanel, name);
    }

    // CHOICE PANEL
    private class ChoicePanel extends JPanel {
        public ChoicePanel() {
            setLayout(new GridLayout(3, 1, 10, 10));
            JButton createBtn = new JButton("Create Account");
            createBtn.addActionListener(e -> showPanel("CREATE"));
            JButton loginBtn = new JButton("Login");
            loginBtn.addActionListener(e -> showPanel("LOGIN"));
            JButton exitBtn = new JButton("Exit");
            exitBtn.addActionListener(e -> System.exit(0));
            add(createBtn);
            add(loginBtn);
            add(exitBtn);
        }
    }

    //CREATE ACCOUNT PANEL
    private class CreateAccountPanel extends JPanel {
        private JTextField idField, nameField, deptField, batchField, emailField, phoneField, dobField;
        private JPasswordField passField;

        public CreateAccountPanel() {
            setLayout(new BorderLayout());
            JPanel form = new JPanel(new GridLayout(8, 2, 5, 5));
            idField = new JTextField();
            nameField = new JTextField();
            deptField = new JTextField();
            batchField = new JTextField();
            emailField = new JTextField();
            phoneField = new JTextField();
            dobField = new JTextField();
            passField = new JPasswordField();

            form.add(new JLabel("Student ID:"));
            form.add(idField);
            form.add(new JLabel("Name:"));
            form.add(nameField);
            form.add(new JLabel("Dept:"));
            form.add(deptField);
            form.add(new JLabel("Batch:"));
            form.add(batchField);
            form.add(new JLabel("Email:"));
            form.add(emailField);
            form.add(new JLabel("Phone:"));
            form.add(phoneField);
            form.add(new JLabel("DOB:"));
            form.add(dobField);
            form.add(new JLabel("Password:"));
            form.add(passField);

            JButton createBtn = new JButton("Create Account");
            createBtn.addActionListener(e -> createAccount());
            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(e -> showPanel("CHOICE"));
            JButton exitBtn = new JButton("Exit");
            exitBtn.addActionListener(e -> System.exit(0));

            JPanel btnPanel = new JPanel();
            btnPanel.add(createBtn);
            btnPanel.add(backBtn);
            btnPanel.add(exitBtn);

            add(form, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
        }

        private void createAccount() {
            String id = idField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (id.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID or Password Empty!");
                return;
            }
            if (!dm.isStudentIdUnique(id)) {
                JOptionPane.showMessageDialog(this, "Duplicate ID!");
                return;
            }
            if (!dm.isPasswordUnique(pass)) {
                JOptionPane.showMessageDialog(this, "Duplicate Password!");
                return;
            }

            Student s = new Student(id, nameField.getText(), deptField.getText(), batchField.getText(),
                    emailField.getText(), phoneField.getText(), dobField.getText());
            Account a = new Account(pass);
            dm.addLink(s, a);

            JOptionPane.showMessageDialog(this, "Account Created! Go to Login.");
            showPanel("LOGIN");

            // Clear fields
            idField.setText("");
            nameField.setText("");
            deptField.setText("");
            batchField.setText("");
            emailField.setText("");
            phoneField.setText("");
            dobField.setText("");
            passField.setText("");

            // refresh login panel
            Component[] comps = mainPanel.getComponents();
            for (Component c : comps) {
                if (c instanceof LoginPanel)
                    ((LoginPanel) c).loadAccounts();
            }
        }
    }

    //LOGIN PANEL
    private class LoginPanel extends JPanel {
        private JComboBox<String> selectAccount;
        private JPasswordField passField;

        public LoginPanel() {
            setLayout(new BorderLayout());
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            selectAccount = new JComboBox<>();
            passField = new JPasswordField();
            loadAccounts();

            panel.add(new JLabel("Select Account:"));
            panel.add(selectAccount);
            panel.add(new JLabel("Password:"));
            panel.add(passField);

            JButton loginBtn = new JButton("Log In");
            loginBtn.addActionListener(e -> login());
            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(e -> showPanel("CHOICE"));

            JPanel btnPanel = new JPanel();
            btnPanel.add(loginBtn);
            btnPanel.add(backBtn);

            add(panel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
        }

        private void loadAccounts() {
            selectAccount.removeAllItems();
            for (StudentAccountLink l : dm.getAllAccounts()) {
                selectAccount.addItem(l.getStudent().getStudentId() + " - " +
                        (l.getAccount().isDeleted() ? "[Deleted]" : "[Active]"));
            }
        }

        private void login() {
            if (selectAccount.getSelectedItem() == null)
                return;
            String id = selectAccount.getSelectedItem().toString().split(" - ")[0];
            String pass = new String(passField.getPassword()).trim();
            StudentAccountLink link = dm.getByIdAndPass(id, pass);
            if (link == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
                return;
            }
            mainPanel.add(new LoggedInPanel(link), "LOGGED");
            showPanel("LOGGED");
        }
    }



    //LOGGED IN PANEL 
    private class LoggedInPanel extends JPanel {
        private StudentAccountLink link;
        private JTextArea infoArea;
        private JButton updateBtn, depositBtn, withdrawBtn, deleteBtn, recoverBtn;

        public LoggedInPanel(StudentAccountLink link) {
            this.link = link;
            setLayout(new BorderLayout());
            infoArea = new JTextArea();
            infoArea.setEditable(false);
            refreshInfo();

            JPanel btnPanel = new JPanel();
            updateBtn = new JButton("Update Info");
            depositBtn = new JButton("Deposit");
            withdrawBtn = new JButton("Withdraw");
            deleteBtn = new JButton("Delete Account");
            recoverBtn = new JButton("Recover Account");
            JButton backBtn = new JButton("Back");
            JButton logoutBtn = new JButton("Logout");

            btnPanel.add(updateBtn);
            btnPanel.add(depositBtn);
            btnPanel.add(withdrawBtn);
            btnPanel.add(deleteBtn);
            btnPanel.add(recoverBtn);
            btnPanel.add(backBtn);
            btnPanel.add(logoutBtn);

            add(new JScrollPane(infoArea), BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);

            updateBtn.addActionListener(e -> updateInfo());
            depositBtn.addActionListener(e -> deposit());
            withdrawBtn.addActionListener(e -> withdraw());
            deleteBtn.addActionListener(e -> deleteAccount());
            recoverBtn.addActionListener(e -> recoverAccount());
            backBtn.addActionListener(e -> showPanel("LOGIN"));
            logoutBtn.addActionListener(e -> showPanel("CHOICE"));

            updateButtonsState();
        }

        private void refreshInfo() {
            Student s = link.getStudent();
            Account a = link.getAccount();
            infoArea.setText(
                    "Student ID: " + s.getStudentId() +
                            "\nName: " + s.getName() +
                            "\nDepartment: " + s.getDepartment() +
                            "\nBatch: " + s.getBatch() +
                            "\nEmail: " + s.getEmail() +
                            "\nPhone: " + s.getPhone() +
                            "\nDOB: " + s.getDob() +
                            "\nBalance: " + a.getBalance() +
                            "\nDeleted: " + a.isDeleted());
        }

        private void updateButtonsState() {
            boolean active = !link.getAccount().isDeleted();
            updateBtn.setEnabled(active);
            depositBtn.setEnabled(active);
            withdrawBtn.setEnabled(active);
            deleteBtn.setEnabled(active);
            recoverBtn.setEnabled(!active); // recover enabled only if deleted
        }

        private void updateInfo() {
            Student s = link.getStudent();
            s.setName(JOptionPane.showInputDialog(this, "Name", s.getName()));
            s.setDepartment(JOptionPane.showInputDialog(this, "Department", s.getDepartment()));
            s.setBatch(JOptionPane.showInputDialog(this, "Batch", s.getBatch()));
            s.setEmail(JOptionPane.showInputDialog(this, "Email", s.getEmail()));
            s.setPhone(JOptionPane.showInputDialog(this, "Phone", s.getPhone()));
            s.setDob(JOptionPane.showInputDialog(this, "DOB", s.getDob()));
            dm.save();
            refreshInfo();
        }

        private void deposit() {
            try {
                double amt = Double.parseDouble(JOptionPane.showInputDialog(this, "Deposit amount"));
                link.getAccount().deposit(amt);
                dm.save();
                refreshInfo();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            }
        }

        private void withdraw() {
            try {
                double amt = Double.parseDouble(JOptionPane.showInputDialog(this, "Withdraw amount"));
                if (!link.getAccount().withdraw(amt))
                    JOptionPane.showMessageDialog(this, "Insufficient balance!");
                dm.save();
                refreshInfo();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            }
        }

        private void deleteAccount() {
            int ans = JOptionPane.showConfirmDialog(this, "Delete this account?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                link.getAccount().setDeleted(true);
                dm.save();
                JOptionPane.showMessageDialog(this, "Account Deleted!");
                refreshInfo();
                updateButtonsState();
                
                
                
                // refresh login panel
                Component[] comps = mainPanel.getComponents();
                for (Component c : comps) {
                    if (c instanceof LoginPanel)
                        ((LoginPanel) c).loadAccounts();
                }
            }
        }

        private void recoverAccount() {
            if (!link.getAccount().isDeleted()) {
                JOptionPane.showMessageDialog(this, "Account is already active!");
                return;
            }
            link.getAccount().setDeleted(false);
            dm.save();
            JOptionPane.showMessageDialog(this, "Account Recovered!");
            refreshInfo();
            updateButtonsState();
            // refresh login panel
            Component[] comps = mainPanel.getComponents();
            for (Component c : comps) {
                if (c instanceof LoginPanel)
                    ((LoginPanel) c).loadAccounts();
            }
        }
    }   
    
    public MainApp() {
        setTitle("Student Bank App");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(new ChoicePanel(), "CHOICE");
        mainPanel.add(new CreateAccountPanel(), "CREATE");
        mainPanel.add(new LoginPanel(), "LOGIN");

        add(mainPanel);
        cl.show(mainPanel, "CHOICE");
    }    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}