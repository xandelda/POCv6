package classesAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import poc.com.br.pocv6.CargoActivity;
import poc.com.br.pocv6.R;

/**
 * Created by JOAO on 02/06/2016.
 */
public class CargoListArrayAdapter extends ArrayAdapter<CargoActivity.Cargo> {
    private final List<CargoActivity.Cargo> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView flag;
    }

    public CargoListArrayAdapter(Activity context, List<CargoActivity.Cargo> list) {
        super(context, R.layout.activity_cargo, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_cargo, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.nameCargo);
            viewHolder.flag = (ImageView) view.findViewById(R.id.flagCargo);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getFlag());
        return view;
    }
}
