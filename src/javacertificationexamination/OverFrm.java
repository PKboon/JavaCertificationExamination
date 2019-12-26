// Pikulkaew Boonpeng
// 29/11/2019
package javacertificationexamination;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class OverFrm {

    String headStr;
    JLabel headLb = new JLabel(headStr);
    JButton quitBtn = new JButton("Quit");
    double score;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String DATABASE_URL = "jdbc:mysql://localhost/final";
    final String USER = "pk";
    final String PASSWORD = "pk";
    String QUERY_UPDATE;

    public OverFrm(JFrame frm) {
        setHeadLb(score);
        frm.setLayout(null);
        frm.add(headLb);
        frm.add(quitBtn);
        setup();
        
        quitBtn.addActionListener(e->{
            System.exit(0);
        });
    }

    public void setHeadLb(double score) {
        if (score >= 65 && score <= 74.9) {
            headStr = "<html>Congratulations!<br>You have gotten Java Certified Programmer!</htlm>";
        } else if (score >= 75 && score <= 84.9) {
            headStr = "<html>Congratulations!<br>You have gotten Java Certified Developer!</htlm>";
        } else if (score >= 85) {
            headStr = "<html>Congratulations!<br>You have gotten Java Certified Architect!</htlm>";
        } else {
            headStr = "Sorry, you did not pass. Try again!";
        }
    }
    
    public double getScore(){
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setVisible(boolean v) {
        headLb.setText(headStr);
        headLb.setVisible(v);
        quitBtn.setVisible(v);
    }

    private void setup() {
        headLb.setBounds(100, 70, 500, 100);
        headLb.setFont(new Font("Helvetica", Font.BOLD, 25));
        headLb.setForeground(Color.BLUE);
        quitBtn.setBounds(243, 300, 200, 50);
        quitBtn.setFont(new Font("Helvetica", Font.BOLD, 20));
        quitBtn.setBackground(Color.PINK);
        quitBtn.setForeground(Color.BLUE);
    }

}
