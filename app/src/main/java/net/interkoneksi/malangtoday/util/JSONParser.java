package net.interkoneksi.malangtoday.util;



import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.app.AppController;

import net.interkoneksi.malangtoday.model.Post;
import net.interkoneksi.malangtoday.model.Category;

import java.util.ArrayList;

public class JSONParser {
    private static final String TAG="JSONParer";

    public static ArrayList<Category> parseCategories(JSONObject jsonObject){
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        try {
            JSONArray categories = jsonObject.getJSONArray("categories");

            Category all = new Category();
            all.setId(0);
            all.setName(AppController.getInstance().getString(R.string.tab_all));
            categoryArrayList.add(all);

            for (int i=0; i<categories.length();i++){
                JSONObject catObj = categories.getJSONObject(i);
                Log.d(TAG,"parsing" +catObj.getString("title")+", ID"+catObj.getInt("id"));
                Category c = new Category();
                c.setId(catObj.getInt("id"));
                c.setName(catObj.getString("title"));
                categoryArrayList.add(c);
            }
        }catch (JSONException e){
            Log.d(TAG,"-----------JSON EXCEPTION");
            e.printStackTrace();
            return null;
        }
        return categoryArrayList;
    }


    public static ArrayList<Post> parsePosts(JSONObject jsonObject){
        ArrayList<Post> posts = new ArrayList<>();

        try {
            JSONArray postArray = jsonObject.getJSONArray("posts");

            for (int i=0;i<postArray.length();i++){
                JSONObject postObject = postArray.getJSONObject(i);

                Post post = new Post();
                post.setTitle(postObject.optString("title","N/A"));

                post.setThumbnailUrl(postObject.optString("thumbnail", Config.DEFAULT_THUMBNAIL_URL));

                post.setDate(postObject.optString("date","N/A"));

                post.setContent(postObject.optString("content","N/A"));

                post.setAuthor(postObject.getJSONObject("author").optString("name","N/A"));

                post.setId(postObject.optInt("id"));

                post.setUrl(postObject.optString("url"));

                JSONObject featuredImages = postObject.optJSONObject("thumbnail_images");
                if (featuredImages != null){
                    post.setFeaturedImageUrl(featuredImages.optJSONObject("full")
                            .optString("url", Config.DEFAULT_THUMBNAIL_URL));
                }
                posts.add(post);
            }
        }catch (JSONException e){
            Log.d(TAG,"----------JSON Exception");
            Log.d(TAG, e.getMessage());
            return null;
        }

        return posts;
    }
}
