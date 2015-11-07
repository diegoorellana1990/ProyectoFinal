package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.diego.proyecto.AdaptadorAnuncios;
import com.example.diego.proyecto.Anuncio;
import com.example.diego.proyecto.R;

import java.util.ArrayList;

/**
 * Created by Diego on 23/10/2015.
 */
public class PostearTrabajoFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posteartrabajo,container,false);
        return rootView;
    }
}
