package net.interkoneksi.malangtoday.app;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.adaptor.RecycleViewAdaptor;
import net.interkoneksi.malangtoday.util.JSONParser;
import net.interkoneksi.malangtoday.util.Config;
import net.interkoneksi.malangtoday.model.Post;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class RecyclerViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    protected static final String CAT_ID = "id";
    protected static final String QUERY = "query";
    protected static final String TAG = "RecyclerViewFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecycleViewAdaptor mAdaptor;
    private LinearLayoutManager mLayoutManager;
    private TextView mLoadingView;

    private ArrayList<Post> postList = new ArrayList<>();
    private boolean isLoading = false;
    private  int mPage = 1;
    private int mCatId;
    private int mPreviousPostNum = 0;
    private int mPostNum;

    private  String mQuery="";
    private boolean isSearch = false;
    private int mPastVisibleItems;
    private int mVisibleItemCount;

    private PostListener mListener;

    public static RecyclerViewFragment newInstance(int id){
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt(CAT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
    public static RecyclerViewFragment newInstance(String query){
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }
    public RecyclerViewFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getArguments() !=null){
            mCatId = getArguments().getInt(CAT_ID, -1);
            mQuery = getArguments().getString(QUERY, "");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, containter, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLoadingView = (TextView) rootView.findViewById(R.id.text_view_loading);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdaptor = new RecycleViewAdaptor(postList,
                new RecycleViewAdaptor.OnItemClickListener() {
                    @Override
                    public void onItemClick(Post post) {
                        mListener.onPostSelected(post, isSearch);
                    }
                });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdaptor);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                mVisibleItemCount = mLayoutManager.getChildCount();
                mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();

                if (mPostNum>mPreviousPostNum && !postList.isEmpty() && mVisibleItemCount!=0
                        && totalItemCount > mVisibleItemCount && !isLoading &&
                        (mVisibleItemCount + mPastVisibleItems) >= totalItemCount){
                    loadNextPage();
                    mPreviousPostNum = mPostNum;
                }
            }
        });
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);
        loadFirstPage();
    }
    public void loadFirstPage(){
        mPage = 1;

        if (postList.isEmpty()){
            showLoadingView();
            mPreviousPostNum = 0;
            loadPosts(mPage, false);
        }else {
            hideLoadingView();
        }
    }
    public void loadNextPage(){
        mPage++;
        loadPosts(mPage, true);
    }

    private  void loadPosts(int page,final boolean showLoadingMsg){

        isLoading = true;
        if (showLoadingMsg){
            Toast.makeText(getActivity(), getString(R.string.loading_article),
                    Toast.LENGTH_LONG).show();
        }

        String url;
        if (!mQuery.isEmpty()){
            isSearch = true;
            url = Config.BASE_URL + "?json=get_search_results&search=" + mQuery +
                    "&page=" + String.valueOf(page);
        }else {
            isSearch = false;
            if (mCatId == 0){
                url = Config.BASE_URL + "?json=get_recent_posts&page=" + String.valueOf(page);
            }else {
                isSearch = false;
                url = Config.BASE_URL + "?json=get_category_posts&category_id=" + String.valueOf(mCatId)
                        + "&page=" + String.valueOf(page);
            }
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        //Parsing JSON

                        postList.addAll(JSONParser.parsePosts(jsonObject));
                        Set<Post> set = new LinkedHashSet<>(postList);
                        postList.clear();
                        postList.addAll(new ArrayList<>(set));
                        mPostNum = postList.size();
                        Log.d(TAG, "Number Post"+mPostNum);
                        mAdaptor.notifyDataSetChanged();
                        if (RecyclerViewFragment.this.mPage != 1) {
                            mLayoutManager.scrollToPosition(mPastVisibleItems + mVisibleItemCount);
                        }
                        isLoading = false;

                        hideLoadingView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        isLoading = false;
                        hideLoadingView();
                        mSwipeRefreshLayout.setRefreshing(false);

                        volleyError.printStackTrace();
                        Log.d(TAG, "=============ERROR VOLLEY=============="+volleyError.getMessage());

                        Snackbar.make(mRecyclerView, R.string.error_load_post, Snackbar.LENGTH_LONG)
                                .setAction(R.string.action_retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loadPosts(mPage, true);
                                    }
                                }).show();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(request,TAG);
    }
    @Override
    public void onRefresh(){
        postList.clear();
        mAdaptor.notifyDataSetChanged();
        loadFirstPage();
    }
    private void showLoadingView(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.VISIBLE);
    }
    private void hideLoadingView(){
        mLoadingView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    public interface PostListener{
        void onPostSelected(Post post, boolean isSearch);
    }

}