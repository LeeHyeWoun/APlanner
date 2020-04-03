package hobby.leehyewoun.aplanner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hobby.leehyewoun.aplanner.fragment.ScoreTop1Fragment;
import hobby.leehyewoun.aplanner.fragment.ScoreTop2Fragment;

public class PagerAdapter_ScoreTop extends FragmentStatePagerAdapter {
    private Fragment tab;
    private int mTabOfNum;
    private int MaxPage = 2;

    //생성자
    public PagerAdapter_ScoreTop(FragmentManager fm){ super(fm); }

    @Override
    public Fragment getItem(int position) {
        //BaseAdapter 에서 getView() 메서드에 해당하는 메서드로써,
        //position 값이 곧 현재 선택 된 Tab 의 Index 번호를 나타낸다.

        //평균 원형 그래프
        if(position==0){
            tab = new ScoreTop1Fragment();
            ((ScoreTop1Fragment) tab).setTabOfNum(mTabOfNum);
        }
        //전체 평균 성적 꺽은 선 그래프
        else{
            tab = new ScoreTop2Fragment();
        }
        return tab;

    }

    @Override
    public int getCount() { return MaxPage; }

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;

    }
    public void setTabOfNum(int tabOfNum){
        mTabOfNum = tabOfNum;
    }

}
