package hobby.leehyewoun.aplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import hobby.leehyewoun.aplanner.adapter.PagerAdapter_Score;
import hobby.leehyewoun.aplanner.adapter.PagerAdapter_ScoreTop;
import hobby.leehyewoun.aplanner.custom.Custom_dialog1;
import hobby.leehyewoun.aplanner.util.PrefUtil;

public class ScoreActivity extends AppCompatActivity {
    private ImageView imgBack, imgTool,imgAdd;
    private TabLayout mTabLayout;
    private ViewPager TopPager,Pager;
    private PagerAdapter_Score mPagerAdapter;
    private PagerAdapter_ScoreTop mTopPagerAdapter;


    private int semester;   //현재학기
    private int oldTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //아이디 찾기
        imgBack = findViewById(R.id.imgBack);
        imgTool = findViewById(R.id.imgTool);
        imgAdd = findViewById(R.id.imgAdd);
        mTabLayout = findViewById(R.id.tabLayout);
        TopPager = findViewById(R.id.topPager);
        Pager = findViewById(R.id.pager);

        //추가버튼의 이미지 설정
        Bitmap bmp=BitmapFactory.decodeResource(getResources(),R.drawable.icon_add);//image is your image
        bmp=Bitmap.createScaledBitmap(bmp, 40,40, true);
        imgAdd.setImageBitmap(bmp);


        //현재 학기에 대한 데이터 받기
        semester = PrefUtil.getDataInt(ScoreActivity.this,"semester");
        oldTab = semester;

        //탭을 동적으로 추가하기
        for(int i=0; i<semester; i++){
            String EditList_key, tabName;
            //정규 학기
            if(i%2==0){
                tabName = (i/4+1)+"학년 "+(i%4/2+1)+"학기";
                EditList_key = i+"EditList";
                mTabLayout.addTab(mTabLayout.newTab().setText(tabName));
                PrefUtil.setData(ScoreActivity.this,EditList_key,-1);
            }
            //계절학기
            else {
                //기존에 저장한 탭 존재여부 불러오기
                Boolean tabData = PrefUtil.getDataBoolean(ScoreActivity.this,"tab"+(i+1));

                if (tabData) {
                    //여름 계절학기
                    if (i % 4 == 1) {
                        tabName = (i/4+1) + "학년 여름";
                        EditList_key = i+"Summer"+"EditList";
                    }
                    //여름 계절학기
                    else {
                        tabName = (i/4+1) + "학년 겨울";
                        EditList_key = i+"Winter"+"EditList";
                    }
                    mTabLayout.addTab(mTabLayout.newTab().setText(tabName));
                    PrefUtil.setData(ScoreActivity.this, EditList_key, -1);
                }
            }
        }

        //viewPager 는 adapter 를 통해서 page(fragment)를 관리
        mPagerAdapter=new PagerAdapter_Score(getSupportFragmentManager(),mTabLayout.getTabCount());
        Pager.setAdapter(mPagerAdapter);

        //현재 학기를 시작으로 둔다.
        if(!PrefUtil.getDataBoolean(ScoreActivity.this,"startOther")){
            Pager.setCurrentItem(semester);
        }
        else{
            Pager.setCurrentItem(PrefUtil.getDataInt0(ScoreActivity.this,"tabOfNum"));
            PrefUtil.setData(ScoreActivity.this,"tabOfNum",null);
            PrefUtil.setData(ScoreActivity.this,"startOther",null);
        }

        //viewPager 는 adapter 를 통해서 page(fragment)를 관리
        //Pager.getCurrentItem()는 학기 시작 설정 후로 해야한다.
        mTopPagerAdapter= new PagerAdapter_ScoreTop(getSupportFragmentManager());
        mTopPagerAdapter.setTabOfNum(Pager.getCurrentItem());
        TopPager.setAdapter(mTopPagerAdapter);


        //이벤트-----------------------------------------------------------------------------------
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //custom Dialog
                Custom_dialog1 dialog = new Custom_dialog1(ScoreActivity.this, Pager);
                dialog.show();
            }
        });
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //density 구하기
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float density = displayMetrics.density; //px = dp*density;

                //화면 크기 구하기
                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;

                int TabX = 0;
                int j=1;
                while (j*100*density*2 + 100*density < width){
                    j++;
                }
                if(Pager.getCurrentItem()+1<=j){
                    TabX = -1 * ((j-Pager.getCurrentItem())*100);
                }
                else if(semester-Pager.getCurrentItem()<=j){
                    TabX = (Pager.getCurrentItem()-j)*100;
                }

                PrefUtil.setData(ScoreActivity.this,Pager.getCurrentItem()+"EditList",-1);
                Intent intent = new Intent(ScoreActivity.this,ScoreModifyActivity.class);
                intent.putExtra("tabOfNum",Pager.getCurrentItem());
                intent.putExtra("tabX",TabX);
                startActivity(intent);
            }
        });
        //TabLayout 과 ViewPager 를 서로 연결
        //ViewPager 가 움직였을 때, 탭 변경
        Pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //ScoreActivity 상단의 viewpager 를 갱신합니다.
                mTopPagerAdapter.setTabOfNum(position);
                mTopPagerAdapter.notifyDataSetChanged();
                if(PrefUtil.getDataInt(ScoreActivity.this,oldTab+"EditList")>-1){
                    PrefUtil.setData(ScoreActivity.this, oldTab+"EditList",-1);
                    mPagerAdapter.listNotifyDataSetChanged();
                }
                oldTab = position;
            }
        });
        //TabLayout 이 움직였을 때, ViewPager 변경
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //현재 사용자가 선택한 탭의 이벤트를 실행
                Pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }//end OnCreate

    @Override
    protected void onResume() {
        super.onResume();
        //초기화면 등장 효과
        imgBack.setVisibility(View.GONE);
        imgTool.setVisibility(View.GONE);
        imgAdd.setVisibility(View.GONE);
        Animation aniA = new AlphaAnimation(0,1);
        aniA.setDuration(400);
        imgBack.setAnimation(aniA);
        imgTool.setAnimation(aniA);
        imgAdd.setAnimation(aniA);
        imgBack.setVisibility(View.VISIBLE);
        imgTool.setVisibility(View.VISIBLE);
        imgAdd.setVisibility(View.VISIBLE);

        mTopPagerAdapter.notifyDataSetChanged();
    }//end OnResume

    @Override
    public void onBackPressed() {
        //편집/삭제가 열려있으면 닫기
        if(PrefUtil.getDataInt(ScoreActivity.this,Pager.getCurrentItem()+"EditList")>-1){
            PrefUtil.setData(ScoreActivity.this, Pager.getCurrentItem()+"EditList",-1);
            mPagerAdapter.listNotifyDataSetChanged();
        }
        else{
            super.onBackPressed();
        }
    }

}//end Class
