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
public class GridviewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<myMovieData> MoviesData ;


    public GridviewAdapter(Context context, ArrayList<myMovieData> moviesList) {

        mContext   = context;

        MoviesData = moviesList ;



    }




    @Override
    public int getCount() {
        return MoviesData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    public class Holder
//    {
////        TextView tv;
//        ImageView img;
//    }


    TextView text ;
    ImageView img ;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


//        Holder holder=new Holder();


        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);

       // text=(TextView)convertView.findViewById(R.id.text);
        img=(ImageView)convertView.findViewById(R.id.imageView1);

        //text.setText(MoviesData.get(position).getOriginal_title());


        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + MoviesData.get(position).getPoster()).placeholder(R.drawable.movie).into(img);


        return convertView;
    }
}
