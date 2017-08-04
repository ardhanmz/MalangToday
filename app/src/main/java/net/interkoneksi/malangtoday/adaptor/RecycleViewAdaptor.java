package net.interkoneksi.malangtoday.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.interkoneksi.malangtoday.model.Post;

import java.util.ArrayList;
import java.util.List;


public class RecycleViewAdaptor extends RecyclerView.Adapter<RecycleViewAdaptor.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnailImageView;
        TextView title;
        TextView commentCount;

        public ViewHolder(View itemView){
            super(itemView);

            thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            commentCount = (TextView) itemView.findViewById(R.id.comment_count);
        }

        private List<Post> posts;
        private Context mContext;

        private OnItemClickListener mListener;

        private interface OnItemClickListener {
            void onItemClick(Post post);
        }

        public RecycleViewAdaptor(ArrayList<Post> posts, OnItemClickListener listener){
            this.posts = posts;
            mListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_item, viewGroup, false);
            mContext = viewGroup.getContext();
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i){
            Glide.with(mContext).load(posts.get(i).getTittle()).centerCrop().into(ViewHolder,thumbnailImageView);

            viewHolder.title.setText(posts.get(i).getTittle());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    mListener.onItemClick(posts.get(i));
                }
            });
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }
}
