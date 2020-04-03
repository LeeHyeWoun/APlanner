package hobby.leehyewoun.aplanner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import hobby.leehyewoun.aplanner.R;
import hobby.leehyewoun.aplanner.ScoreModifyActivity;
import hobby.leehyewoun.aplanner.bean.SaveBean;
import hobby.leehyewoun.aplanner.bean.ScoreBean;
import hobby.leehyewoun.aplanner.util.PrefUtil;

public class ScoreTop1Fragment extends Fragment {

    private ProgressBar PbThisSemester,PbTotal,PbCreditsEarned;
    private TextView txtThisSemester,txtTotal,txtCreditsEarned;

    private Gson gson = new Gson();

    //변수
    private int semester;       //현재 학기 정보
    private int creditsEarned=0;  //총 이수 학점
    private String KeyName_tabData;
    private int tabOfNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_top1,null);

        //아이디 찾기
        PbThisSemester = view.findViewById(R.id.thisSemester);
        PbTotal = view.findViewById(R.id.Total);
        PbCreditsEarned = view.findViewById(R.id.creditsEarned);
        txtThisSemester = view.findViewById(R.id.txtThisSemester);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtCreditsEarned = view.findViewById(R.id.txtCreditsEarned);

        return view;
    }//end onCreateView

    @Override
    public void onResume() {

        //탭 데이터의 키값 설정
        KeyName_tabData = tabOfNum+"tabScoreData";

        //현재 학기 정보 불러오기
        semester = PrefUtil.getDataInt(getActivity(),"semester");
        creditsEarned = PrefUtil.getDataInt0(getActivity(),"sumCredit");


        //선택 학기 평균 학점 설정-------------------------------------------------------------------
        //기존에 저장한 리스트 불러오기
        SaveBean saveBean = new SaveBean();
        String jsonScoreBeanData = PrefUtil.getData(getActivity(),KeyName_tabData);
        if(jsonScoreBeanData.length()>0){
            saveBean = gson.fromJson(jsonScoreBeanData,SaveBean.class);
        }
        if(saveBean.getScoreListBean()==null){
            saveBean.setScoreListBean(new ArrayList<ScoreBean>());
        }
        int sumOfScore = 0;
        int sumOfCredit = 0;
        for(int i=0; i<saveBean.getScoreListBean().size(); i++){
            String s = saveBean.getScoreListBean().get(i).getScore();
            if(!s.equals("P")){
                sumOfScore += valueOfScore(s)*Integer.parseInt(saveBean.getScoreListBean().get(i).getCredit());
                sumOfCredit += Integer.parseInt(saveBean.getScoreListBean().get(i).getCredit());
            }
        }
        if(sumOfScore>0){
            float result = (sumOfScore/sumOfCredit)*0.01f;
            PbThisSemester.setProgress(sumOfScore/sumOfCredit);
            txtThisSemester.setText(String.format("%.2f", result));
        }
        else {
            PbThisSemester.setProgress(0);
            txtThisSemester.setText(String.valueOf(0));
        }


        //총 평균 학점 설정-------------------------------------------------------------------------


        //총 이수 학점 설정-------------------------------------------------------------------------
        PbCreditsEarned.setProgress(creditsEarned);
        txtCreditsEarned.setText(String.valueOf(creditsEarned));
        super.onResume();
    }//end onResume

    //tabOfNum 설정자
    public void setTabOfNum(int tabNum){
        tabOfNum = tabNum;
    }

    private int valueOfScore(String s){
        int scoreValue=0;
        switch (s){
            case "A+":
                scoreValue = 450;
                break;
            case "A":
                scoreValue = 400;
                break;
            case "B+":
                scoreValue = 350;
                break;
            case "B":
                scoreValue = 300;
                break;
            case "C+":
                scoreValue = 250;
                break;
            case "C":
                scoreValue = 200;
                break;
            case "D+":
                scoreValue = 150;
                break;
            case "D":
                scoreValue = 100;
                break;
            case "F":
                scoreValue = 0;
                break;
        }
        return scoreValue;
    }
    //프래그먼트 애니메이션
    private class ProgressBarAnimation extends Animation{
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }
    }


    }//end class

