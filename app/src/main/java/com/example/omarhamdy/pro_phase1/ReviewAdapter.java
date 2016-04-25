package com.example.omarhamdy.pro_phase1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Omar Hamdy on 25/03/2016.
 */
public class ReviewAdapter extends BaseAdapter {

    Context mContext;

    ArrayList<String> Author ;
    ArrayList<String> Content ;



    public ReviewAdapter(Context context, ArrayList<String> author , ArrayList<String> content) {

        mContext   = context;

        Author = author ;
        Content = content ;




    }

    @Override
    public int getCount() {
        return Author.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView author , content;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        Holder holder=new Holder();


        convertView = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, null);

        holder.author=(TextView)convertView.findViewById(R.id.text);
        holder.content=(TextView)convertView.findViewById(R.id.text2);

        holder.author.setText(Author.get(position));
        holder.content.setText(Content.get(position));


        return convertView;
    }
}
