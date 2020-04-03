package hobby.leehyewoun.aplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import hobby.leehyewoun.aplanner.adapter.ListAdapter_ScoreModify;
import hobby.leehyewoun.aplanner.bean.SaveBean;
import hobby.leehyewoun.aplanner.bean.ScoreBean;
import hobby.leehyewoun.aplanner.util.PrefUtil;

public class ScoreModifyActivity extends AppCompatActivity {
    //xml
    private ImageView imgBack, imgSave;
    private ProgressBar creditsEarned;
    private ListView list;
    private TextView txtTitle,txtTabName, txtCreditsEarned, txtCategory, txtScore, txtCredit;
    private EditText edtSubject;
    private LinearLayout category, score, credit;
    private NumberPicker npCategory,npScore, npCredit;
    private Button btnNext1, btnNext2, btnNext3;

    //키보드 객체
    private InputMethodManager imm;

    //어답터
    private ListAdapter_ScoreModify listAdapter;

    //Bean
    private SaveBean saveBean;

    //넘버피커 안의 데이터
    private String[] sCategory = new String[]{"부전선택","부전필수","복전선택","복전필수","전공선택","전공필수","교양선택","교양필수","일반선택","기타"};
    private String[] sScore = new String[]{"A+","A","B+","B","C+","C","D+","D","F","P"};
    private String[] sCredit = new String[]{"6","5","4","3","2","1","0"};

    //변수
    private  int tabOfNum;
    private String tabName;
    private String KeyName_tabData;
    private int listItemHeight;
    private int sumCredit;
    private float tabX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//=========================================
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoremodify);

        //아이디 찾기---------------------------------------------------------
        imgBack = findViewById(R.id.imgBack);
        txtTitle = findViewById(R.id.txtTitle);
        imgSave = findViewById(R.id.imgSave);
        txtCreditsEarned = findViewById(R.id.txtCreditsEarned);
        creditsEarned = findViewById(R.id.creditsEarned);
        txtTabName = findViewById(R.id.txtTabName);
        list = findViewById(R.id.list);
        txtCategory = findViewById(R.id.txtCategory);
        txtScore = findViewById(R.id.txtScore);
        txtCredit = findViewById(R.id.txtCredit);
        edtSubject = findViewById(R.id.edtSubject);
        category = findViewById(R.id.category);
        score = findViewById(R.id.score);
        credit = findViewById(R.id.credit);
        npCategory = findViewById(R.id.npCategory);
        npScore = findViewById(R.id.npScore);
        npCredit = findViewById(R.id.npCredit);
        btnNext1 = findViewById(R.id.btnNext1);
        btnNext2 = findViewById(R.id.btnNext2);
        btnNext3 = findViewById(R.id.btnNext3);

        //키보드 객체
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //이수학점 설정
        sumCredit = PrefUtil.getDataInt0(ScoreModifyActivity.this,"sumCredit");
        txtCreditsEarned.setText(String.valueOf(sumCredit));
        creditsEarned.setProgress(sumCredit);

        //데이터 수신
        Intent intent = getIntent();
        tabOfNum = intent.getExtras().getInt("tabOfNum");
        tabX = intent.getExtras().getInt("tabX");

        //선택된 탭의 이름 설정
        setTabName();

        //탭 데이터의 키값 설정
        KeyName_tabData = tabOfNum+"tabScoreData";

        //초기화
        //초기화면 설정 학기 정보 텍스트 애니메이션 탭 전의 위치에서 중앙으로
        Animation aniT = new TranslateAnimation(tabX, 0, 0, 0);
        aniT.setDuration(600);
        aniT.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim. accelerate_decelerate_interpolator));
        txtTabName.startAnimation(aniT);
        //초기화면 설정 등장 효과
        findViewById(R.id.edtLayout).setVisibility(View.GONE);
        imgSave.setVisibility(View.GONE);
        Animation aniA = new AlphaAnimation(0,1);
        aniA.setDuration(400);
        findViewById(R.id.edtLayout).setAnimation(aniA);
        findViewById(R.id.edtLayout).setVisibility(View.VISIBLE);
        imgSave.setAnimation(aniA);
        imgSave.setVisibility(View.VISIBLE);

        category.setVisibility(View.GONE);
        //넘버피커 : category 설정
        npCategory.setMinValue(0);
        npCategory.setMaxValue(sCategory.length-1);
        npCategory.setValue(5);
        npCategory.setWrapSelectorWheel(false);
        npCategory.setDisplayedValues(sCategory);
        npCategory.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtCategory.setText(sCategory[newVal]);
            }
        });

        score.setVisibility(View.GONE);
        //넘버피커 : score 설정
        npScore.setMinValue(0);
        npScore.setMaxValue(sScore.length-1);
        npScore.setValue(0);
        npScore.setWrapSelectorWheel(false);
        npScore.setDisplayedValues(sScore);
        npScore.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtScore.setText(sScore[newVal]);
            }
        });

        credit.setVisibility(View.GONE);
        //넘버피커 : credit 설정
        npCredit.setMinValue(0);
        npCredit.setMaxValue(sCredit.length-1);
        npCredit.setValue(3);
        npCredit.setWrapSelectorWheel(false);
        npCredit.setDisplayedValues(sCredit);
        npCredit.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtCredit.setText(sCredit[newVal]);
            }
        });

        //이벤트-----------------------------------------------------------------------------------
        //titleBar
        imgBack.setOnClickListener(titleBar);
        imgSave.setOnClickListener(titleBar);
        //input_넘버피커
        txtCategory.setOnClickListener(txtButton);
        txtScore.setOnClickListener(txtButton);
        txtCredit.setOnClickListener(txtButton);
        //input_키보드
        edtSubject.setOnFocusChangeListener(edtFocus);
        edtSubject.setOnEditorActionListener(complete);
        //버튼
        btnNext1.setOnClickListener(next);
        btnNext2.setOnClickListener(next);
        btnNext3.setOnClickListener(next);

    }//end onCreate===============================================================================

    @Override
    protected void onResume() {//=================================================================
        super.onResume();

        saveBean= new SaveBean();

        //기존에 저장한 리스트 불러오기
        String jsonScoreBeanData = PrefUtil.getData(ScoreModifyActivity.this,KeyName_tabData);
        if(jsonScoreBeanData.length()>0){
            Gson gson = new Gson();
            saveBean = gson.fromJson(jsonScoreBeanData,SaveBean.class);
        }
        if(saveBean.getScoreListBean()==null){
            saveBean.setScoreListBean(new ArrayList<ScoreBean>());
        }
        listAdapter = new ListAdapter_ScoreModify(ScoreModifyActivity.this,saveBean.getScoreListBean(),txtTitle,imgSave,
                tabOfNum,txtCreditsEarned, creditsEarned, txtCategory,txtScore,txtCredit,edtSubject);
        list.setAdapter(listAdapter);

        //초기화면 설정 리스트에 아무것도 존재하지 않을 경우 바로 입력 모드
        if(saveBean.getScoreListBean().size()==0) {
            //카테고리 선택시키기
            txtCategory.setText(sCategory[npCategory.getValue()]);
            npCategory.setValue(5);
            txtCategory.setSelected(true);
            //넘버피커 등장 효과
            Animation animation = new AlphaAnimation(0, 1);
            animation.setDuration(400);
            category.setAnimation(animation);
            category.setVisibility(View.VISIBLE);
            btnNext1.setText("다음");
        }
        //초기화면 설정 수정하기로 들어온 경우
        else if(PrefUtil.getDataInt(ScoreModifyActivity.this,tabOfNum+"EditList")>-1){
            int position = PrefUtil.getDataInt(ScoreModifyActivity.this,tabOfNum+"EditList");
            //데이터 설정
            ScoreBean sb = saveBean.getScoreListBean().get(position);
            txtCategory.setText(sb.getCategory());
            txtScore.setText(sb.getScore());
            txtCredit.setText(sb.getCredit());
            edtSubject.setText(sb.getSubject());
            imgSave.setImageResource(R.drawable.icon_ok);
            //타이틀 변경
            SpannableStringBuilder ssb = new SpannableStringBuilder("'"+sb.getSubject());
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtTitle.setText(ssb.append("' 수정"));
            //넘버피커 띄우기
            txtCategory.setSelected(true);
            category.setVisibility(View.VISIBLE);
            btnNext1.setText("완료");
            int i=0;
            while (!sCategory[i].equals(txtCategory.getText().toString())){
                i++;
            }
            npCategory.setValue(i);
        }

        //density 구하기
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density; //px = dp*density;

        //리스트의 높이
        listItemHeight = (int) (saveBean.getScoreListBean().size()*30*density*2);

    }//end onResume===============================================================================

    //이벤트 리스너==================================================================================
    private ImageView.OnClickListener titleBar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgBack:
                    if(!txtCategory.getText().toString().equals("")
                            ||!txtScore.getText().toString().equals("")
                            ||!txtCredit.getText().toString().equals("")
                            ||!edtSubject.getText().toString().equals("")){
                        new AlertDialog.Builder(ScoreModifyActivity.this)
                                .setMessage("작성 중이던 내용이 있습니다.\n그래도 나가시겠습니까?")
                                .setCancelable(true)
                                .setNegativeButton("취소", null)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PrefUtil.setData(ScoreModifyActivity.this,tabOfNum+"EditList",-1);
                                        finish();
                                    }
                                })
                                .show();
                    }else {
                        finish();
                    }
                    break;

                case R.id.imgSave:
                    if(txtCategory.getText().toString().equals("")
                            &&txtScore.getText().toString().equals("")
                            &&txtCredit.getText().toString().equals("")
                            &&edtSubject.getText().toString().equals("")){
                        Toast.makeText(ScoreModifyActivity.this,"추가할 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else if(txtCategory.getText().toString().equals("")
                            ||txtScore.getText().toString().equals("")
                            ||txtCredit.getText().toString().equals("")
                            ||edtSubject.getText().toString().equals("")){
                        Toast.makeText(ScoreModifyActivity.this,"내용을 전부 채워주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        int selectedListData =PrefUtil.getDataInt(ScoreModifyActivity.this,tabOfNum+"EditList");
                        Gson gson = new Gson();
                        ScoreBean scoreBean = new ScoreBean();
                        scoreBean.setCategory(txtCategory.getText().toString());
                        scoreBean.setScore(txtScore.getText().toString());
                        scoreBean.setCredit(txtCredit.getText().toString());
                        scoreBean.setSubject(edtSubject.getText().toString());
                        if(selectedListData == -1){
                            String data="";
                            for(int i = 0; i<saveBean.getScoreListBean().size(); i++){
                                data += saveBean.getScoreListBean().get(i).getSubject();
                            }
                            if(!data.contains(scoreBean.getSubject())){
                                //저장하기...리스트 마지막에 추가
                                saveBean.getScoreListBean().add(scoreBean);
                                PrefUtil.setData(ScoreModifyActivity.this,KeyName_tabData,gson.toJson(saveBean));

                                //이번학기 이수한 학점 저장
                                if(!scoreBean.getScore().equals("F")){
                                    sumCredit = PrefUtil.getDataInt0(ScoreModifyActivity.this,"sumCredit");
                                    ProgressBarAnimation anim = new ProgressBarAnimation(creditsEarned, sumCredit, sumCredit+Integer.parseInt(scoreBean.getCredit()));
                                    anim.setDuration(300);
                                    creditsEarned.setAnimation(anim);

                                    sumCredit += Integer.parseInt(scoreBean.getCredit());
                                    PrefUtil.setData(ScoreModifyActivity.this,"sumCredit",sumCredit);
                                    txtCreditsEarned.setText(String.valueOf(sumCredit));
                                }

                                //안내
                                Toast.makeText(ScoreModifyActivity.this,"추가 완료",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //안내
                                Toast.makeText(ScoreModifyActivity.this,"이미 저장되어 있습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        //수정
                        else {
                            if(saveBean.getScoreListBean().get(selectedListData).getCategory().equals(txtCategory.getText().toString())
                                    &&saveBean.getScoreListBean().get(selectedListData).getScore().equals(txtScore.getText().toString())
                                    &&saveBean.getScoreListBean().get(selectedListData).getCredit().equals(txtCredit.getText().toString())
                                    &&saveBean.getScoreListBean().get(selectedListData).getSubject().equals(edtSubject.getText().toString())){
                                Toast.makeText(ScoreModifyActivity.this,"수정된 내용이 없습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //저장 아이콘 변경
                                imgSave.setImageResource(R.drawable.icon_add);

                                //리스트 체크 해제
                                PrefUtil.setData(ScoreModifyActivity.this,tabOfNum+"EditList",-1);

                                //이수학점 수정
                                sumCredit = PrefUtil.getDataInt0(ScoreModifyActivity.this,"sumCredit");
                                int oldSumCredit = sumCredit;
                                if(!saveBean.getScoreListBean().get(selectedListData).getScore().equals("F")){
                                    sumCredit -= Integer.parseInt(saveBean.getScoreListBean().get(selectedListData).getCredit());
                                }
                                if(!scoreBean.getScore().equals("F")){
                                    sumCredit += Integer.parseInt(scoreBean.getCredit());
                                }
                                PrefUtil.setData(ScoreModifyActivity.this,"sumCredit",sumCredit);
                                ProgressBarAnimation anim = new ProgressBarAnimation(creditsEarned, oldSumCredit, sumCredit);
                                anim.setDuration(300);
                                creditsEarned.setAnimation(anim);
                                txtCreditsEarned.setText(String.valueOf(sumCredit));

                                //타이틀 변경
                                txtTitle.setText("추가하기");

                                //수정하기...이전 데이터를 삭제하고 리스트에 추가하기
                                saveBean.getScoreListBean().remove(selectedListData);
                                saveBean.getScoreListBean().add(selectedListData,scoreBean);
                                PrefUtil.setData(ScoreModifyActivity.this,KeyName_tabData,gson.toJson(saveBean));

                                //안내
                                Toast.makeText(ScoreModifyActivity.this,"수정 완료",Toast.LENGTH_SHORT).show();
                            }
                        }
                        //리스트 갱신
                        listAdapter.notifyDataSetChanged();
                        //입력란 초기화
                        resetEdtLayout();
                        //선택 초기화
                        PrefUtil.setData(ScoreModifyActivity.this,tabOfNum+"EditList",-1);
                    }
                    break;
            }
        }
    };//end ImageView.OnClickListener titleBar
    private TextView.OnClickListener txtButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //버튼 등장 효과
            Animation animation = new AlphaAnimation(0,1);
            animation.setDuration(400);
            switch (v.getId()){
                case R.id.txtCategory:
                    if(txtCategory.getText().toString().equals("")){
                        txtCategory.setText(sCategory[npCategory.getValue()]);
                        npCategory.setValue(5);
                    }
                    else {
                        int i=0;
                        while (!sCategory[i].equals(txtCategory.getText().toString())){
                            i++;
                        }
                        npCategory.setValue(i);
                    }
                    txtScore.setSelected(false);
                    txtCredit.setSelected(false);
                    score.setVisibility(View.GONE);
                    credit.setVisibility(View.GONE);
                    //보이기
                    if(category.getVisibility()!=View.VISIBLE){
                        txtCategory.setSelected(true);
                        category.setAnimation(animation);
                        category.setVisibility(View.VISIBLE);
                        if(txtScore.getText().toString().equals("")){
                            btnNext1.setText("다음");
                        }
                        else{
                            btnNext1.setText("완료");
                        }
                    }
                    //다시 닫기
                    else {
                        txtCategory.setSelected(false);
                        category.setVisibility(View.GONE);
                    }
                    break;

                case R.id.txtScore:
                    if(txtScore.getText().toString().equals("")){
                        txtScore.setText(sScore[npScore.getValue()]);
                        npScore.setValue(0);
                    }
                    else {
                        int i=0;
                        while (!sScore[i].equals(txtScore.getText().toString())){
                            i++;
                        }
                        npScore.setValue(i);
                    }
                    txtCategory.setSelected(false);
                    txtCredit.setSelected(false);
                    category.setVisibility(View.GONE);
                    credit.setVisibility(View.GONE);
                    //보이기
                    if(score.getVisibility()!=View.VISIBLE){
                        txtScore.setSelected(true);
                        score.setAnimation(animation);
                        score.setVisibility(View.VISIBLE);
                        if(txtCredit.getText().toString().equals("")){
                            btnNext2.setText("다음");
                        }
                        else{
                            btnNext2.setText("완료");
                        }
                    }
                    //다시닫기
                    else{
                        txtScore.setSelected(false);
                        score.setVisibility(View.GONE);
                    }
                    break;

                case R.id.txtCredit:
                    if(txtCredit.getText().toString().equals("")){
                        txtCredit.setText(sCredit[npCredit.getValue()]);
                        npCredit.setValue(3);
                    }
                    else{
                        int i=0;
                        while (!sCredit[i].equals(txtCredit.getText().toString())){
                            i++;
                        }
                        npCredit.setValue(i);
                    }
                    txtCategory.setSelected(false);
                    txtScore.setSelected(false);
                    category.setVisibility(View.GONE);
                    score.setVisibility(View.GONE);
                    //보이기
                    if(credit.getVisibility()!=View.VISIBLE){
                        txtCredit.setSelected(true);
                        credit.setAnimation(animation);
                        credit.setVisibility(View.VISIBLE);
                        if(edtSubject.getText().toString().equals("")){
                            btnNext3.setText("다음");
                        }
                        else{
                            btnNext3.setText("완료");
                        }
                    }
                    //다시닫기
                    else{
                        txtCredit.setSelected(false);
                        credit.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };//end TextView.OnClickListener txtButton
    private EditText.OnFocusChangeListener edtFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                //포커스를 잃을 경우 키보드 닫기
                imm.hideSoftInputFromWindow(edtSubject.getWindowToken(), 0);
            }
            else {
                txtCategory.setSelected(false);
                txtScore.setSelected(false);
                txtCredit.setSelected(false);
                category.setVisibility(View.GONE);
                score.setVisibility(View.GONE);
                credit.setVisibility(View.GONE);
                imm.showSoftInput(edtSubject,0);
            }
        }
    };//end EditText.OnFocusChangeListener edtFocus
    private EditText.OnEditorActionListener complete = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(!txtCategory.getText().toString().equals("")
                    &&!txtScore.getText().toString().equals("")
                    &&!txtCredit.getText().toString().equals("")
                    &&!edtSubject.getText().toString().equals("")){
                int selectedListData =PrefUtil.getDataInt(ScoreModifyActivity.this,tabOfNum+"EditList");
                Gson gson = new Gson();
                ScoreBean scoreBean = new ScoreBean();
                scoreBean.setCategory(txtCategory.getText().toString());
                scoreBean.setScore(txtScore.getText().toString());
                scoreBean.setCredit(txtCredit.getText().toString());
                scoreBean.setSubject(edtSubject.getText().toString());
                if(selectedListData == -1){
                    String data = gson.toJson(saveBean);
                    if(!data.contains(scoreBean.getSubject())){
                        //저장하기...리스트 마지막에 추가
                        saveBean.getScoreListBean().add(scoreBean);
                        PrefUtil.setData(ScoreModifyActivity.this,KeyName_tabData,gson.toJson(saveBean));

                        //이번학기 이수한 학점 저장
                        if(!scoreBean.getScore().equals("F")){
                            sumCredit = PrefUtil.getDataInt0(ScoreModifyActivity.this,"sumCredit");
                            ProgressBarAnimation anim = new ProgressBarAnimation(creditsEarned, sumCredit, sumCredit+Integer.parseInt(scoreBean.getCredit()));
                            anim.setDuration(300);
                            creditsEarned.setAnimation(anim);

                            sumCredit += Integer.parseInt(scoreBean.getCredit());
                            PrefUtil.setData(ScoreModifyActivity.this,"sumCredit",sumCredit);
                            txtCreditsEarned.setText(String.valueOf(sumCredit));
                        }

                        //안내
                        Toast.makeText(ScoreModifyActivity.this,"추가 완료",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //안내
                        Toast.makeText(ScoreModifyActivity.this,"해당 학기에 이미 저장되어 있습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //저장 아이콘 변경
                    imgSave.setImageResource(R.drawable.icon_add);

                    //리스트 체크 해제
                    PrefUtil.setData(ScoreModifyActivity.this,tabOfNum+"EditList",-1);

                    //이수학점 수정
                    sumCredit = PrefUtil.getDataInt0(ScoreModifyActivity.this,"sumCredit");
                    int oldSumCredit = sumCredit;
                    if(!saveBean.getScoreListBean().get(selectedListData).getScore().equals("F")){
                        sumCredit -= Integer.parseInt(saveBean.getScoreListBean().get(selectedListData).getCredit());
                    }
                    if(!scoreBean.getScore().equals("F")){
                        sumCredit += Integer.parseInt(scoreBean.getCredit());
                    }
                    PrefUtil.setData(ScoreModifyActivity.this,"sumCredit",sumCredit);
                    ProgressBarAnimation anim = new ProgressBarAnimation(creditsEarned, oldSumCredit, sumCredit);
                    anim.setDuration(300);
                    creditsEarned.setAnimation(anim);
                    txtCreditsEarned.setText(String.valueOf(sumCredit));

                    //타이틀 변경
                    txtTitle.setText("추가하기");

                    //수정하기...이전 데이터를 삭제하고 리스트에 추가하기
                    saveBean.getScoreListBean().remove(selectedListData);
                    saveBean.getScoreListBean().add(selectedListData,scoreBean);
                    PrefUtil.setData(ScoreModifyActivity.this,KeyName_tabData,gson.toJson(saveBean));

                    //안내
                    Toast.makeText(ScoreModifyActivity.this,"수정 완료",Toast.LENGTH_SHORT).show();
                }
                //리스트 갱신
                listAdapter.notifyDataSetChanged();
                //입력란 초기화
                resetEdtLayout();
            }
            edtSubject.clearFocus();
            return true;
        }
    };//end EditText.OnEditorActionListener complete
    private Button.OnClickListener next = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnNext1:
                    //숨기기
                    category.setVisibility(View.GONE);
                    txtCategory.setSelected(false);
                    //보이기
                    if(txtScore.getText().toString().equals("")){
                        npScore.setValue(0);
                        //버튼 등장 효과
                        Animation animation = new AlphaAnimation(0,1);
                        animation.setDuration(400);
                        score.setAnimation(animation);
                        score.setVisibility(View.VISIBLE);
                        txtScore.setSelected(true);
                        txtScore.setText(sScore[npScore.getValue()]);
                    }
                    break;

                case R.id.btnNext2:
                    score.setVisibility(View.GONE);
                    txtScore.setSelected(false);
                    if(txtCredit.getText().toString().equals("")){
                        npCredit.setValue(3);
                        //버튼 등장 효과
                        Animation animation = new AlphaAnimation(0,1);
                        animation.setDuration(400);
                        credit.setAnimation(animation);
                        credit.setVisibility(View.VISIBLE);
                        txtCredit.setSelected(true);
                        txtCredit.setText(sCredit[npCredit.getValue()]);
                    }
                    break;

                case R.id.btnNext3:
                    credit.setVisibility(View.GONE);
                    txtCredit.setSelected(false);
                    if(edtSubject.getText().toString().equals("")){
                        edtSubject.requestFocus();
                    }
                    break;
            }
        }
    };//end Button.OnClickListener next

    //일반 함수=====================================================================================
    private void setTabName(){
        //마지막 학기에 대한 데이터 받기
        int semester = PrefUtil.getDataInt(ScoreModifyActivity.this,"semester");
        //탭이름 구하기
        //탭의 위치로 부터 탭의 이름을 구하기 위한 계산
        int count, i;
        for(i=0,count=0; i<semester && count<=tabOfNum; i++){
            //정규 학기
            if(i%2==0){ count++; }
            //계절학기
            else {
                //기존에 저장한 탭 존재여부 불러오기
                Boolean tabData = PrefUtil.getDataBoolean(ScoreModifyActivity.this,"tab"+(i+1));
                if (tabData) { count++; }
            }
        }
        //탭의 이름 설정
        tabName=(i/4+1) + "학년 ";
        if(i%2!=0){
            tabName += (i%4/2+1)+"학기"; }
        else if(i%4==2){
            tabName += "여름"; }
        else{
            tabName += "겨울"; }

        txtTabName.setText(tabName);
    }
    private void resetEdtLayout(){
        txtCategory.setText("");
        txtScore.setText("");
        txtCredit.setText("");
        edtSubject.setText("");
    }

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

    //오버라이드====================================================================================
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            //범위 외 터치 시
            // 카테고리 닫기
            if(category.getVisibility()==View.VISIBLE){
                Rect r = new Rect();
                findViewById(R.id.edtLayout).getGlobalVisibleRect(r);
                int rawX = (int)ev.getRawX();
                int rawY = (int)ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    txtCategory.setSelected(false);
                    category.setVisibility(View.GONE);
                }
            }
            //점수 닫기
            else if (score.getVisibility()==View.VISIBLE){
                Rect r = new Rect();
                findViewById(R.id.edtLayout).getGlobalVisibleRect(r);
                int rawX = (int)ev.getRawX();
                int rawY = (int)ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    txtScore.setSelected(false);
                    score.setVisibility(View.GONE);
                }
            }
            //이수학점 닫기
            else if (credit.getVisibility()==View.VISIBLE){
                Rect r = new Rect();
                findViewById(R.id.edtLayout).getGlobalVisibleRect(r);
                int rawX = (int)ev.getRawX();
                int rawY = (int)ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    txtCredit.setSelected(false);
                    credit.setVisibility(View.GONE);
                }
            }
            //키보드 닫기
            else if (view instanceof EditText) {
                Rect r = new Rect();
                edtSubject.getGlobalVisibleRect(r);
                int rawX = (int)ev.getRawX();
                int rawY = (int)ev.getRawY();
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
            //편집 취소
            else if(PrefUtil.getDataInt(ScoreModifyActivity.this,tabOfNum+"EditList")>-1) {
                Rect r1 = new Rect(list.getLeft(),list.getTop(),list.getRight(),list.getTop()+listItemHeight);
                Rect r2 = new Rect();
                Rect r3 = new Rect();
                Rect r4 = new Rect();
                findViewById(R.id.edtLayout).getGlobalVisibleRect(r2);
                imgBack.getGlobalVisibleRect(r3);
                imgSave.getGlobalVisibleRect(r4);
                int rawX = (int)ev.getRawX();
                int rawY = (int)ev.getRawY();
                if (!r1.contains(rawX, rawY)&&!r2.contains(rawX,rawY)&&!r3.contains(rawX,rawY)&&!r4.contains(rawX,rawY)) {
                    if(!txtCategory.getText().equals("")
                            &&!txtScore.getText().equals("")
                            &&!txtCredit.getText().toString().equals("")
                            &&!edtSubject.getText().toString().equals("")){
                        int tabOfNumEditList = PrefUtil.getDataInt(ScoreModifyActivity.this,tabOfNum+"EditList");
                        String oldListCategory = saveBean.getScoreListBean().get(tabOfNumEditList).getCategory();
                        String oldListScore = saveBean.getScoreListBean().get(tabOfNumEditList).getScore();
                        String oldListCredit = saveBean.getScoreListBean().get(tabOfNumEditList).getCredit();
                        String oldListSubject = saveBean.getScoreListBean().get(tabOfNumEditList).getSubject();
                        if(!oldListCategory.equals(txtCategory.getText().toString())
                                || !oldListScore.equals(txtScore.getText().toString())
                                || !oldListCredit.equals(txtCredit.getText().toString())
                                || !oldListSubject.equals(edtSubject.getText().toString())){
                            //다이얼로그
                            new AlertDialog.Builder(ScoreModifyActivity.this)
                                    .setMessage("수정 중이던 내용이 있습니다.\n그래도 취소하시겠습니까?")
                                    .setCancelable(true)
                                    .setNegativeButton("취소", null)
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //타이틀 변경
                                            txtTitle.setText("추가하기");
                                            //저장 아이콘 변경
                                            imgSave.setImageResource(R.drawable.icon_add);
                                            //리스트 체크 해제
                                            PrefUtil.setData(ScoreModifyActivity.this,tabOfNum+"EditList",-1);
                                            listAdapter.notifyDataSetChanged();
                                            //입력란 초기화
                                            resetEdtLayout();
                                            //안내
                                            Toast.makeText(ScoreModifyActivity.this,"수정 취소",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            //타이틀 변경
                            txtTitle.setText("추가하기");
                            //저장 아이콘 변경
                            imgSave.setImageResource(R.drawable.icon_add);
                            //리스트 체크 해제
                            PrefUtil.setData(ScoreModifyActivity.this,tabOfNum+"EditList",-1);
                            listAdapter.notifyDataSetChanged();
                            //입력란 초기화
                            resetEdtLayout();
                            //안내
                            Toast.makeText(ScoreModifyActivity.this,"수정 취소",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void onBackPressed() {
        if(category.getVisibility()==View.VISIBLE){
            txtCategory.setSelected(false);
            category.setVisibility(View.GONE);
        }
        else if(score.getVisibility()==View.VISIBLE){
            txtScore.setSelected(false);
            score.setVisibility(View.GONE);
        }
        else if (credit.getVisibility()==View.VISIBLE){
            txtCredit.setSelected(false);
            credit.setVisibility(View.GONE);
        }
        else if(!txtCategory.getText().toString().equals("")
                ||!txtScore.getText().toString().equals("")
                ||!txtCredit.getText().toString().equals("")
                ||!edtSubject.getText().toString().equals("")){
            txtCategory.setText("");
            txtScore.setText("");
            txtCredit.setText("");
            edtSubject.setText("");
            PrefUtil.setData(ScoreModifyActivity.this,tabOfNum+"EditList",-1);
            listAdapter.notifyDataSetChanged();
            txtTitle.setText("추가하기");
        }else {
            super.onBackPressed();
        }
    }
}
