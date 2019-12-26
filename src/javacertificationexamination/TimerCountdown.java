// Pikulkaew Boonpeng
// 29/11/2019
package javacertificationexamination;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author PK
 */
public class TimerCountdown implements Runnable {

    JLabel clock = new JLabel();
    String time;
    boolean isTimeOut = false;
    int min = 10, sec = 0;
    JFrame mainFrm;
    ExamFrm examFrm;
    double score;

    public void setFrame(JFrame mainFrm) {
        this.mainFrm = mainFrm;
    }

    public void setFrame(ExamFrm examFrm) {
        this.examFrm = examFrm;
    }
    
    public void setScore(double score){
        this.score = score;
    }

    public TimerCountdown(JFrame frm) {
        setup();
        frm.add(clock);
    }

    private void setup() {
        clock.setBounds(295, 20, 200, 25);
        clock.setFont(new Font("Helvetica", Font.BOLD, 16));
        clock.setForeground(Color.BLUE);
    }

    @Override
    public void run() {
        try {
            while (!isTimeOut) {
                sec -= 1;
                if (sec < 0) {
                    min -= 1;
                    sec = 59;
                }

                if (min < 10) {
                    time = "0" + String.valueOf(min) + ":";
                }

                if (sec < 10) {
                    time += "0" + String.valueOf(sec);
                } else {
                    time += "" + String.valueOf(sec);
                }

                clock.setText("Timer "+time);
                if (min <= 0 && sec <= 0) {
                    isTimeOut = true;
                    JOptionPane.showMessageDialog(mainFrm, "Time is up.");
                    examFrm.setVisible(false);
                    setVisible(false);
                    OverFrm overFrm = new OverFrm(mainFrm);
                    setVisible(false);
                    overFrm.setScore(score);
                    overFrm.setHeadLb(score);
                    overFrm.setVisible(true);
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        }
    }

    public void setVisible(boolean v) {
        clock.setVisible(v);
    }
}
