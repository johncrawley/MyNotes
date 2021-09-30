package com.jcrawley.mynotes.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;

public class ListItemArrayAdapter extends ArrayAdapter<ListItem> {

    private final Context context;
    private final List<ListItem> items;

    public ListItemArrayAdapter(Context context, int viewResourceId, List<ListItem> items){
        super(context, viewResourceId, items);
        this.context = context;
        this.items = items;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(android.R.layout.simple_list_item_1,null);
        }

        ListItem item = items.get(position);
        if(item == null || item.getName().isEmpty()){
            return view;
        }

        TextView textView = (TextView)view;
        textView.setText(item.getName());
        return view;
    }


}

