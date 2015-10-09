package com.training.android.dle.instagramphoto;


import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto>{
    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_photo, parent, false);
            viewHolder.caption = (TextView)convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView)convertView.findViewById(R.id.ivPhoto);
            viewHolder.likes = (TextView)convertView.findViewById(R.id.tvLikes);
            viewHolder.user = (TextView)convertView.findViewById(R.id.tvUser);
            viewHolder.time = (TextView)convertView.findViewById(R.id.tvDate);
            viewHolder.profilePicture = (ImageView)convertView.findViewById(R.id.ivUser);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.caption.setText(photo.caption);
        // clear out photo view
        viewHolder.photo.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.loading).into(viewHolder.photo);
        viewHolder.likes.setText(Html.fromHtml(String.format("<strong>%d Likes</strong>", photo.likeCount)));
        viewHolder.user.setText( photo.username );
        String date = DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        viewHolder.time.setText(date);
        viewHolder.profilePicture.setImageResource(0);
        if (photo.profileUrl != null){
            Picasso.with(getContext()).load(photo.profileUrl).resize(75,75).into(viewHolder.profilePicture);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView caption;
        ImageView photo;
        TextView likes;
        TextView user;
        TextView time;
        ImageView profilePicture;
    }
}
