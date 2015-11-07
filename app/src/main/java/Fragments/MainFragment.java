package Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diego.proyecto.AdaptadorAnuncios;
import com.example.diego.proyecto.Anuncio;
import com.example.diego.proyecto.R;
import com.example.diego.proyecto.data.JobPostDbContract;
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
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Diego on 22/10/2015.
 */
public class MainFragment extends Fragment {

    ListView listview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        final ArrayList<Anuncio> anuncios = new ArrayList<Anuncio>();
        Anuncio a = new Anuncio(1,"Titulo",new Date(),"dsaf", new String[]{"564","4564"});
        anuncios.add(a);
        ListView lv = (ListView)rootView.findViewById(R.id.lvTrabajos);
        lv.setAdapter(new AdaptadorAnuncios(getActivity(), anuncios));


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Anuncio anuncioactual = anuncios.get(position);


                Bundle b = new Bundle();
                b.putInt("id_trabajo", anuncioactual.getId());
                b.putString("descripcion_trabajo",anuncioactual.getDescripcionn());
                b.putString("fecha_trabajo",anuncioactual.getFecha().toString());
                b.putStringArray("numeros_trabajo",anuncioactual.getNumeros());


                FragmentManager fm = getFragmentManager();
                AnuncioDetalleFragment fragment = new AnuncioDetalleFragment();
                fragment.setArguments(b);
                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });
        return rootView;
    }

}


