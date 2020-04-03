package hobby.leehyewoun.aplanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import hobby.leehyewoun.aplanner.R;
import hobby.leehyewoun.aplanner.ScoreModifyActivity;
import hobby.leehyewoun.aplanner.bean.SaveBean;
import hobby.leehyewoun.aplanner.bean.ScoreBean;
import hobby.leehyewoun.aplanner.util.PrefUtil;

public class ListAdapter_Score extends BaseAdapter {
    //생성자의 매개변수 값을 받을 변수
    private Context mContext;
    private List<ScoreBean> mList;

    //tab 위치
    private int tabOfNum;

    //생성자
    public ListAdapter_Score(Context context, List<ScoreBean> list){
        mContext=context;
        mList=list;
    }

    @Override
    public int getCount() { return mList.size(); }

    @Override
    public Object getItem(int position) { return position; }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //인플레이팅 하는 작업
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_score_list, null);

        //해당 ROW 의 데이터를 찾는 작업
        final ScoreBean scoreBean = mList.get(position);

        //인플레이팅 된 뷰에서 ID 찾는작업
        TextView txtCategory = convertView.findViewById(R.id.txtCategory);
        TextView txtScore = convertView.findViewById(R.id.txtScore);
        TextView txtCredit = convertView.findViewById(R.id.txtCredit);
        TextView txtSubject = convertView.findViewById(R.id.txtSubject);
        final View edtLayout = convertView.findViewById(R.id.edtLayout);
        final View listTool = convertView.findViewById(R.id.listTool);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        //데이터 설정
        txtCategory.setText(scoreBean.getCategory());
        txtScore.setText(scoreBean.getScore());
        txtCredit.setText(scoreBean.getCredit());
        txtSubject.setText(scoreBean.getSubject());

        //상태 설정
        listTool.setVisibility(View.GONE);
        if(PrefUtil.getDataInt(mContext,tabOfNum+"EditList")==position){
            edtLayout.setSelected(true);
        }
        else {
            edtLayout.setSelected(false);
        }

        if(PrefUtil.getDataInt(mContext,tabOfNum+"EditList")==position){
            Animation aniA = new AlphaAnimation(0,1);
            aniA.setDuration(400);
            listTool.setAnimation(aniA);
            listTool.setVisibility(View.VISIBLE);
        }

        //이벤트---------------------------------------------------------------------------------
        edtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listTool.getVisibility()==View.GONE){
                    PrefUtil.setData(mContext,tabOfNum+"EditList",position);
                    notifyDataSetChanged();
                }
                else{
                    PrefUtil.setData(mContext, tabOfNum+"EditList",-1);
                    notifyDataSetChanged();
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //density 구하기
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
                float density = displayMetrics.density; //px = dp*density;

                //화면 크기 구하기
                DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;

                int TabX = 0;
                int j=1;
                while (j*100*density*2 + 100*density < width){
                    j++;
                }
                if(tabOfNum+1<=j){
                    TabX = -1 * ((j-tabOfNum)*100);
                }
                else if(PrefUtil.getDataInt(mContext,"semester")-tabOfNum<=j){
                    TabX = (tabOfNum-j)*100;
                }

                //데이터 전달
                Intent intent = new Intent(mContext,ScoreModifyActivity.class);
                intent.putExtra("tabOfNum",tabOfNum);
                intent.putExtra("tabX",TabX);
                mContext.startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //삭제
                mList.remove(position);
                //이수한 학점 삭제
                if(!scoreBean.getScore().equals("F")){
                    int sumCredit = PrefUtil.getDataInt0(mContext,"sumCredit");
                    sumCredit-= Integer.parseInt(scoreBean.getCredit());
                    PrefUtil.setData(mContext,"sumCredit",sumCredit);
                }
                //리스트 저장하기
                SaveBean saveBean = new SaveBean();
                Gson gson = new Gson();
                saveBean.setScoreListBean(mList);
                PrefUtil.setData(mContext,tabOfNum+"tabScoreData",gson.toJson(saveBean));

                //선택 초기화
                PrefUtil.setData(mContext, tabOfNum+"EditList",-1);

                //리스트 갱신
                notifyDataSetChanged();

                //추가 알림 토스트 띄우기
                //종성 받침의 유무에 따라 '가'/'이' 구분
                if((scoreBean.getSubject().charAt(scoreBean.getSubject().length() - 1) - 0xAC00) % 28>0){
                    Toast.makeText(mContext,"'"+scoreBean.getSubject()+"'가 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(mContext,"'"+scoreBean.getSubject()+"'이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }//end OnCreate;

    //tabOfNum 설정자
    public void setTabOfNumForList(int tabNum){
        tabOfNum = tabNum;
    }

}//end class
