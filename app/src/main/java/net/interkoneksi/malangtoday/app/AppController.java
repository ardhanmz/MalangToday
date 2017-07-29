package net.interkoneksi.malangtoday.app;

/**
 * Created by Ardhan MZ on 29-Jul-17.
 */

import net.interkoneksi.malangtoday.util.LruBitmapCache;
import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application{
    public static final String TAG = AppController.class.getSimpleName();

    public RequestQueue mRequestQueue;
    public ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate(){//Method untuk inisialisasi onCreate
        super.onCreate();
        mInstance=this;
    }
    public static synchronized AppController getmInstance(){
        return mInstance;
    }

    public RequestQueue getmRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public ImageLoader getmImageLoader(){
        getmRequestQueue();
        if (mImageLoader == null){
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getmRequestQueue().add(req);
    }
    public void cancelPendingRequest(Object tag){
        if(mRequestQueue !=null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
