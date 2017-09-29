package com.rohit.techniche.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rohit.techniche.R;
import com.rohit.techniche.adapter.CustomeAdapterListViewForExpandableListView;
import com.rohit.techniche.adapter.ExpandableListAdapter;
import com.rohit.techniche.bean.CategoryItem;
import com.rohit.techniche.cart.ProceedToCart;


public class PageFragment extends Fragment {
    private static final String ARG_CATEGORY = "categoryItem";
    private ExpandableListAdapter listAdapter;
//    private Snackbar snackbar;
//    private int cartTotalCount = 0;

    public PageFragment() {
    }

    public static PageFragment newInstance(CategoryItem categoryItem) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY,categoryItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.children_item_expandable_list, container, false);

//        ListView menuItemListView = (ListView) rootView.findViewById(R.id.menuItemListView);
        final CategoryItem categoryItem = (CategoryItem) getArguments().getSerializable(ARG_CATEGORY);
//        TextView textView = (TextView) rootView.findViewById(R.id.textView);
//        textView.setText(categoryItem.getName());

//        CustomeAdapterListViewForExpandableListView expandableListAdapter = new CustomeAdapterListViewForExpandableListView(getActivity(),categoryItem.getChildrenItemList());
//        menuItemListView.setAdapter(expandableListAdapter);

//        cartTotalCount = ProceedToCart.getCartItemTotalCount();

        ExpandableListView childrenExpandableListView = (ExpandableListView) rootView.findViewById(R.id.childrenExpandableListView);

        listAdapter = new ExpandableListAdapter(getActivity(), categoryItem.getChildrenItemList());
        listAdapter.notifyDataSetChanged();
        // setting list adapter
        childrenExpandableListView.setAdapter(listAdapter);

        // Listview on child click listener
//        childrenExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getActivity(),
//                        categoryItem.getChildrenItemList().get(groupPosition)
//                                + " : "
//                                + categoryItem.getChildrenItemList().get(groupPosition).getProductItemList().get(childPosition), Toast.LENGTH_SHORT)
//                        .show();
//                return false;
//            }
//        });

        childrenExpandableListView.setDividerHeight(10);


//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        builder.append("My message ").append(" ");
//        builder.setSpan(new ImageSpan(getActivity(), R.drawable.location_icon), builder.length() - 1, builder.length(), 0);
//        builder.append(" next message");
//        snackbar = Snackbar.make(getActivity().findViewById(R.id.linearLayout), "2 items has been taken", Snackbar.LENGTH_INDEFINITE);
//        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
//        Snackbar.make(getActivity().findViewById(R.id.linearLayout), "2 items has been taken", Snackbar.LENGTH_LONG).show();
//        layout.setPadding(0, 0, 0, 0); //set padding to 0
//        snackbar.show();

        return rootView;
    }
}
