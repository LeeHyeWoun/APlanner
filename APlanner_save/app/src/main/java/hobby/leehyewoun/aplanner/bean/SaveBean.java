package hobby.leehyewoun.aplanner.bean;

import java.io.Serializable;
import java.util.List;

public class SaveBean implements Serializable {
    private List<ScoreBean> scoreListBean;
    private List<Boolean > tabs;

    //getter
    public List<ScoreBean> getScoreListBean() {
        return scoreListBean;
    }

    public List<Boolean> getTabs() {
        return tabs;
    }

    //setter
    public void setScoreListBean(List<ScoreBean> scoreListBean) {
        this.scoreListBean = scoreListBean;
    }

    public void setTabs(List<Boolean> tabs) {
        this.tabs = tabs;
    }
}
