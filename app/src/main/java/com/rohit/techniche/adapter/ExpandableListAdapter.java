package com.rohit.techniche.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohit.techniche.R;
import com.rohit.techniche.activity.ProceedToCartActivity;
import com.rohit.techniche.bean.ChildrenItem;
import com.rohit.techniche.bean.ProductItem;
import com.rohit.techniche.cart.ProceedToCart;

import java.util.List;

/**
 * Created by macbookpro on 28/09/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private List<ChildrenItem> childrenItemList; // list of expandable list
    private Snackbar snackbar;

    public ExpandableListAdapter(Activity activity, List<ChildrenItem> childrenItemList) {
        this.activity = activity;
        this.childrenItemList = childrenItemList;
    }

    @Override
    public int getGroupCount() {
        return childrenItemList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(childrenItemList.get(groupPosition).getProductItemList() != null){
            return childrenItemList.get(groupPosition).getProductItemList().size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return childrenItemList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(childrenItemList.get(groupPosition).getProductItemList() != null){
            return childrenItemList.get(groupPosition).getProductItemList().get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ChildrenItem childrenItem = (ChildrenItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(childrenItem.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ProductItem productItem = (ProductItem) getChild(groupPosition, childPosition);

        int productcount = ProceedToCart.getParticularProductCount(productItem);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.children_item_row1, null);
        }

        TextView productNameTextView = (TextView) convertView
                .findViewById(R.id.productNameTextView);

        TextView productPriceTextView = (TextView) convertView
                .findViewById(R.id.productPriceTextView);

        final TextView countProductTextView = (TextView) convertView
                .findViewById(R.id.countProductTextView);

        final ImageView removeProductImageView = (ImageView) convertView
                .findViewById(R.id.removeProductImageView);

        final ImageView addProductImageView = (ImageView) convertView
                .findViewById(R.id.addProductImageView);

        if(productcount == 0){
            removeProductImageView.setVisibility(View.INVISIBLE);
            hideSnackBar();
            countProductTextView.setText("");
        }else{
            removeProductImageView.setVisibility(View.VISIBLE);
            displaySnackBar();
            countProductTextView.setText(String.valueOf(ProceedToCart.getParticularProductCount(productItem)));
        }

        removeProductImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProceedToCart.removeProductToList(productItem);
                int count = ProceedToCart.getParticularProductCount(productItem);
                if(count == 0){
                    removeProductImageView.setVisibility(View.INVISIBLE);
                    hideSnackBar();
                    countProductTextView.setText("");
                }else{
                    removeProductImageView.setVisibility(View.VISIBLE);
                    displaySnackBar();
                    countProductTextView.setText(String.valueOf(ProceedToCart.getParticularProductCount(productItem)));
                }
            }
        });

        addProductImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProceedToCart.addProductToList(productItem);
                removeProductImageView.setVisibility(View.VISIBLE);
                displaySnackBar();
                countProductTextView.setText(String.valueOf(ProceedToCart.getParticularProductCount(productItem)));
            }
        });

        productNameTextView.setText(productItem.getName());
        productPriceTextView.setText(activity.getString(R.string.rs)+" "+String.valueOf(productItem.getAmount()+productItem.getTax()));
        countProductTextView.setText("0");

        return convertView;
    }

    public void displaySnackBar(){
        if(snackbar != null){
            snackbar.dismiss();
        }
        String message  = getSnackBarMessage();
        snackbar = Snackbar.make(activity.findViewById(R.id.linearLayout), message, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(0, 0, 0, 0); //set padding to 0
        View snackbarView = snackbar.getView();
        Button snackbar_action = (Button) snackbarView.findViewById(android.support.design.R.id.snackbar_action);
        snackbar_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProceedToCartActivity.class);
                activity.startActivity(intent);
            }
        });
        snackbar.show();
    }

    public void hideSnackBar(){
        if(snackbar != null){
            snackbar.dismiss();
        }
    }

    public String getSnackBarMessage(){
        String message = "";
        int totalItemInCart = ProceedToCart.getCartItemTotalCount();
        double totalCartAmount = ProceedToCart.getCartItemTotalPrice();
        if(totalItemInCart < 2){
            message = totalItemInCart+" item in Cart "+activity.getString(R.string.rs)+""+totalCartAmount;
        }else{
            message = totalItemInCart+" items in Cart "+activity.getString(R.string.rs)+""+totalCartAmount;
        }
        return message;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
