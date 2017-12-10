package dawson.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dawson.fragments.ClassMenuFragment;
import dawson.dawsondangerousclub.R;

/**
 * Adapter for the Classes. (used by Cancelled Classes Activity)
 * @author Isaak
 */
public class ClassesAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Entry> classList;
    LayoutInflater inflater;
    private ClassMenuFragment.OnItemSelectedListener listener;

    public ClassesAdapter(Context c, ArrayList<Entry> classList, ClassMenuFragment.OnItemSelectedListener listener) {
        context = c;
        this.classList = classList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return (classList != null ? classList.size() : 0);
    }

    @Override
    public Object getItem(int i) {
        return classList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder vh = new ViewHolder();
        View row = view;

        if (view == null) {
            row = inflater.inflate(R.layout.cancel_list, null);

            vh.tv = (TextView) row.findViewById(R.id.classTitle);
            vh.tv.setText(classList.get(position).course);
            row.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
            vh.tv.setText(classList.get(position).course);
        }

        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onClassItemSelected(position);
            }
        });

        return row;
    }

    public class ViewHolder {
        TextView tv;
    }


}





