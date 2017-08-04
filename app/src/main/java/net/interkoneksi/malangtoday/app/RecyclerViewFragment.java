package net.interkoneksi.malangtoday.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.appcompat.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import net.interkoneksi.malangtoday.adaptor.RecycleViewAdaptor;
import net.interkoneksi.malangtoday.util.JSONParser;
import net.interkoneksi.malangtoday.Config.Config;
import net.interkoneksi.malangtoday.model.Post;

import java.util.ArrayList;
import java.util.LinkedHashSet;




public class RecyclerViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "RecycleViewFragment";
    protected static final String CAT_ID= "id";
    protected static final String QUERY = "query";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecycleViewAdaptor mAdaptor;
    private LinearLayoutManager mLayoutManager;

    private TextView mLoadingView;

    private ArrayList<Post> postsList = new ArrayList<>();

    private boolean isLoading = false;

    private int mPage = 1;
    private int mCatId;
    private int mPreviousPostNum = 0;
    private int mPostNum;
    private String mQuery = "";
    private boolean isSearch = false;

    private int mPastVisibleItems;
    private int mVisibleItemCount;

    private PostListListener mListener;

    public static RecyclerViewFragment newInstance(int id){
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt(CAT_ID, id);
        return fragment;

    }

    public RecyclerViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            mCatId = getArguments().getInt(CAT_ID, -1);
            mQuery = getArguments().getString(QUERY, "");
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mLoadingView = (TextView) rootView.findViewById(R.id.text_view_loading);


    }

}
