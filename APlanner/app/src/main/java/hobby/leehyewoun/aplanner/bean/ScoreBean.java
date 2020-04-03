package hobby.leehyewoun.aplanner.bean;

import java.io.Serializable;

public class ScoreBean implements Serializable {
    private String category;
    private String Score;
    private String credit;
    private String subject;

    //getter
    public String getCategory() { return category; }
    public String getScore() { return Score; }
    public String getCredit() { return credit; }
    public String getSubject() { return subject; }

    //setter
    public void setCategory(String category) { this.category = category; }
    public void setScore(String score) { Score = score; }
    public void setCredit(String credit) { this.credit = credit; }
    public void setSubject(String subject) { this.subject = subject; }
}
