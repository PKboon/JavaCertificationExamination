// Pikulkaew Boonpeng
// 29/11/2019
package javacertificationexamination;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class HelloFrm {

    String welcomeStr = "Hello ";
    JLabel headLb = new JLabel(welcomeStr);
    JLabel certiLb = new JLabel("");
    JLabel numOfTryLb = new JLabel("");
    JButton logOutBtn = new JButton("Log out");
    JButton startBtn = new JButton("Start!");
    String name;

    ExamFrm examFrm;
    Thread examThread;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String DATABASE_URL = "jdbc:mysql://localhost/final";
    final String USER = "pk";
    final String PASSWORD = "pk";
    String QUERY_INSERT, QUERY_SELECT, QUERY_UPDATE;

    public HelloFrm(JFrame frm) {
        frm.setLayout(null);
        frm.add(headLb);
        frm.add(certiLb);
        frm.add(numOfTryLb);
        frm.add(logOutBtn);
        frm.add(startBtn);
        setup();
    }

    public void btnsActions(LoginFrm loginFrm, JFrame mainFrm, String name, int numOfTry, double score) {
        if (numOfTry > 1) {
            startBtn.setEnabled(false);
        }

        logOutBtn.addActionListener(e -> {
            int logOut = JOptionPane.showOptionDialog(mainFrm, "Are you sure?", "Logging Out", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            // yes
            if (logOut == 0) {
                setVisible(false, "");
                loginFrm.setVisible(true);
            }
        });

        startBtn.addActionListener(e -> {
            int startExam = JOptionPane.showOptionDialog(mainFrm, "Are you ready?", "Start the Examination", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            // yes
            if (startExam == 0) {
                examFrm = new ExamFrm(mainFrm);
                examThread = new Thread(examFrm);
                examThread.start();
                setVisible(false, "");
                examFrm.setName(name);
                examFrm.setVisible(true);

                try {
                    QUERY_SELECT = "SELECT numOfTry FROM user";
                    Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                    PreparedStatement statement = connection.prepareStatement(QUERY_SELECT);
                    ResultSet resultSet = statement.executeQuery();
                    Class.forName(DRIVER);

                    while (resultSet.next()) {
                        int importedNumOfTry = resultSet.getInt(1);
                        int newNum = importedNumOfTry;
                        if (importedNumOfTry > 2) {
                            newNum = 2;
                        } else {
                            newNum++;
                        }

                        QUERY_UPDATE = "UPDATE user SET numOfTry = " + newNum + " WHERE name = '" + name + "'";
                        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                        statement = connection.prepareStatement(QUERY_UPDATE);
                        statement.executeUpdate(QUERY_UPDATE);
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                } catch (ClassNotFoundException ce) {
                    ce.printStackTrace();
                }
            }
        });
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void checkNumOfTry(int numOfTry) {
        String msg = "";
        if (numOfTry == 1) {
            msg = "This is the LAST attemp. Good luck!!";

        } else if (numOfTry == 0) {
            msg = "This is your FIRST attemp. Good luck!!";
        } else {
            msg = "You have already used 2 tries.";
        }
        numOfTryLb.setText(msg);
    }

    public void setCerticicationLabel(double score) {
        String msg = "You've gotten ";
        if (score >= 65 && score <= 74.9) {
            msg += "Java Certified Programmer!";
        } else if (score >= 75 && score <= 84.9) {
            msg += "Java Certified Developer!";
        } else if (score >= 85) {
            msg += "Java Certified Architect!";
        } else {
            msg = "";
        }
        certiLb.setText(msg);
    }

    public void setVisible(boolean v, String name) {
        headLb.setText("<html>" + welcomeStr + name + "!<br>"
                + "<br>You have 10 minutes to finish these 20 questions about Java.</html>");
        setName(name);
        headLb.setVisible(v);
        certiLb.setVisible(v);
        numOfTryLb.setVisible(v);
        logOutBtn.setVisible(v);
        startBtn.setVisible(v);
    }

    private void setup() {
        headLb.setBounds(70, 50, 500, 100);
        headLb.setFont(new Font("Helvetica", Font.BOLD, 20));
        headLb.setForeground(Color.BLUE);
        certiLb.setBounds(70, 200, 1000, 50);
        certiLb.setFont(new Font("Helvetica", Font.BOLD, 30));
        certiLb.setForeground(Color.BLACK);
        numOfTryLb.setBounds(70, 280, 1000, 50);
        numOfTryLb.setFont(new Font("Helvetica", Font.BOLD, 20));
        numOfTryLb.setForeground(Color.BLUE);
        logOutBtn.setBounds(250, 460, 150, 50);
        logOutBtn.setFont(new Font("Helvetica", Font.BOLD, 20));
        logOutBtn.setBackground(Color.PINK);
        startBtn.setBounds(250, 380, 150, 50);
        startBtn.setFont(new Font("Helvetica", Font.BOLD, 20));
        startBtn.setForeground(Color.YELLOW);
        startBtn.setBackground(Color.BLUE);
    }

}
