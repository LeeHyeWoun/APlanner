package hobby.leehyewoun.aplanner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import hobby.leehyewoun.aplanner.R;
import hobby.leehyewoun.aplanner.ScoreModifyActivity;
import hobby.leehyewoun.aplanner.adapter.ListAdapter_Score;
import hobby.leehyewoun.aplanner.bean.SaveBean;
import hobby.leehyewoun.aplanner.bean.ScoreBean;
import hobby.leehyewoun.aplanner.util.PrefUtil;

public class ScoreList1Fragment extends Fragment {
    //xml 의 객체
    private ListView scoreList;
    private TextView txtNoList;

    //어답터
    private ListAdapter_Score listAdapterScore;

    //Gson
    Gson gson = new Gson();

    //Bean
    private SaveBean saveBean = new SaveBean();

    //tab 위치
    private int tabOfNum;
    private String KeyName_tabData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_list,null);

        //아이디 찾기
        scoreList = view.findViewById(R.id.scoreList);
        txtNoList = view.findViewById(R.id.txtNoList);

        txtNoList.setVisibility(View.GONE);

        //탭 데이터의 키값 설정
        KeyName_tabData = tabOfNum+"tabScoreData";

        txtNoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //density 구하기
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float density = displayMetrics.density; //px = dp*density;

                //화면 크기 구하기
                DisplayMetrics dm = getActivity().getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;

                int TabX = 0;
                int j=1;
                while (j*100*density*2 + 100*density < width){
                    j++;
                }
                if(tabOfNum+1<=j){
                    TabX = -1 * ((j-tabOfNum)*100);
                }
                else if(PrefUtil.getDataInt(getActivity(),"semester")-tabOfNum<=j){
                    TabX = (tabOfNum-j)*100;
                }

                Intent intent = new Intent(getActivity(),ScoreModifyActivity.class);
                intent.putExtra("tabOfNum",tabOfNum);
                intent.putExtra("tabX",TabX);
                getActivity().startActivity(intent);
            }
        });
        scoreList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //현재 학기에 대한 데이터 받기
                int semester = PrefUtil.getDataInt(getActivity(),"semester");

                //density 구하기
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float density = displayMetrics.density; //px = dp*density;

                //화면 크기 구하기
                DisplayMetrics dm = getActivity().getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;

                int TabX = 0;
                int j=1;
                while (j*100*density*2 + 100*density < width){
                    j++;
                }
                if(tabOfNum+1<=j){
                    TabX = -1 * (j-tabOfNum)*100;
                }
                else if(semester-tabOfNum<=j){
                    TabX = (tabOfNum-j)*100;
                }

                Intent intent = new Intent(getActivity(),ScoreModifyActivity.class);
                intent.putExtra("tabOfNum",tabOfNum);
                intent.putExtra("tabX",TabX);
                startActivity(intent);
                return true;
            }
        });

        return view;
    }//end onCreateView

    @Override
    public void onResume() {
        super.onResume();

        //기존에 저장한 리스트 불러오기
        String jsonScoreBeanData = PrefUtil.getData(getActivity(),KeyName_tabData);
        if(jsonScoreBeanData.length()>0){
            saveBean = gson.fromJson(jsonScoreBeanData,SaveBean.class);
        }
        if(saveBean.getScoreListBean()==null){
            saveBean.setScoreListBean(new ArrayList<ScoreBean>());
            txtNoList.setVisibility(View.VISIBLE);
        }
        else if(saveBean.getScoreListBean().size()==0){
            txtNoList.setVisibility(View.VISIBLE);
        }
        else{
            txtNoList.setVisibility(View.GONE);
        }

        listAdapterScore = new ListAdapter_Score(getContext(),saveBean.getScoreListBean());
        listAdapterScore.setTabOfNumForList(tabOfNum);
        scoreList.setAdapter(listAdapterScore);
    }//end onResume

    //tabOfNum 설정자
    public void setTabOfNum(int tabNum){
        tabOfNum = tabNum;
    }

    //리스트 어답터 갱신 ScoreList1Fragment->PagerAdapter -> ScoreActivity 까지 전달하기
    public void listNotifyDataSetChanged(){listAdapterScore.notifyDataSetChanged();}

}//end class
