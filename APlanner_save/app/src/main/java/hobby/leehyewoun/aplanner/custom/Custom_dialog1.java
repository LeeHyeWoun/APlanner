package hobby.leehyewoun.aplanner.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import hobby.leehyewoun.aplanner.R;
import hobby.leehyewoun.aplanner.ScoreActivity;
import hobby.leehyewoun.aplanner.ScoreModifyActivity;
import hobby.leehyewoun.aplanner.bean.SaveBean;
import hobby.leehyewoun.aplanner.bean.ScoreBean;
import hobby.leehyewoun.aplanner.util.PrefUtil;

public class Custom_dialog1 extends Dialog {
    private Context mContext;
    private ViewPager mPager;
    private View customDialog, layout_tab, layout_list;
    private TextView txtTabEdt, txtAddSeasonalSemester,txtAddSemester, txtSubSemester,
            txtListEdt, txtClear, txtSort, txtEditableTrue;

    private int semester;
    private int tabOfNum;
    private String tabName;
    private String KeyName_tabData;

    //생성자
    public Custom_dialog1(Context context, ViewPager pager){
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        mPager = pager;
    }

    //바깥쪽 터치시 다이얼로그 닫기
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.dismiss();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams IpWindow = new WindowManager.LayoutParams();
        IpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;    //Dialog 호출시 배경화면이 검정색으로 바뀌는 것 막기
        IpWindow.dimAmount = 0.5f;  //흐린 정도
        getWindow().setAttributes(IpWindow);
        /**
         * 대화형 모델이 아니기 때문에 아래의 방법이 작동되지 않아
         * 다이얼로그 영역 외부 터치시 dismiss 를 실행하도록 함.
         *         setCanceledOnTouchOutside(true);
         *         setCancelable(true);
         */
        //다이얼로그 영역 외부 터치시 반응을 얻기 위함
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        //다이얼로그 view 설정
        setContentView(R.layout.custom_dialog1);

        //아이디 찾기
        customDialog = findViewById(R.id.customDialog);
        layout_tab = findViewById(R.id.Layout_tab);
        layout_list = findViewById(R.id.Layout_list);
        txtTabEdt = findViewById(R.id.txtTabEdt);
        txtListEdt = findViewById(R.id.txtListEdt);
        txtAddSemester = findViewById(R.id.txtAddSemester);
        txtAddSeasonalSemester = findViewById(R.id.txtAddSeasonalSemester);
        txtSubSemester = findViewById(R.id.txtSubSemester);
        txtClear = findViewById(R.id.txtClear);
        txtSort = findViewById(R.id.txtSort);
        txtEditableTrue = findViewById(R.id.txtEditableTrue);

        //학기 정보 구하기
        semester = PrefUtil.getDataInt(mContext,"semester");
        //탭 위치 구하기
        tabOfNum = mPager.getCurrentItem();

        //초기 설정
        layout_tab.setVisibility(View.GONE);
        layout_list.setVisibility(View.GONE);
        if(semester==1){
            txtSubSemester.setVisibility(View.GONE);
        }

        //layout_tab 안의 버튼의 leftDrawable 설정
        Drawable imgAdd = getContext().getResources().getDrawable( R.drawable.icon_add );
        Drawable imgSub = getContext().getResources().getDrawable( R.drawable.icon_sub );
        imgAdd.setBounds( 0, 0, 32, 32 );   //32x32
        imgSub.setBounds( 0, 0, 32, 7 );    //32x7
        txtSubSemester.setCompoundDrawables(imgSub, null, null, null);
        txtAddSemester.setCompoundDrawables(imgAdd,null,null,null);
        txtAddSeasonalSemester.setCompoundDrawables(imgAdd,null,null,null);

        //선택지 이름 구하기
        String addS = "";
        String addSS = "";
        String subS = (semester/4+1)+"학년 ";
        if(semester%2==0){
            //마지막학기가 계절 학기 일 경우 계절학기 추가 막기
            txtAddSeasonalSemester.setVisibility(View.GONE);
            if(semester%4==2){
                subS += "여름";
            }
            else{
                subS += "겨울";
            }
            addS = ((semester+1)/4+1)+"학년 "+((semester+1)%4/2+1)+"학기";
        }
        else{
            if(semester%4==1){
                addSS = (semester/4+1)+"학년 여름";
            }
            else{
                addSS = (semester/4+1)+"학년 겨울";
            }
            addS = ((semester+2)/4+1)+"학년 "+((semester+2)%4/2+1)+"학기";
            subS +=(semester%4/2+1)+"학기";
        }

        //선택지 이름 설정
        txtSubSemester.setText(subS);
        txtAddSemester.setText(addS);
        txtAddSeasonalSemester.setText(addSS);

        //이벤트
        txtTabEdt.setOnClickListener(clickListener);
        txtListEdt.setOnClickListener(clickListener);
        txtSubSemester.setOnClickListener(clickListener);
        txtAddSemester.setOnClickListener(clickListener);
        txtAddSeasonalSemester.setOnClickListener(clickListener);
        txtClear.setOnClickListener(clickListener);
        txtSort.setOnClickListener(clickListener);
        txtEditableTrue.setOnClickListener(clickListener);
    }
    private void clickEditButton(TextView click){
        if((click.getId()==R.id.txtTabEdt&&layout_tab.getVisibility()==View.VISIBLE)
                ||(click.getId()==R.id.txtListEdt&&layout_list.getVisibility()==View.VISIBLE)){
            layout_tab.setVisibility(View.GONE);
            layout_list.setVisibility(View.GONE);
            txtTabEdt.setCompoundDrawables(null,null,null,null);
            txtListEdt.setCompoundDrawables(null,null,null,null);
            txtTabEdt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_customdialog_button1));
            txtListEdt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_customdialog_button1));
            txtTabEdt.setTextColor(Color.parseColor("#606060"));
            txtListEdt.setTextColor(Color.parseColor("#606060"));
        }
        else {
            txtTabEdt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_customdialog_button2));
            txtListEdt.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_customdialog_button2));
            txtTabEdt.setTextColor(Color.parseColor("#CDEEF3"));
            txtListEdt.setTextColor(Color.parseColor("#CDEEF3"));
            Drawable img = getContext().getResources().getDrawable( R.drawable.icon_this );
            img.setBounds( 0, 0, 24, 32 );

            //하위 버튼 등장 효과
            Animation animation = new AlphaAnimation(0,1);
            animation.setDuration(600);

            if(click.getId()==R.id.txtTabEdt){
                layout_tab.setVisibility(View.VISIBLE);
                layout_list.setVisibility(View.GONE);
                layout_tab.setAnimation(animation);
                txtTabEdt.setCompoundDrawables(img,null,null,null);
                txtListEdt.setCompoundDrawables(null,null,null,null);
            }
            else{
                layout_tab.setVisibility(View.GONE);
                layout_list.setVisibility(View.VISIBLE);
                layout_list.setAnimation(animation);
                txtTabEdt.setCompoundDrawables(null,null,null,null);
                txtListEdt.setCompoundDrawables(img,null,null,null);
            }

        }
    }
    private void listClear(int tabNum){
        Gson gson = new Gson();

        //해당 탭의 리스트에 접근할 키 데이터
        KeyName_tabData = tabNum+"tabScoreData";

        //기존에 저장한 리스트 불러오기
        SaveBean saveBean = new SaveBean();
        String jsonScoreBeanData = PrefUtil.getData(mContext,KeyName_tabData);
        if(jsonScoreBeanData.length()>0){
            saveBean = gson.fromJson(jsonScoreBeanData,SaveBean.class);
        }
        if(saveBean.getScoreListBean()==null){
            saveBean.setScoreListBean(new ArrayList<ScoreBean>());
        }

        //이수한 학점 초기화
        int sumCredit = PrefUtil.getDataInt0(mContext,"sumCredit");
        for(int i=0; i<saveBean.getScoreListBean().size(); i++){
            if(!saveBean.getScoreListBean().get(i).getScore().equals("F")){
                sumCredit-= Integer.parseInt(saveBean.getScoreListBean().get(i).getCredit());
            }
        }
        PrefUtil.setData(mContext,"sumCredit",sumCredit);

        //리스트 초기화
        saveBean.setScoreListBean(new ArrayList<ScoreBean>());

        //리스트 저장하기
        PrefUtil.setData(mContext,KeyName_tabData,gson.toJson(saveBean));
    }
    private TextView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txtTabEdt:
                    clickEditButton(txtTabEdt);
                    break;


                case R.id.txtListEdt:
                    clickEditButton(txtListEdt);
                    break;


                //마지막 학기 삭제하기
                case R.id.txtSubSemester:
                    //선택지 숨기기
                    customDialog.setVisibility(View.INVISIBLE);

                    //마지막 탭으로 이동
                    mPager.setCurrentItem(semester);

                    //삭제되는 탭이름 구하기
                    if(semester%2!=0){
                        tabName = "'" + ((semester-1)/4+1)+"학년 " + ((semester-1)%4/2+1) + "학기'";
                    }
                    else if(semester%4==2){
                        tabName = "'" + ((semester-1)/4+1)+"학년 여름'";
                    }
                    else {
                        tabName = "'" + ((semester-1)/4+1)+"학년 겨울'";
                    }

                    //출력할 메세지 중 탭이름에 색상넣기
                    final SpannableStringBuilder ssb = new SpannableStringBuilder(tabName);
                    if(semester%2!=0){
                        ssb.append("를 삭제하시겠습니까?");
                    }
                    else{
                        ssb.append("을 삭제하시겠습니까?");
                    }
                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 0, tabName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    //최종 확인 다이얼로그
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(ssb);
                    builder.setCancelable(true);
                    builder.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //선택지 다시 보여주기
                            customDialog.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listClear(semester);

                            //학기 삭제
                            //정규학기이며 이전학기에 계절을 듣지 않은 경우
                            if(semester%2!=0 && !PrefUtil.getDataBoolean(mContext,"tab"+(semester-1))){
                                semester-=2;
                            }
                            else{
                                if(semester%2==0){
                                    //계절학기 존재 여부 삭제
                                    PrefUtil.setData(mContext,"tab"+semester,false);
                                }
                                semester--;
                            }
                            PrefUtil.setData(mContext,"semester", semester);

                            //탭을 갱신하기 위해 같은 activity 로 다시 열기
                            mContext.startActivity(new Intent(mContext,ScoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                            //추가 알림 토스트 띄우기
                            ssb.delete(tabName.length(),ssb.length());
                            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#f28eb3")), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            if(semester%2!=0){
                                ssb.append("이 삭제되었습니다.");
                            }
                            else{
                                ssb.append("가 삭제되었습니다.");
                            }
                            Toast.makeText(mContext, ssb,Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("취소", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //선택지 다시 보여주기
                            customDialog.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.show();
                    break;


                //학기 추가하기
                case R.id.txtAddSemester:
                    //학기 추가
                    //계절은 다음학기까 정규학기이고, 정규학기는 다음 정규학기까지 2만큼 남음
                    if(semester%2==0){ semester++; }
                    else{
                        semester+=2;}
                    PrefUtil.setData(mContext,"semester", semester);

                    //탭을 갱신하기 위해 같은 activity 로 다시 열기
                    mContext.startActivity(new Intent(mContext,ScoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    //추가 알림 토스트 띄우기
                    SpannableStringBuilder ssbAS = new SpannableStringBuilder("'"+(semester/4+1)+"학년 "+(semester%4/2+1)+"학기'");
                    ssbAS.setSpan(new ForegroundColorSpan(Color.parseColor("#AEE6CB")), 0, ssbAS.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssbAS.append("가 추가되었습니다.");
                    Toast.makeText(mContext,ssbAS,Toast.LENGTH_LONG).show();
                    break;


                //다음학기로 계절학기 추가하기
                case R.id.txtAddSeasonalSemester:
                    semester++;

                    //계절학기 존재여부 추가
                    PrefUtil.setData(mContext,"tab"+semester,true);

                    //학기 추가
                    PrefUtil.setData(mContext,"semester", semester);

                    //탭을 갱신하기 위해 같은 activity 로 다시 열기
                    mContext.startActivity(new Intent(mContext,ScoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    //추가 알림 토스트 띄우기
                    tabName = "'"+((semester-1)/4+1)+"학년 ";
                    if(semester%4==2){
                        tabName += "여름'";
                    }
                    else{
                        tabName += "겨울'";
                    }
                    SpannableStringBuilder ssbASS = new SpannableStringBuilder(tabName);
                    ssbASS.setSpan(new ForegroundColorSpan(Color.parseColor("#AEE6CB")), 0, ssbASS.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssbASS.append("이 추가되었습니다.");
                    Toast.makeText(mContext,ssbASS,Toast.LENGTH_LONG).show();
                    break;


                //리스트 초기화하기
                case R.id.txtClear:
                    //선택지 숨기기
                    customDialog.setVisibility(View.INVISIBLE);

                    //탭의 위치로 부터 탭의 이름을 구하기 위한 계산
                    int count, i;
                    for(i=0,count=0; i<semester && count<=tabOfNum; i++){
                        //정규 학기
                        if(i%2==0){ count++; }
                        //계절학기
                        else {
                            //기존에 저장한 탭 존재여부 불러오기
                            Boolean tabData = PrefUtil.getDataBoolean(mContext,"tab"+(i+1));
                            if (tabData) { count++; }
                        }
                    }
                    //탭의 이름 설정
                    tabName="'" + (i/4+1) + "학년 ";
                    if(i%2!=0){
                        tabName += (i%4/2+1)+"학기'"; }
                    else if(i%4==2){
                        tabName += "여름'"; }
                    else{
                        tabName += "겨울'"; }

                    final String finalTabName = tabName;

                    //출력할 메세지 중 탭이름에 색상넣기
                    SpannableStringBuilder ssb2 = new SpannableStringBuilder(tabName);
                    if(semester%2!=0){
                        ssb2.append("를 초기화하시겠습니까?");
                    }
                    else{
                        ssb2.append("을 초기화하시겠습니까?");
                    }
                    ssb2.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 0, tabName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    //최종 확인 다이얼로그
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                    builder2.setMessage(ssb2);
                    builder2.setCancelable(true);
                    builder2.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //선택지 다시 보여주기
                            customDialog.setVisibility(View.VISIBLE);
                        }
                    });
                    builder2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //초기화
                            listClear(tabOfNum);

                            //탭을 갱신하기 위해 같은 activity 로 다시 열기
                            PrefUtil.setData(mContext,"startOther",true);
                            PrefUtil.setData(mContext,"tabOfNum",tabOfNum);
                            mContext.startActivity(new Intent(mContext,ScoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                            //추가 알림 토스트 띄우기
                            SpannableStringBuilder ssb2 = new SpannableStringBuilder(finalTabName);
                            if(semester%2!=0){
                                ssb2.append("가 초기화되었습니다.");
                            }
                            else{
                                ssb2.append("이 초기화되었습니다.");
                            }
                            ssb2.setSpan(new ForegroundColorSpan(Color.parseColor("#f28eb3")), 0, finalTabName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(mContext, ssb2,Toast.LENGTH_LONG).show();
                        }
                    });
                    builder2.setNegativeButton("취소", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //선택지 다시 보여주기
                            customDialog.setVisibility(View.VISIBLE);
                        }
                    });
                    builder2.show();

                    break;


                //정렬
                case R.id.txtSort:
                    break;


                //편집하기
                case R.id.txtEditableTrue:
                    //density 구하기
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
                    else if(mPager.getChildCount()-tabOfNum<=j){
                        TabX = (tabOfNum-j)*100;
                    }

                    Intent intent = new Intent(mContext,ScoreModifyActivity.class);
                    intent.putExtra("tabOfNum",tabOfNum);
                    intent.putExtra("tabX",TabX);
                    mContext.startActivity(intent);
                    dismiss();
                    break;
            }
        }
    };
}
