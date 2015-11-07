package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.diego.proyecto.AdaptadorAnuncios;
import com.example.diego.proyecto.Anuncio;
import com.example.diego.proyecto.R;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by Diego on 02/11/2015.
 */
public class AnuncioDetalleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_anuncio_detalle, container, false);

        Bundle b = getArguments();
        TextView tvTitulo = (TextView)rootView.findViewById(R.id.tvDTitulo);
        TextView tvDescripcion = (TextView)rootView.findViewById(R.id.tvDDescripcion);
        TextView tvNumeros = (TextView)rootView.findViewById(R.id.tvDNumeros);

        tvTitulo.setText(String.valueOf(b.getInt("id_trabajo")));
        tvDescripcion.setText(b.getString("descripcion_trabajo"));

        String num = "";
        String[] numeros = b.getStringArray("numeros_trabajo");
        for(int i=0;i<numeros.length;i++)
        {
            num+=numeros[i]+"\n";
        }

        tvNumeros.setText(num);
        return rootView;
    }
}
