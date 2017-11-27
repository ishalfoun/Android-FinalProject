package dawson.dawsondangerousclub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dawson.dawsondangerousclub.ClassMenuFragment;

public class ClassAdapter extends BaseAdapter {
	
	private Context context;
    ArrayList<Entry> classList;
    LayoutInflater inflater;
    private ClassMenuFragment.OnItemSelectedListener listener;

//    public ClassAdapter(Context c, String [] listClassTitle,String [] listClassDescription,
//                        String [] listClassName, String [] listClassTeacher, String [] listClassNotes,
//                        String[] listClassPubDate, ClassMenuFragment.OnItemSelectedListener listener){
//        context = c;
//        this.listClassTitle = listClassTitle;
//        this.listClassDescription = listClassDescription;
//        this.listClassName = listClassName;
//        this.listClassTeacher = listClassTeacher;
//        this.listClassNotes = listClassNotes;
//        this.listClassPubDate = listClassPubDate;
//        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.listener = listener;
//    }
    public ClassAdapter(Context c, ArrayList<Entry> classList, ClassMenuFragment.OnItemSelectedListener listener){
        context = c;
        this.classList=classList;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    //

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        TextView tv; 
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


}





