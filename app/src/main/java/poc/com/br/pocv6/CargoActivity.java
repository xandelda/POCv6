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

import classesAdapter.CargoListArrayAdapter;

public class CargoActivity extends ListActivity {

    public static String RESULT_CARGONAME = "cargoname";
    public static String RESULT_CARGOCODE = "cargocode";
    public String[] cargonames, cargocodes;
    private TypedArray imgs;
    private List<Cargo> cargoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateCargoList();
        ArrayAdapter<Cargo> adapter = new CargoListArrayAdapter(this, cargoList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cargo c = cargoList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_CARGONAME, c.getName());
                returnIntent.putExtra(RESULT_CARGOCODE,c.getCode());
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }


    private void populateCargoList() {
        cargoList = new ArrayList<Cargo>();
        cargonames = getResources().getStringArray(R.array.cargo_names);
        cargocodes = getResources().getStringArray(R.array.cargo_codes);
        imgs = getResources().obtainTypedArray(R.array.cargo_flags);
        for(int i = 0; i < cargocodes.length; i++){
            cargoList.add(new Cargo(cargonames[i], cargocodes[i], imgs.getDrawable(i)));
        }
    }




    public class Cargo {
        private String name;
        private String code;
        private Drawable flag;
        public Cargo(String name, String code, Drawable flag){
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
