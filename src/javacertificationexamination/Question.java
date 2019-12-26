// Pikulkaew Boonpeng
// 29/11/2019
package javacertificationexamination;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JRadioButton;

/**
 *
 * @author PK
 */
public class Question {

    int group, number;
    String question, choice1, choice2, choice3, choice4, answer, rbValue;
    JRadioButton rb1 = new JRadioButton();
    JRadioButton rb2 = new JRadioButton();
    JRadioButton rb3 = new JRadioButton();
    JRadioButton rb4 = new JRadioButton();
    JRadioButton rb5 = new JRadioButton();
    ArrayList<String> shuffledChoices = new ArrayList<>();
    boolean isShuffled = false;

    public Question() {

    }

    public Question(int group, int number, String question, String choice1,
            String choice2, String choice3, String choice4, String answer) {
        this.group = group;
        this.number = number;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.answer = answer;
        this.rbValue = "";
    }

    public void shuffleChoicesArray() {
        shuffledChoices.add(choice1);
        shuffledChoices.add(choice2);
        shuffledChoices.add(choice3);
        shuffledChoices.add(choice4);
        shuffledChoices.add(answer);
        if (isShuffled == false) {
            Collections.shuffle(shuffledChoices);
            isShuffled = true;
        }
    }

    public ArrayList<String> getShuffledChoices() {
        return shuffledChoices;
    }

    public void setValuesToRbs() {
        this.rb1.setText(shuffledChoices.get(0));
        this.rb2.setText(shuffledChoices.get(1));
        this.rb3.setText(shuffledChoices.get(2));
        this.rb4.setText(shuffledChoices.get(3));
        this.rb5.setText(shuffledChoices.get(4));
    }

    public JRadioButton getRb1() {
        return this.rb1;
    }

    public JRadioButton getRb2() {
        return this.rb2;
    }

    public JRadioButton getRb3() {
        return this.rb3;
    }

    public JRadioButton getRb4() {
        return this.rb4;
    }

    public JRadioButton getRb5() {
        return this.rb5;
    }

    public String getValueSelected() {
        return rbValue;
    }
    
    public void setValueSelected(String rbValue){
        this.rbValue = rbValue;
    }

    public int getGroup() {
        return this.group;
    }

    public int getNumber() {
        return this.number;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getChoice1() {
        return this.choice1;
    }

    public String getChoice2() {
        return this.choice2;
    }

    public String getChoice3() {
        return this.choice3;
    }

    public String getChoice4() {
        return this.choice4;
    }

    public String getAnswer() {
        return this.answer;
    }

}
