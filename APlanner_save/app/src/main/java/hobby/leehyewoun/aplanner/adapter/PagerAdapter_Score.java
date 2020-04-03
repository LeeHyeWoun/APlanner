package hobby.leehyewoun.aplanner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hobby.leehyewoun.aplanner.fragment.ScoreList1Fragment;

public class PagerAdapter_Score extends FragmentStatePagerAdapter {
    private int mNumOfTab;
    private Fragment tab;

    //생성자
    public PagerAdapter_Score(FragmentManager fm, int NumOfTab){
        super(fm);
        mNumOfTab = NumOfTab;
    }

    @Override
    public Fragment getItem(int position) {
        //BaseAdapter 에서 getView() 메서드에 해당하는 메서드로써,
        //position 값이 곧 현재 선택 된 Tab 의 Index 번호를 나타낸다.
        tab = new ScoreList1Fragment();
        ((ScoreList1Fragment) tab).setTabOfNum(position);
        return tab;
    }

    @Override
    public int getCount() { return mNumOfTab; }

    //리스트 어답터 갱신 ScoreList1Fragment->PagerAdapter -> ScoreActivity 까지 전달하기
    public void listNotifyDataSetChanged(){((ScoreList1Fragment)tab).listNotifyDataSetChanged();}

}
