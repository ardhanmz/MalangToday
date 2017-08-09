package net.interkoneksi.malangtoday.adaptor;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import net.interkoneksi.malangtoday.app.RecyclerViewFragment;
import net.interkoneksi.malangtoday.model.Category;

import java.util.ArrayList;

public class RecycleViewAdaptorFragment extends FragmentPagerAdapter {
    private ArrayList<Category> categories;

    public RecycleViewAdaptorFragment (FragmentManager fm, ArrayList<Category> categories){
        super(fm);
        this.categories = categories;
    }
    @Override
    public Fragment getItem(int position){
        return RecyclerViewFragment.newInstance(categories.get(position).getId());
    }

    @Override
    public CharSequence getPageTitle(int position){ return categories.get(position).getName();}

    @Override
    public int getCount(){
        return categories.size();
    }
}
