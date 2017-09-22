package ingeniumbd.com.myapplication2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<Blog> songlists;

    public CustomAdapter(Context c, ArrayList<Blog> songlists) {
        this.c = c;
        this.songlists = songlists;
    }
    @Override
    public int getCount() {
        return songlists.size();
    }
    @Override
    public Object getItem(int position) {
        return songlists.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.data_row,parent,false);
        }
        TextView title= (TextView) convertView.findViewById(R.id.title);

        final Blog s= (Blog) this.getItem(position);
        title.setText(s.getTitle());
        return convertView;
    }
}