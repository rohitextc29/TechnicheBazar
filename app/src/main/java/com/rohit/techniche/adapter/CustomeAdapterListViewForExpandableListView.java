package com.rohit.techniche.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.rohit.techniche.R;
import com.rohit.techniche.bean.ChildrenItem;

import java.util.List;

/**
 * Created by macbookpro on 28/09/17.
 */

public class CustomeAdapterListViewForExpandableListView extends ArrayAdapter {

    private Activity activity;
    private List<ChildrenItem> childrenItemList;

    public CustomeAdapterListViewForExpandableListView(Activity activity, List<ChildrenItem> childrenItemList) {
        super(activity, R.layout.children_item_expandable_list, childrenItemList);
        this.activity = activity;
        this.childrenItemList = childrenItemList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRow = layoutInflater.inflate(R.layout.children_item_expandable_list, null,true);

        ExpandableListView childrenExpandableListView = (ExpandableListView) viewRow.findViewById(R.id.childrenExpandableListView);


        return viewRow;
    }

}
