// Pikulkaew Boonpeng
// 21/10/2019
package javacertificationexamination;

import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import java.util.*;

public class ExamFrm implements Runnable {

    JLabel questionLb = new JLabel();
    JTextArea textArea = new JTextArea();
    JScrollPane textSPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    ButtonGroup choiceRbsGroup = new ButtonGroup();
    ArrayList<JRadioButton> rbArr = new ArrayList<>();
    JRadioButton choice1Rb = new JRadioButton();
    JRadioButton choice2Rb = new JRadioButton();
    JRadioButton choice3Rb = new JRadioButton();
    JRadioButton choice4Rb = new JRadioButton();
    JRadioButton choice5Rb = new JRadioButton();
    JButton nextBtn = new JButton("Next >");
    JButton prevBtn = new JButton("< Previous");
    public JButton submitBtn = new JButton("Submit");
    JLabel numberOfQuestionLb = new JLabel();

    double numOfWrongAns = 0, numOfCorrectAns = 0;
    final double NUM_OF_Qs = 20.0;
    final double POINT = 5.0;
    final int GROUP_MAX = 4;
    final int GROUP_MIN = 1;
    final int NUMBER_MAX = 25;
    final int NUMBER_MIN = 1;

    Random random = new Random();
    int group, indexQ = 0;
    ArrayList<Question> allQuestionArr = new ArrayList<>();
    Question[] twentyQsArr = new Question[20];
    Set<Integer> usedQsIndex = new LinkedHashSet<>();

    OverFrm overFrm;
    TimerCountdown timerCountdown;
    Thread timerThread;

    String name;
    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String DATABASE_URL = "jdbc:mysql://localhost/final";
    final String USER = "pk";
    final String PASSWORD = "pk";
    String QUERY_UPDATE;

    public ExamFrm(JFrame frm) {
        timerCountdown = new TimerCountdown(frm);
        timerCountdown.setFrame(frm);
        timerCountdown.setFrame(this);
        timerThread = new Thread(timerCountdown);
        timerThread.start();

        frm.add(numberOfQuestionLb);
        frm.add(questionLb);
        frm.add(textSPane);
        frm.add(choice1Rb);
        frm.add(choice2Rb);
        frm.add(choice3Rb);
        frm.add(choice4Rb);
        frm.add(choice5Rb);
        frm.add(nextBtn);
        frm.add(prevBtn);
        frm.add(submitBtn);
        setup();

        loadAllQuestionToArray();

        setEnableBtns(indexQ);
        group = random.nextInt(GROUP_MAX) + 1; // random group for the first question
        // load first question to 20 Qs array
        numberOfQuestionLb.setText(String.valueOf(indexQ + 1) + ".");
        loadQuestiontoTwentyQsArr(indexQ, group, getRandomNumber());
        setQuestionAndRbs(twentyQsArr, indexQ);

        nextBtn.addActionListener(e -> {
            nextBtnAction();
        });

        prevBtn.addActionListener(e -> {
            prevBtnAction();
        });

        submitBtn.addActionListener(e -> {
            submitBtnAction(frm);
        });
    }

    public void setName(String name) {
        this.name = name;
    }

    //DONE
    private void setSelectedRb(Question[] arr, int index) {
        if (!arr[index].getValueSelected().isEmpty()) {
            for (Enumeration btns = choiceRbsGroup.getElements(); btns.hasMoreElements();) {
                JRadioButton rb = (JRadioButton) btns.nextElement();
                if (arr[index].getValueSelected().equals(rb.getText())) {
                    rb.setSelected(true);
                }
            }
        }
    }

    //DONE
    private void nextBtnAction() {
        String selectedValue = getSelectedValue(choiceRbsGroup);
        twentyQsArr[indexQ].setValueSelected(selectedValue);

        if (twentyQsArr[indexQ].getValueSelected().length() != 0) {
            if (twentyQsArr[indexQ].getValueSelected().equals(twentyQsArr[indexQ].getAnswer())) {
                group = ((group += 1) > GROUP_MAX) ? GROUP_MAX : group;
                numOfCorrectAns += 1;
            } else {
                group = ((group -= 1) < GROUP_MIN) ? GROUP_MIN : group;
                numOfWrongAns += 1;
            }
        }

        indexQ += 1;
        indexQ = (indexQ > 19) ? 19 : indexQ;
        if (twentyQsArr[indexQ] == null) {
            loadQuestiontoTwentyQsArr(indexQ, group, getRandomNumber());
        }
        setEnableBtns(indexQ);
        numberOfQuestionLb.setText(String.valueOf(indexQ + 1) + ".");
        setQuestionAndRbs(twentyQsArr, indexQ);
        choiceRbsGroup.clearSelection();
        setSelectedRb(twentyQsArr, indexQ);
    }

    //DONE
    private void prevBtnAction() {
        String selectedValue = getSelectedValue(choiceRbsGroup);
        twentyQsArr[indexQ].setValueSelected(selectedValue);

        indexQ -= 1;
        indexQ = (indexQ < 0) ? 0 : indexQ;
        setEnableBtns(indexQ);
        numberOfQuestionLb.setText(String.valueOf(indexQ + 1) + ".");
        setQuestionAndRbs(twentyQsArr, indexQ);
        choiceRbsGroup.clearSelection();
        setSelectedRb(twentyQsArr, indexQ);
    }

    private void submitBtnAction(JFrame mainFrm) {
        updateScore();
        timerCountdown.setVisible(false);
        overFrm = new OverFrm(mainFrm);
        setVisible(false);
        overFrm.setScore(getScore());
        overFrm.setHeadLb(getScore());
        overFrm.setVisible(true);
    }

    //DONE
    private void setEnableBtns(int index) {
        if (index == 0) {
            prevBtn.setEnabled(false);
        } else if (index == 19) {
            nextBtn.setEnabled(false);
        } else {
            prevBtn.setEnabled(true);
            nextBtn.setEnabled(true);
        }
    }

    //DONE
    private void loadQuestiontoTwentyQsArr(int index, int group, int number) {
        int indexOfAllQs = getIndexQuestion(group, number);
        twentyQsArr[index] = allQuestionArr.get(indexOfAllQs);
    }

    private int getIndexQuestion(int group, int number) {
        int index = 1;

        for (int i = 0; i < allQuestionArr.size(); i++) {
            if (allQuestionArr.get(i).getGroup() == group
                    && allQuestionArr.get(i).getNumber() == number) {
                index = i;
            }
        }

        if (usedQsIndex.add(index)) {
            return index;
        } else {
            return getIndexQuestion(group, getRandomNumber());
        }

    }

    private String getSelectedValue(ButtonGroup btnGroup) {
        for (Enumeration btns = btnGroup.getElements(); btns.hasMoreElements();) {
            JRadioButton rb = (JRadioButton) btns.nextElement();

            if (rb.isSelected()) {
                return rb.getText();
            }
        }
        return "";
    }

    // shuffle choices and answer. **Depends on loadChoicesToArray method
    private void setQuestionAndRbs(Question[] arr, int index) {
        arr[index].shuffleChoicesArray();
        textArea.setText(arr[index].getQuestion());
        choice1Rb.setText(arr[index].getShuffledChoices().get(0));
        choice2Rb.setText(arr[index].getShuffledChoices().get(1));
        choice3Rb.setText(arr[index].getShuffledChoices().get(2));
        choice4Rb.setText(arr[index].getShuffledChoices().get(3));
        choice5Rb.setText(arr[index].getShuffledChoices().get(4));
    }

    //DONE
    private int getRandomNumber() {
        return random.nextInt(NUMBER_MAX) + 1;
    }

    //DONE
    private void loadAllQuestionToArray() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement("SELECT g,n, q, c1, c2, c3, c4, ans FROM question");
            ResultSet resultSet = statement.executeQuery("SELECT g,n, q, c1, c2, c3, c4, ans FROM question");
            Class.forName(DRIVER);

            while (resultSet.next()) {
                int g = resultSet.getInt(1);
                int n = resultSet.getInt(2);
                String q = resultSet.getString(3).replaceAll("\\n", "\n");
                String c1 = resultSet.getString(4);
                String c2 = resultSet.getString(5);
                String c3 = resultSet.getString(6);
                String c4 = resultSet.getString(7);
                String ans = resultSet.getString(8);
                Question question = new Question(g, n, q, c1, c2, c3, c4, ans);
                allQuestionArr.add(question);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }
    }

    // DONE
    public double getScore() {
        double step1 = (NUM_OF_Qs - numOfWrongAns) / NUM_OF_Qs;
        double step2 = 1.0 - step1;
        double step3 = step2 * numOfWrongAns;
        double step4 = step3 + numOfWrongAns;
        double step5 = (numOfCorrectAns * 5) - step4;
        return step5;
    }

    private void updateScore() {
        try {
            QUERY_UPDATE = "UPDATE user SET score = " + getScore() + " WHERE name = '" + name + "'";
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE);
            statement.executeUpdate(QUERY_UPDATE);
            Class.forName(DRIVER);
            timerCountdown.setScore(getScore());
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                updateScore();
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
        }
    }

    private void setup() {
        numberOfQuestionLb.setBounds(93, 60, 100, 20);
        numberOfQuestionLb.setFont(new Font("Helvetica", Font.BOLD, 16));
        textArea.setEditable(false);
        textArea.setFont(new Font("Helvetica", Font.PLAIN, 16));
        textSPane.setBounds(117, 60, 450, 150);
        prevBtn.setBounds(227, 400, 120, 50);
        nextBtn.setBounds(347, 400, 120, 50);
        submitBtn.setBounds(285, 460, 120, 50);
        rbArr.add(choice1Rb);
        rbArr.add(choice2Rb);
        rbArr.add(choice3Rb);
        rbArr.add(choice4Rb);
        rbArr.add(choice5Rb);
        choiceRbsGroup.add(choice1Rb);
        choiceRbsGroup.add(choice2Rb);
        choiceRbsGroup.add(choice3Rb);
        choiceRbsGroup.add(choice4Rb);
        choiceRbsGroup.add(choice5Rb);
        choice1Rb.setBounds(105, 210, 700, 30);
        choice1Rb.setFont(new Font("Helvetica", Font.PLAIN, 14));
        choice2Rb.setBounds(105, 245, 700, 30);
        choice2Rb.setFont(new Font("Helvetica", Font.PLAIN, 14));
        choice3Rb.setBounds(105, 280, 700, 30);
        choice3Rb.setFont(new Font("Helvetica", Font.PLAIN, 14));
        choice4Rb.setBounds(105, 315, 700, 30);
        choice4Rb.setFont(new Font("Helvetica", Font.PLAIN, 14));
        choice5Rb.setBounds(105, 350, 700, 30);
        choice5Rb.setFont(new Font("Helvetica", Font.PLAIN, 14));
    }

    public void setVisible(boolean v) {
        numberOfQuestionLb.setVisible(v);
        questionLb.setVisible(v);
        textSPane.setVisible(v);
        choice1Rb.setVisible(v);
        choice2Rb.setVisible(v);
        choice3Rb.setVisible(v);
        choice4Rb.setVisible(v);
        choice5Rb.setVisible(v);
        nextBtn.setVisible(v);
        prevBtn.setVisible(v);
        submitBtn.setVisible(v);
    }
}
