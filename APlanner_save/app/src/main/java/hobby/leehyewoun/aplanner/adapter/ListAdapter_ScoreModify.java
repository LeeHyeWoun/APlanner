package hobby.leehyewoun.aplanner.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import hobby.leehyewoun.aplanner.R;
import hobby.leehyewoun.aplanner.ScoreModifyActivity;
import hobby.leehyewoun.aplanner.bean.SaveBean;
import hobby.leehyewoun.aplanner.bean.ScoreBean;
import hobby.leehyewoun.aplanner.util.PrefUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ListAdapter_ScoreModify extends BaseAdapter {
    //생성자의 매개변수 값을 받을 변수
    private Context mContext;
    private List<ScoreBean> mList;
    private ImageView mImgSave;
    private int mTabOfNum;
    private TextView mTxtCreditsEarned,mTxtTitle, mCategory, mScore, mCredit, mSubject;
    private ProgressBar mCreditsEarned;

    public ListAdapter_ScoreModify(Context context, List<ScoreBean> list, TextView txtTitle, ImageView imgSave,
                                   int tabOfNum, TextView txtCreditsEarned, ProgressBar creditsEarned, TextView category, TextView score, TextView credit, EditText subject){
        mContext = context;
        mList = list;
        mTxtTitle = txtTitle;
        mImgSave = imgSave;
        mTabOfNum = tabOfNum;
        mTxtCreditsEarned = txtCreditsEarned;
        mCreditsEarned = creditsEarned;
        mCategory = category;
        mScore = score;
        mCredit = credit;
        mSubject = subject;
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
        convertView = inflater.inflate(R.layout.view_scoremodify_list, null);

        //해당 ROW 의 데이터를 찾는 작업
        final ScoreBean scoreBean = mList.get(position);

        //인플레이팅 된 뷰에서 ID 찾는작업
        final View listBackground = convertView.findViewById(R.id.listBackground);
        final TextView txtCategory = convertView.findViewById(R.id.txtCategory);
        final TextView txtScore = convertView.findViewById(R.id.txtScore);
        final TextView txtCredit = convertView.findViewById(R.id.txtCredit);
        final TextView txtSubject = convertView.findViewById(R.id.txtSubject);

        //키보드 객체
        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);

        //데이터 설정
        txtCategory.setText(scoreBean.getCategory());
        txtScore.setText(scoreBean.getScore());
        txtCredit.setText(scoreBean.getCredit());
        txtSubject.setText(scoreBean.getSubject());
        //만약 편집을 위한 클릭시 색상으로 표시
        if(PrefUtil.getDataInt(mContext,mTabOfNum+"EditList")!=position){
            listBackground.setPressed(false);
        }
        else{
            listBackground.setPressed(true);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //키보드 닫기
                imm.hideSoftInputFromWindow(mSubject.getWindowToken(), 0);

                //이전에 선택된 목록의 배경을 지우기 위한 갱신
                notifyDataSetChanged();

                //타이틀 변경
                SpannableStringBuilder ssb = new SpannableStringBuilder("'"+scoreBean.getSubject());
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTxtTitle.setText(ssb.append("' 수정"));

                //수정용 아이콘으로 바꾸기
                mImgSave.setImageResource(R.drawable.icon_ok);

                //데이터 설정
                mCategory.setText(scoreBean.getCategory());
                mScore.setText(scoreBean.getScore());
                mCredit.setText(scoreBean.getCredit());
                mSubject.setText(scoreBean.getSubject());

                mSubject.clearFocus();

                //리스트의 변경될 데이터 위치 저장
                PrefUtil.setData(mContext,mTabOfNum+"EditList",position);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //키보드 닫기
                imm.hideSoftInputFromWindow(mSubject.getWindowToken(), 0);

                listBackground.setSelected(true);
                txtCategory.setTextColor(Color.parseColor("#ffffff"));
                txtScore.setTextColor(Color.parseColor("#ffffff"));
                txtCredit.setTextColor(Color.parseColor("#ffffff"));
                txtSubject.setTextColor(Color.parseColor("#ffffff"));

                //메세지 설정
                SpannableStringBuilder ssb = new SpannableStringBuilder("'"+scoreBean.getSubject()+"'");
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#D81B60")), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //종성 받침의 유무에 따라 '을'/'를' 구분
                if((scoreBean.getSubject().charAt(scoreBean.getSubject().length() - 1) - 0xAC00) % 28>0){
                    ssb.append("을 삭제하시겠습니까?");
                }
                else { ssb.append("를 삭제하시겠습니까?"); }

                //다이얼로그
                new AlertDialog.Builder(mContext)
                        .setMessage(ssb)
                        .setCancelable(true)
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //삭제
                                mList.remove(position);
                                //이수한 학점 삭제
                                if(!scoreBean.getScore().equals("F")){
                                    int sumCredit = PrefUtil.getDataInt0(mContext,"sumCredit");
                                    ProgressBarAnimation anim = new ProgressBarAnimation(mCreditsEarned, sumCredit, sumCredit - Integer.parseInt(scoreBean.getCredit()));
                                    anim.setDuration(300);
                                    mCreditsEarned.setAnimation(anim);

                                    sumCredit-= Integer.parseInt(scoreBean.getCredit());
                                    PrefUtil.setData(mContext,"sumCredit",sumCredit);
                                    mTxtCreditsEarned.setText(String.valueOf(sumCredit));
                                    mCreditsEarned.setProgress(sumCredit);
                                }

                                //초기화
                                mCategory.setText("");
                                mScore.setText("");
                                mCredit.setText("");
                                mSubject.setText("");
                                PrefUtil.setData(mContext,mTabOfNum+"EditList",-1);

                                //리스트 저장하기
                                SaveBean saveBean = new SaveBean();
                                Gson gson = new Gson();
                                saveBean.setScoreListBean(mList);
                                PrefUtil.setData(mContext,mTabOfNum+"tabScoreData",gson.toJson(saveBean));

                                //리스트 갱신
                                notifyDataSetChanged();

                                //추가 알림 토스트 띄우기
                                Toast.makeText(mContext,scoreBean.getSubject()+"가 삭제되었습니다.",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .show();

                return true;
            }
        });
        return convertView;

    }//end OnCreate;

    private class ProgressBarAnimation extends Animation {
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
    }//end ProgressBarAnimation


}//end class
