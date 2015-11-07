package com.example.diego.proyecto;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.diego.proyecto.data.JobPostDbHelper;

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
import java.util.Date;

import Fragments.MainFragment;
import Fragments.PostearTrabajoFragment;

import static com.example.diego.proyecto.data.JobPostDbContract.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation, R.string.cerrar);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Llamo un fragmento para que sea el main por defecto


        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame,new MainFragment()).commit();


        //LoadDBAsyncTask asyncTask = new LoadDBAsyncTask();
        //asyncTask.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getFragmentManager();

        int id = item.getItemId();

        if (id == R.id.nav_sincroonizar) {
            fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

        } else if (id == R.id.nav_posteartrabajo) {
            fm.beginTransaction().replace(R.id.content_frame, new PostearTrabajoFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class NetworkOperationAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // This is my URL: http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null; // HttpURLConnection Object
            BufferedReader reader = null; // A BufferedReader in order to read the data as a file
            Uri buildUri = Uri.parse("http://dipandroid-ucb.herokuapp.com").buildUpon() // Build the URL using the Uri class
                    .appendPath("work_posts.json").build();
            try {
                URL url = new URL(buildUri.toString()); // Create a new URL

                urlConnection = (HttpURLConnection) url.openConnection(); // Get a HTTP connection
                urlConnection.setRequestMethod("GET"); // I'm using GET to query the server
                urlConnection.addRequestProperty("Content-Type", "application/json"); // The MIME type is JSON
                urlConnection.connect(); // Connect!! to the cloud!!!

                // Methods in order to read a text file (In this case the query from the server)
                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                // Save the data in a String
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                saveJSONToDatabase(buffer.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }

                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

            }
            return null;
        }

        private void saveJSONToDatabase(String json) throws JSONException {
            JSONArray array = new JSONArray(json);
            JobPostDbHelper dbHelper = new JobPostDbHelper(MainActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            for (int i = 0; i < array.length(); i++) {
                JSONObject jobPostJSON = array.getJSONObject(i);
                int id = jobPostJSON.getInt("id");
                String title = jobPostJSON.getString("title");
                String description = jobPostJSON.getString("description");
                String postedDate = jobPostJSON.getString("posted_date");
                ContentValues contentValues = new ContentValues();

                contentValues.put(JobEntry._ID, id);
                contentValues.put(JobEntry.COLUMN_TITLE, title);
                contentValues.put(JobEntry.COLUMN_DESCRIPTION, description);
                contentValues.put(JobEntry.COLUMN_POSTED_DATE, postedDate);
                db.insert(JobEntry.TABLE_NAME, null, contentValues);
            }

            db.close();
            dbHelper.close();
            LoadDBAsyncTask asyncTask = new LoadDBAsyncTask();
            asyncTask.execute();
        }
    }

    private class LoadDBAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> result = new ArrayList<>();
            JobPostDbHelper dbHelper = new JobPostDbHelper(MainActivity.this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(JobEntry.TABLE_NAME,
                    new String[] {JobEntry.COLUMN_TITLE, JobEntry.COLUMN_POSTED_DATE},
                    null, null, null, null, JobEntry.COLUMN_TITLE + " DESC");
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(0) + " - " + cursor.getString(1);
                    result.add(title);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            dbHelper.close();
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            arrayAdapter.clear();
            for (String title : strings) {
                arrayAdapter.add(title);
            }
        }
    }

}


