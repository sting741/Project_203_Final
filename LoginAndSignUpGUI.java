import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class LoginAndSignUpGUI {
    private HashMap<String, String> users = new HashMap<>();
    private static final String USER_DATA_FILE = "users.txt";
    private String loggedInUser = null;

    public LoginAndSignUpGUI() {
        loadUserData();
    }

    public boolean showLoginScreen() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(signUpButton);

        JDialog loginDialog = new JDialog((Frame) null, "Login or Sign Up", true);
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        loginDialog.getContentPane().add(loginPanel);
        loginDialog.setSize(400, 200);
        loginDialog.setLocationRelativeTo(null);

        final boolean[] authenticated = {false};

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (checkCredentials(username, password)) {
                loggedInUser = username;
                JOptionPane.showMessageDialog(null, "Login successful!");
                authenticated[0] = true;
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
            }
        });

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(null, "Username already exists.");
            } else {
                users.put(username, password);
                saveUserData();
                JOptionPane.showMessageDialog(null, "Sign up successful! You can now log in.");
            }
        });

        loginDialog.setVisible(true);
        return authenticated[0];
    }

    private boolean checkCredentials(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    private void loadUserData() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // File not found, create a new one
            saveUserData();
        }
    }

    private void saveUserData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            for (String username : users.keySet()) {
                bw.write(username + ":" + users.get(username));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }
}
