package ioc.xtec.cat.freebooks;

import android.widget.Filter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jordi on 24/03/2018.
 */

public class FiltreLlibres extends Filter {
    Adaptador adapter;
    ArrayList<Llibre> filterList;

    /**
     * Constructor
     *
     * @param filterList
     * @param adapter
     */
    public FiltreLlibres(ArrayList<Llibre> filterList, Adaptador adapter) {
        this.adapter = adapter;
        this.filterList = filterList;

    }

    /**
     * Filtratge
     *
     * @param constraint
     * @return
     */
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        String paraulaBuscada = constraint.toString();
        String texteOnBuscar = "";
        String texteOnBuscarSenseAccents = "";

        // Guarda els llibres filtrats
        ArrayList<Llibre> llibresFiltrats = new ArrayList<>();

        // Valida
        if (constraint != null && constraint.length() > 0) {

            for (int i = 0; i < filterList.size(); i++) {
                //Obtenim cada un dels titols o autors dels llibres disponibles a la bdd
                texteOnBuscar = filterList.get(i).getTitol() + " " + filterList.get(i).getAutor();
                texteOnBuscarSenseAccents = Normalizer.normalize(texteOnBuscar, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                Pattern regex = Pattern.compile("\\b" + Pattern.quote(paraulaBuscada), Pattern.CASE_INSENSITIVE);
                Matcher match = regex.matcher(texteOnBuscar);
                Matcher matchSenseAccents = regex.matcher(texteOnBuscarSenseAccents);
                //Si hi ha alguna coincidència tan si te accents/majúcules/mínuscules que començi per la paraula buscada
                if (match.find() || matchSenseAccents.find()) {
                    //Afegim el llibre a la llista on acumularem els coincidents amb la paraula cercada
                    llibresFiltrats.add(filterList.get(i));
                }
            }

            results.count = llibresFiltrats.size();
            results.values = llibresFiltrats;
        } else {
            results.count = filterList.size();
            results.values = filterList;

        }


        return results;
    }

    /**
     * Publica els resultats
     *
     * @param constraint
     * @param results
     */
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.setItems((ArrayList<Llibre>) results.values);

        // Nofifica els canvis a l'adaptador
        adapter.notifyDataSetChanged();
    }

}
