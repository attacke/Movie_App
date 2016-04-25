package com.example.omarhamdy.pro_phase1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.realm.Realm;


public class details extends Fragment {

    TextView textView ,textView2 ,textView3 , textView4 ;
    ImageView imageView ;
    ListView listView ,listView2 ;
    ImageView imageFav ;

    String Id ,Original_title , Poster , Overview , Release_date , Vote_average ;
    myMovieData Favourite ;

    private static final String RESULTS = "results" ;
    private static final String KEY = "key" ;
    private static final String AUTHOR = "author" ;
    private static final String CONTENT = "content" ;

    JSONObject TralierObject = null;
    JSONArray TralierArray = null;
    ArrayList<String> asyncTralier =new ArrayList<String>();
    ArrayList <String> tralierList =new ArrayList<String>();


    ArrayList<String> asyncAuthor =new ArrayList<String>();
    ArrayList<String> asyncContent =new ArrayList<String>();

    public ShareActionProvider shareActionProvider ;

    MenuItem item ;

    Intent shareIntent = new Intent(Intent.ACTION_SEND);




    public details() {

        // Required empty public constructor
    }

    public details(myMovieData fav,String id ,String original_title,String poster,String overview, String release_date , String vote_average) {

        Favourite=fav ;
        Id=id;
        Original_title=original_title;
        Poster=poster;
        Overview=overview;
        Release_date=release_date;
        Vote_average=vote_average;

        Tralier tralier = new Tralier();
        tralier.execute();

        Review review = new Review();
        review.execute();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(Favourite);
//        realm.commitTransaction();
     }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_details, container, false);

        listView=(ListView)view.findViewById(R.id.list);
        listView2=(ListView)view.findViewById(R.id.list2);

        textView=(TextView)view.findViewById(R.id.textView);
        textView.setText(Original_title);

        imageView=(ImageView)view.findViewById(R.id.imageView);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185" + Poster)
                .resize(350,350)
                .into(imageView);


        textView2=(TextView)view.findViewById(R.id.textView2);
        textView2.setText(Release_date);

        textView3=(TextView)view.findViewById(R.id.textView3);
        textView3.setText(Vote_average+" / 10 ");



        textView4=(TextView)view.findViewById(R.id.textView4);
        textView4.setText(Overview);

        imageFav = (ImageView)view.findViewById(R.id.imageView2);
        imageFav.setImageResource(R.drawable.favourite_icon);


        imageFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(Favourite);
                realm.commitTransaction();

                Toast.makeText(getActivity(),Favourite.getOriginal_title()+" added to favourit",Toast.LENGTH_SHORT).show();

            }
        });


        setHasOptionsMenu(true);

        return view ;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.share_menu, menu);
         item = menu.findItem(R.id.menu_item_share);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        if (asyncTralier.size()>0)
        {
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + asyncTralier.get(0));

        }
        else
        {
            shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        }

        shareActionProvider.setShareIntent(shareIntent);


    }



    public class Tralier extends AsyncTask<View, View, ArrayList<String>>
    {

        private final String LOG_TAG = Tralier.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(View... params)
        {

            HttpURLConnection urlConnection =null;
            BufferedReader reader = null ;

            String tralierJson=null;

            try {

                URL url = new URL("http://api.themoviedb.org/3/movie/"+ Id +"/videos?api_key=" + "enter your api key here" ); // enter your api key here

                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer =new StringBuffer();
                if (inputStream==null){
                    return null ;
                }

                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line ;
                while ((line=reader.readLine()) !=null){
                    buffer.append(line + "\n");
                }

                if (buffer.length()==0){
                    return null;
                }

                tralierJson=buffer.toString();
                Log.e(LOG_TAG, "tralier JSON String " + tralierJson);

                TralierObject = new JSONObject(tralierJson);
                TralierArray=TralierObject.getJSONArray(RESULTS);


                for (int i = 0;i<TralierArray.length();i++) {

                    JSONObject data = TralierArray.getJSONObject(i);

                    asyncTralier.add(data.getString(KEY));

                }


//                Log.e("test","tralier key " +asyncTralier.get(0) );


                if(asyncTralier.size() > 0)
                {

                    return asyncTralier;
                }





            } catch (IOException e) {
                Log.e(LOG_TAG, "error", e);
                return null;
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<String> strings) {




            for (int i=0 ; i<asyncTralier.size();i++ )
            {
                tralierList.add("Tralier "+(i+1));
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.tralier_list_item,R.id.text,tralierList);
            listView.setAdapter(adapter);

            Log.e("test", "tralier key " + asyncTralier.size());


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v="+asyncTralier.get(position)));

                    startActivity(intent);
                }
            });



        }
    }



    public class Review extends AsyncTask<View, View, ArrayList<String>>
    {

        private final String LOG_TAG = Review.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(View... params)
        {

            HttpURLConnection urlConnection =null;
            BufferedReader reader = null ;

            String reviewJson=null;

            try {

                URL url = new URL("http://api.themoviedb.org/3/movie/"+ Id +"/reviews?api_key=" + "enter your api key here"); // enter your api key here

                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer =new StringBuffer();
                if (inputStream==null){
                    return null ;
                }

                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line ;
                while ((line=reader.readLine()) !=null){
                    buffer.append(line + "\n");
                }

                if (buffer.length()==0){
                    return null;
                }

                reviewJson=buffer.toString();
                Log.e(LOG_TAG, "review JSON String " + reviewJson);

                TralierObject = new JSONObject(reviewJson);
                TralierArray=TralierObject.getJSONArray(RESULTS);


                for (int i = 0;i<TralierArray.length();i++) {

                    JSONObject data = TralierArray.getJSONObject(i);

                    asyncAuthor.add(data.getString(AUTHOR)+" : ");
                    asyncContent.add(data.getString(CONTENT));

                }


//                Log.e("test","tralier key " +asyncTralier.get(0) );


                if(asyncAuthor.size() > 0)
                {

                    return asyncAuthor;
                }


                if(asyncContent.size() > 0)
                {

                    return asyncContent;
                }



            } catch (IOException e) {
                Log.e(LOG_TAG, "error", e);
                return null;
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            listView2.setAdapter(new ReviewAdapter(getActivity(),asyncAuthor,asyncContent));

        }
    }


}

