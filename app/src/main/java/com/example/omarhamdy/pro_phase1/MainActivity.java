package com.example.omarhamdy.pro_phase1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends ActionBarActivity {

    boolean Ui ;

    ArrayList<myMovieData> myFavourit = new ArrayList<myMovieData>(); ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout main2 = (FrameLayout)findViewById(R.id.main_layout2);



        if (main2==null)
        {
            Ui = true ;

            MainActivityFragment fragment = new MainActivityFragment(true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_layout,fragment)
                    .addToBackStack(null)
                    .commit();
        }


        else
        {
            Ui = false ;

            MainActivityFragment fragment = new MainActivityFragment(false);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_layout,fragment)
                    .addToBackStack(null)
                    .commit();

//            details details = new details();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.main_layout2,details)
//                    .addToBackStack(null)
//                    .commit();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (Ui==true)
           {

             if (id == R.id.action_top_rated)
                {

                  MainActivityFragment list = new MainActivityFragment("http://api.themoviedb.org/3/movie/top_rated?api_key=",true);

                  getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout, list )
                        .addToBackStack(null)
                        .commit();

               }

               if (id == R.id.action_most_popular)
               {

                   MainActivityFragment list = new MainActivityFragment("http://api.themoviedb.org/3/movie/popular?api_key=",true);

                   getSupportFragmentManager()
                           .beginTransaction()
                           .replace(R.id.main_layout, list )
                           .addToBackStack(null)
                           .commit();

               }

               if (id == R.id.action_favourit)
               {


                   Realm realm = Realm.getDefaultInstance();

                   RealmResults<myMovieData> List = realm.where(myMovieData.class).findAll();

                   for (int i =0 ;i<List.size();i++)
                   {
                       myFavourit.add(List.get(i));
                   }


                   MainActivityFragment list = new MainActivityFragment(myFavourit,true);

                   getSupportFragmentManager()
                           .beginTransaction()
                           .replace(R.id.main_layout, list)
                           .addToBackStack(null)
                           .commit();

               }

           }

            else
            {

                if (id == R.id.action_top_rated)
                {

                    MainActivityFragment list = new MainActivityFragment("http://api.themoviedb.org/3/movie/top_rated?api_key=",false);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_layout, list)
                            .addToBackStack(null)
                            .commit();

                }

                if (id == R.id.action_most_popular)
                {

                    MainActivityFragment list = new MainActivityFragment("http://api.themoviedb.org/3/movie/popular?api_key=",false);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_layout, list)
                            .addToBackStack(null)
                            .commit();

                }

                if (id == R.id.action_favourit)
                {


                    Realm realm = Realm.getDefaultInstance();

                    RealmResults<myMovieData> List = realm.where(myMovieData.class).findAll();

                    Realm realm2 = Realm.getDefaultInstance();

                    RealmResults<myMovieData> List2 = realm.where(myMovieData.class).findAll();

                    for (int i =0 ;i<List.size();i++)
                    {
                        myFavourit.add(List2.get(i));
                    }


                    MainActivityFragment list = new MainActivityFragment(myFavourit,false);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_layout, list)
                            .addToBackStack(null)
                            .commit();

                }
            }




        return super.onOptionsItemSelected(item);
    }
}
