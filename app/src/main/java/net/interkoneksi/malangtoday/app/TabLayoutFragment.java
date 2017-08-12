package net.interkoneksi.malangtoday.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import net.interkoneksi.malangtoday.util.Config;
import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.adaptor.RecycleViewAdaptorFragment;
import net.interkoneksi.malangtoday.model.Category;
import net.interkoneksi.malangtoday.util.JSONParser;

import org.json.JSONObject;

import java.util.ArrayList;


public class TabLayoutFragment extends Fragment implements SearchView.OnQueryTextListener {
    private  static final String TAG = "TabLayoutFragment";

    private ProgressDialog mProgressDialog;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchView searchView;
    private MenuItem searchMenuItem;

    protected static ArrayList<Category> categories = null;
    private  TabLayoutListener mListener;

    public TabLayoutFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstaceState){
        View rootView = inflater.inflate(R.layout.fragment_tab_layout, container, false);

        mTabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(1);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadCategories();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        Log.d(TAG, "onCreateOptionsMenu");

        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager= (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_search){
            searchView.requestFocus();
        }
        return true;
    }

    private void loadCategories(){
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.loading_category));

        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Config.ALL_CATEGORY_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                mProgressDialog.dismiss();
                categories = JSONParser.parseCategories(jsonObject);

                RecycleViewAdaptorFragment adaptor = new RecycleViewAdaptorFragment(getChildFragmentManager(),
                        categories);
                mViewPager.setAdapter(adaptor);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "========Volley Error");
                mProgressDialog.dismiss();

                Snackbar.make(mTabLayout, R.string.error_load_categories,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.action_retry,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadCategories();
                            }
                        }).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
    @Override
    public boolean onQueryTextSubmit(String query){
        searchView.clearFocus();
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText){ return false; }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            mListener = (TabLayoutListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement blabla");
        }
    }

    public interface TabLayoutListener {

    }
}
