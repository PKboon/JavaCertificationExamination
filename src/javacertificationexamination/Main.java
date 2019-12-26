// Pikulkaew Boonpeng
// 29/11/2019
// Most of the questions are from free java tests
package javacertificationexamination;

import javax.swing.*;

/**
 * Main class for this project.
 * @author Pikulkaew Boonpeng
 */
public class Main extends JFrame {

    JFrame mainFrm = new JFrame("FINAL PROJECT by PIKULKAEW BOONPENG");
    LoginFrm loginFrm = new LoginFrm(mainFrm);
    public Main() {
        mainFrm.setLayout(null);
        mainFrm.setVisible(true);
        mainFrm.setSize(710, 640);
        loginFrm.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
