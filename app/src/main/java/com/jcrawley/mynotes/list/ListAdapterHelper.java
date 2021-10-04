package com.jcrawley.mynotes.list;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jcrawley.mynotes.R;
import java.util.List;

import androidx.core.util.Consumer;

public class ListAdapterHelper {

    private final Context context;
    private ListItemArrayAdapter arrayAdapter;
    private final ListView list;
    private final Consumer<ListItem> clickConsumer;
    private final Consumer<ListItem> longClickConsumer;


    public ListAdapterHelper(Context context, ListView list,
                             Consumer<ListItem> clickConsumer,
                             Consumer<ListItem> longClickConsumer){
        this.context = context;
        this.list = list;
        this.clickConsumer = clickConsumer;
        this.longClickConsumer = longClickConsumer;
    }


    public boolean contains(String str){
        for(int i=0; i < arrayAdapter.getCount(); i++){
            ListItem item = arrayAdapter.getItem(i);
            if(item == null){
                continue;
            }
            if(item.getName().equals(str)){
                return true;
            }
        }
        return false;
    }


    public void setupList(final List<ListItem> items, int layoutRes, View noResultsFoundView){
        if(list == null){
            return;
        }
        arrayAdapter = new ListItemArrayAdapter(context, layoutRes, items);

        AdapterView.OnItemLongClickListener longClickListener = (parent, view, position, id) -> {
            if(position < items.size()){
                ListItem item = items.get(position);
                longClickConsumer.accept(item);
                return true;
            }
            return false;
        };


        AdapterView.OnItemClickListener clickListener = (parent, view, position, id) -> {
            if(position >= items.size()){
                return;
            }
            clickConsumer.accept(items.get(position));
        };


        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        list.setAdapter(arrayAdapter);
        list.setSelector(R.color.select_list_item_hidden);
        setupEmptyView(noResultsFoundView);
        list.setOnItemLongClickListener(longClickListener);
        list.setOnItemClickListener(clickListener);
    }


    private void setupEmptyView(View noResultsFoundView){
        if(noResultsFoundView == null){
            return;
        }
        list.setEmptyView(noResultsFoundView);
    }


    public void addToList(ListItem item){
        if (contains(item)) {
            return;
        }
        arrayAdapter.add(item);
    }


    public boolean contains(ListItem item) {
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            ListItem item1 = arrayAdapter.getItem(i);
            if(item1 == null){
                continue;
            }
            String name = item1.getName();
            if (name.equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

    public void deleteFromList(ListItem listItem){
        arrayAdapter.remove(listItem);
    }

    public void clearSelection(){
        list.clearChoices();
        list.clearFocus();
        arrayAdapter.notifyDataSetChanged();
    }
}
