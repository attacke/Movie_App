package com.example.omarhamdy.pro_phase1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static String link = "http://api.themoviedb.org/3/movie/popular?api_key=";

    boolean UI = true ;

    ArrayList<myMovieData> Favourit ;

    boolean usingfav = false ;

    public MainActivityFragment() {
    }

    public MainActivityFragment(ArrayList<myMovieData> fav , boolean ui) {

        Favourit=fav ;
        UI=ui;
        usingfav=true;
    }

    public MainActivityFragment(boolean ui) { UI=ui ; }

    public MainActivityFragment(String moviesLink , boolean ui)
    {
        link = moviesLink ;
        UI = ui ;
    }

    GridView gridView ;

    private static final String RESULTS="results";
    private static final String ID ="id";
    private static final String POSTER ="poster_path";
    private static final String OVERVIEW="overview";
    private static final String ORIGINAL_TITLE ="original_title";
    private static final String RELEASE_DATE  ="release_date";
    private static final String VOTE_AVERAGE ="vote_average";


    JSONObject Movies = null;
    JSONArray MoviesArray = null;
    ArrayList<myMovieData> MoviesList = new ArrayList<myMovieData>();

    @Override
    public void onStart() {
        super.onStart();

        if (usingfav==false)
        {

            Movie movie =new Movie();
            movie.execute();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_main,container,false);


        gridView =(GridView)view.findViewById(R.id.gridView);


        if (usingfav==true)
        {


            if (UI==false)
            {
                String movie_id = Favourit.get(0).getId();
                String original_title = Favourit.get(0).getOriginal_title();
                String poster = Favourit.get(0).getPoster();
                String overview = Favourit.get(0).getOverview();
                String release_date = Favourit.get(0).getRelease_date();
                String vote_average = Favourit.get(0).getVote_average();

                details details = new details(Favourit.get(0),movie_id,original_title,poster,overview,release_date,vote_average);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout2, details)
//                        .addToBackStack(null)
                        .commit();

            }

            gridView.setAdapter(new GridviewAdapter(getActivity(), Favourit));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String movie_id = Favourit.get(position).getId();
                    String original_title = Favourit.get(position).getOriginal_title();
                    String poster = Favourit.get(position).getPoster();
                    String overview = Favourit.get(position).getOverview();
                    String release_date = Favourit.get(position).getRelease_date();
                    String vote_average = Favourit.get(position).getVote_average();

                    details details = new details(Favourit.get(position) , movie_id,original_title,poster,overview,release_date,vote_average);


                    if (UI==true)
                    {
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_layout, details)
                                .addToBackStack(null)
                                .commit();
                    }
                    else
                    {
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_layout2, details)
//                                .addToBackStack(null)
                                .commit();
                    }




                }
            });


        }


        return view ;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    public class Movie extends AsyncTask<View,View,ArrayList<myMovieData>> {

        private final String LOG_TAG = Movie.class.getSimpleName();

        @Override
        protected ArrayList<myMovieData> doInBackground(View... params) {

            MoviesList.clear();

            HttpURLConnection urlConnection =null;
            BufferedReader reader = null ;

            String movieJson=null;
            try {

                URL url = new URL(link + "enter your api key here"); // youe have to enter your api key in here , tralier asyncTask and review asyncTask

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

                movieJson=buffer.toString();
                Log.e(LOG_TAG, "movie JSON String " + movieJson);

                Movies = new JSONObject(movieJson);
                MoviesArray=Movies.getJSONArray(RESULTS);


                for (int i = 0;i<MoviesArray.length();i++) {

                    JSONObject data = MoviesArray.getJSONObject(i);

                    String id = data.getString(ID);
                    String original_title = data.getString(ORIGINAL_TITLE);
                    String poster = data.getString(POSTER);
                    String overview = data.getString(OVERVIEW);
                    String release_date = data.getString(RELEASE_DATE);
                    String vote_average = data.getString(VOTE_AVERAGE);

                    myMovieData Obj = new myMovieData(id, original_title, poster, overview ,release_date ,vote_average);

                    MoviesList.add(Obj);

                }

                if(MoviesList.size() > 0)
                {

                    return MoviesList;
                }





            } catch (IOException e) {
                Log.e(LOG_TAG, "error", e);
                e.printStackTrace();
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
        protected void onPostExecute(ArrayList movies) {

            if (UI==false)
            {
                String movie_id = MoviesList.get(0).getId();
                String original_title = MoviesList.get(0).getOriginal_title();
                String poster = MoviesList.get(0).getPoster();
                String overview = MoviesList.get(0).getOverview();
                String release_date = MoviesList.get(0).getRelease_date();
                String vote_average = MoviesList.get(0).getVote_average();

                details details = new details(MoviesList.get(0),movie_id,original_title,poster,overview,release_date,vote_average);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout2, details)
//                        .addToBackStack(null)
                        .commit();

            }

          gridView.setAdapter(new GridviewAdapter(getActivity(), MoviesList));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String movie_id = MoviesList.get(position).getId();
                    String original_title = MoviesList.get(position).getOriginal_title();
                    String poster = MoviesList.get(position).getPoster();
                    String overview = MoviesList.get(position).getOverview();
                    String release_date = MoviesList.get(position).getRelease_date();
                    String vote_average = MoviesList.get(position).getVote_average();

                    details details = new details(MoviesList.get(position) , movie_id,original_title,poster,overview,release_date,vote_average);


                    if (UI==true)
                    {
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_layout, details)
                                .addToBackStack(null)
                                .commit();
                    }
                    else
                    {
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_layout2, details)
//                                .addToBackStack(null)
                                .commit();
                    }




                }
            });

        }
    }



}
