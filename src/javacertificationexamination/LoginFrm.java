// Pikulkaew Boonpeng
// 29/11/2019
package javacertificationexamination;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.sql.*;

/**
 * For logging in. The name cannot be duplicated. The ID must have 4 digits. No
 * registration needed. Just create the new account by enter a name and its
 * password. If the name has already taken, the user will be asked to check all
 * the fields.
 *
 * @author Pikulkaew
 */
public class LoginFrm {

    //header
    JLabel headLb = new JLabel("Welcome to Java Certification Examinations");
    //Name Label
    JLabel nameLb = new JLabel("Name:");
    //ID Label
    JLabel idLb = new JLabel("ID:");
    //Name Text Field
    JTextField nameTf = new JTextField();
    //ID Text Field (as circles)
    JPasswordField idTf = new JPasswordField(4);
    //Error Label
    JLabel errorLb = new JLabel();
    //Login Button
    JButton loginBtn = new JButton("Login");

    //Hello Frame after logging in
    HelloFrm helloFrm;

    //Strings for database
    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String DATABASE_URL = "jdbc:mysql://localhost/final";
    final String USER = "pk";
    final String PASSWORD = "pk";
    String QUERY_INSERT, QUERY_SELECT;

    /**
     * Constructs a new LoginFrm. Just adding components to the frame. Also, add
     * action to the loginBtn.
     *
     * @param frm Frame in main function.
     */
    public LoginFrm(JFrame frm) {

        loginBtn.addActionListener(e -> {
            int countDigit = 0;
            for (char pass : idTf.getPassword()) {
                if (Character.isDigit(pass)) {
                    countDigit++;
                }
            }
            if (countDigit != 4) {
                errorLb.setText("ID must have 4 digits.");
            } else {
                loginBtnAction(frm);
            }
        });

        frm.add(headLb);
        frm.add(nameLb);
        frm.add(nameTf);
        frm.add(idLb);
        frm.add(idTf);
        frm.add(errorLb);
        frm.add(loginBtn);
        setup();
    }

    /**
     * Action when the loginBtn is pressed.
     *
     * @param mainFrm Frame in main function.
     */
    private void loginBtnAction(JFrame mainFrm) {
        try {
            QUERY_SELECT = "SELECT name, id, numOfTry, score FROM user";
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(QUERY_SELECT);
            ResultSet resultSet = statement.executeQuery();
            Class.forName(DRIVER);
            boolean foundName = false;

            while (resultSet.next()) {
                String importedName = resultSet.getString(1);
                String importedId = resultSet.getString(2);
                int importedNumOfTry = resultSet.getInt(3);
                double importedScore = resultSet.getDouble(4);

                // name is found
                if (importedName.equals(nameTf.getText()) && importedId.equals(idTf.getText())) {
                    foundName = true;
                    setVisible(false);
                    helloAction(mainFrm, importedName, importedNumOfTry, importedScore);
                    errorLb.setText("");
                } else if (idTf.getText().length() != 4) {
                    errorLb.setText("ID must have 4 digits.");
                } else if (importedName.equals(nameTf.getText()) && !importedId.equals(idTf.getText())) {
                    foundName = true;
                    errorLb.setText("Please check you Name and/or ID.");
                }
            }

            if (foundName == false) {
                if (nameTf.getText().length() == 0 && idTf.getText().length() == 0) {
                    errorLb.setText("Please enter NAME and/or ID.");
                } else if (nameTf.getText().length() == 0 || idTf.getText().length() == 0) {
                    errorLb.setText("Please enter NAME and/or ID.");
                } else if (idTf.getText().length() != 4) {
                    errorLb.setText("ID must have 4 digits.");
                } else {
                    QUERY_INSERT = "INSERT INTO user(name, id, numOfTry, score) VALUES"
                            + "('" + nameTf.getText() + "', '" + idTf.getText() + "', 0, 0)";

                    statement = connection.prepareStatement(QUERY_INSERT);
                    statement.executeUpdate(QUERY_INSERT);

                    setVisible(false);
                    helloAction(mainFrm, nameTf.getText(), 0, 0);
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }
        nameTf.setText("");
        idTf.setText("");
    }

    private void helloAction(JFrame mainFrm, String name, int numOfTry, double score) {
        helloFrm = new HelloFrm(mainFrm);
        helloFrm.setVisible(true, name);
        helloFrm.checkNumOfTry(numOfTry);
        helloFrm.setCerticicationLabel(score);
        helloFrm.btnsActions(this, mainFrm, name, numOfTry, score);
        helloFrm.setName(name);
    }

    public void setVisible(boolean v) {
        headLb.setVisible(v);
        nameLb.setVisible(v);
        nameTf.setVisible(v);
        idLb.setVisible(v);
        idTf.setVisible(v);
        errorLb.setVisible(v);
        loginBtn.setVisible(v);
    }

    private void setup() {
        headLb.setBounds(135, 100, 500, 20);
        headLb.setFont(new Font("Helvetica", Font.BOLD, 20));
        headLb.setForeground(Color.BLUE);
        nameLb.setBounds(250, 200, 200, 20);
        nameLb.setFont(new Font("Helvetica", Font.BOLD, 16));
        nameLb.setForeground(Color.BLUE);
        nameTf.setBounds(305, 200, 130, 25);
        idLb.setBounds(279, 250, 200, 20);
        idLb.setFont(new Font("Helvetica", Font.BOLD, 16));
        idLb.setForeground(Color.BLUE);
        idTf.setBounds(305, 250, 130, 25);
        errorLb.setFont(new Font("Helvetica", Font.ITALIC, 14));
        errorLb.setForeground(Color.RED);
        errorLb.setBounds(265, 300, 500, 20);
        loginBtn.setBounds(275, 440, 150, 50);
    }
}
