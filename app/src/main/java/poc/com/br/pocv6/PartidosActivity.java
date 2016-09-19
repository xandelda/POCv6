package poc.com.br.pocv6;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import classesAdapter.PartidosListArrayAdapter;

public class PartidosActivity extends ListActivity {

    public static String RESULT_PARTIDOSNAME = "partidosname";
    public static String RESULT_PARTIDOSCODE = "partidoscode";
    public String[] partidosnames, partidoscodes;
    private TypedArray imgs;
    private List<Partidos> partidosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populatePartidosList();
        ArrayAdapter<Partidos> adapter = new PartidosListArrayAdapter(this, partidosList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Partidos p = partidosList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_PARTIDOSNAME, p.getName());
                returnIntent.putExtra(RESULT_PARTIDOSCODE,p.getCode());
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }


    private void populatePartidosList() {
        partidosList = new ArrayList<Partidos>();
        partidosnames = getResources().getStringArray(R.array.partidos_names);
        partidoscodes = getResources().getStringArray(R.array.partidos_codes);
        imgs = getResources().obtainTypedArray(R.array.partidos_flags);
        for(int i = 0; i < partidoscodes.length; i++){
            partidosList.add(new Partidos(partidosnames[i], partidoscodes[i], imgs.getDrawable(i)));
        }
    }




    public class Partidos {
        private String name;
        private String code;
        private Drawable flag;
        public Partidos(String name, String code, Drawable flag){
            this.name = name;
            this.code = code;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }
        public String getCode() {
            return code;
        }
    }
}
