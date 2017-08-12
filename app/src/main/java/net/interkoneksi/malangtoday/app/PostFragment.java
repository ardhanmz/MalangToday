package net.interkoneksi.malangtoday.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

import com.bumptech.glide.Glide;

import net.interkoneksi.malangtoday.R;

/**
 * Created by Ardhan MZ on 08-Aug-17.
 */

public class PostFragment extends Fragment {
    private static final String TAG ="PostFragment";

    private int id;
    private String title;
    private String content;
    private String url;
    private String featuredImageUrl;

    private WebView webView;
    private ImageView featuredImageView;
    private NestedScrollView nestedScrollView;

    private AppBarLayout appBarLayout;
    private CoordinatorLayout coordinatorLayout;

    private PostListener mListener;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        return rootView;
    }
    public void setUIArguments(final  Bundle args){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadData("","text/html; charset=UTF-8", null);
                featuredImageView.setImageBitmap(null);

                id=args.getInt("id");
                title = args.getString("title");
                String date = args.getString("date");
                String author = args.getString("author");
                content = args.getString("content");
                url = args.getString("url");
                featuredImageUrl = args.getString("featuredimage");

                Glide.with(PostFragment.this)
                        .load(featuredImageUrl)
                        .centerCrop()
                        .into(featuredImageView);
                String html="<style>img{max-width:100%;height:auto;} "+
                        "iframe{width:100%;}</style>";

                html += "<h2>" +title+ "</h2>";

                html += "<h4>"+date+""+author+"</h4>";

                html += content;

                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());

                webView.loadData(html, "text/html; charset=UTF-8",null);

                Log.d(TAG, "showing post, ID"+id);
                Log.d(TAG, "featured Image : "+featuredImageUrl);

                expandToolbar();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.smoothScrollTo(0,0);
                    }
                },500);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.home:
                mListener.onHomePressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void expandToolbar(){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

        if (behavior!=null){
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, null, 0, 1, new int[2]);
        }
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            mListener = (PostListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"Must Implement");
        }
    }
    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

    public interface PostListener{
        void onHomePressed();
    }
}
