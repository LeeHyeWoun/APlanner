package hobby.leehyewoun.aplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //뒤로가기 관련 변수
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //아이디 찾기
        Button btnCalculator = findViewById(R.id.btnCalculator);
        Button btnManagement = findViewById(R.id.btnManagement);
        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnEnrolment = findViewById(R.id.btnEnrolment);

        //이벤트
        btnCalculator.setOnClickListener(clickListener);
        btnManagement.setOnClickListener(clickListener);
        btnSchedule.setOnClickListener(clickListener);
        btnEnrolment.setOnClickListener(clickListener);
    }//end onCreate

    @Override
    protected void onResume() {
        super.onResume();

        //초기화면 등장효과
        Animation aniA = new AlphaAnimation(0,1);
        aniA.setDuration(400);
        findViewById(R.id.imgTool).setVisibility(View.GONE);
        findViewById(R.id.imgTool).setAnimation(aniA);
        findViewById(R.id.imgTool).setVisibility(View.VISIBLE);
    }

    //클릭리스너
    private Button.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnCalculator:
                    Intent i = new Intent(MainActivity.this,ScoreActivity.class);
                    startActivity(i);
                    break;

                case R.id.btnManagement:
                    Toast.makeText(MainActivity.this,"미완성",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btnSchedule:
                    Toast.makeText(MainActivity.this,"미완성",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btnEnrolment:
                    Toast.makeText(MainActivity.this,"미완성",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //더블 탭으로 앱 종료하기
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
