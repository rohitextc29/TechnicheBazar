package com.rohit.techniche.adapter;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohit.techniche.R;
import com.rohit.techniche.bean.ProductItem;
import com.rohit.techniche.cart.ProceedToCart;

import java.util.List;

/**
 * Created by macbookpro on 29/09/17.
 */

public class CustomAdapterProceedToCart extends BaseAdapter {

    private List<ProductItem> productItemList;
    private LayoutInflater inflater;
    private Activity activity;
    private Snackbar snackbar;
    private TextView paymentCartTextView,totalpaymentCartTextView;

    public CustomAdapterProceedToCart(Activity activity, List<ProductItem> productItemList) {
        this.productItemList = productItemList;
        this.activity = activity;
        inflater = LayoutInflater.from(this.activity);
        paymentCartTextView = (TextView) activity.findViewById(R.id.paymentCartTextView);
        totalpaymentCartTextView = (TextView) activity.findViewById(R.id.totalpaymentCartTextView);
    }

    @Override
    public int getCount() {
        return productItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return productItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.proceed_to_cart_item1, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final ProductItem productItem = (ProductItem) getItem(position);

        int productcount = ProceedToCart.getParticularProductCount(productItem);

        mViewHolder.productNameTextView.setText(productItem.getName());
//        mViewHolder.productItemCountTextView.setText(String.valueOf(ProceedToCart.getParticularProductCount(productItem)));


        mViewHolder.removeProductImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProceedToCart.removeProductToList(productItem);
                int count = ProceedToCart.getParticularProductCount(productItem);
                if(count == 0){
                    mViewHolder.removeProductImageView.setVisibility(View.INVISIBLE);
                    hideSnackBar();
                    mViewHolder.productItemCountTextView.setText("0");
                    mViewHolder.productPriceTextView.setText("0");
                    mViewHolder.totalProductPriceTextView.setText("0");
                }else{
                    mViewHolder.removeProductImageView.setVisibility(View.VISIBLE);
                    displaySnackBar();
                    mViewHolder.productItemCountTextView.setText(String.valueOf(ProceedToCart.getParticularProductCount(productItem)));
                    mViewHolder.productPriceTextView.setText(String.valueOf(productItem.getAmount()+productItem.getTax()));
                    mViewHolder.totalProductPriceTextView.setText(String.valueOf(ProceedToCart.getParticularProductTotalPrice(productItem)));
                }
                changeTotalPriceForCheckout();
            }
        });

        mViewHolder.addProductImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProceedToCart.addProductToList(productItem);
                mViewHolder.removeProductImageView.setVisibility(View.VISIBLE);
                displaySnackBar();
                mViewHolder.productItemCountTextView.setText(String.valueOf(ProceedToCart.getParticularProductCount(productItem)));
                mViewHolder.productPriceTextView.setText(String.valueOf(productItem.getAmount()+productItem.getTax()));
                mViewHolder.totalProductPriceTextView.setText(String.valueOf(ProceedToCart.getParticularProductTotalPrice(productItem)));
                changeTotalPriceForCheckout();
            }
        });

        if(productcount == 0){
            mViewHolder.removeProductImageView.setVisibility(View.INVISIBLE);
            hideSnackBar();
            mViewHolder.productItemCountTextView.setText("0");
            mViewHolder.productPriceTextView.setText("0");
            mViewHolder.totalProductPriceTextView.setText("0");
        }else{
            mViewHolder.removeProductImageView.setVisibility(View.VISIBLE);
            displaySnackBar();
            mViewHolder.productItemCountTextView.setText(String.valueOf(ProceedToCart.getParticularProductCount(productItem)));
            mViewHolder.productPriceTextView.setText(String.valueOf(productItem.getAmount()+productItem.getTax()));
            mViewHolder.totalProductPriceTextView.setText(String.valueOf(ProceedToCart.getParticularProductTotalPrice(productItem)));
        }
        changeTotalPriceForCheckout();

        return convertView;
    }

    public void changeTotalPriceForCheckout(){
        paymentCartTextView.setText(String.valueOf(ProceedToCart.getCartItemTotalPrice()));
        totalpaymentCartTextView.setText(String.valueOf(ProceedToCart.getCartItemTotalPrice()));
    }

    public void displaySnackBar(){
        if(snackbar != null){
            snackbar.dismiss();
        }
        String message  = getSnackBarMessage();
        snackbar = Snackbar.make(activity.findViewById(R.id.linearLayout), message, Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setPadding(0, 0, 0, 0); //set padding to 0
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

    private class MyViewHolder {
        TextView productNameTextView, productItemCountTextView, productPriceTextView, totalProductPriceTextView;
        ImageView addProductImageView, removeProductImageView;

        public MyViewHolder(View item) {
            productNameTextView = (TextView) item.findViewById(R.id.productNameTextView);
            productItemCountTextView = (TextView) item.findViewById(R.id.productItemCountTextView);
            productPriceTextView = (TextView) item.findViewById(R.id.productPriceTextView);
            totalProductPriceTextView = (TextView) item.findViewById(R.id.totalProductPriceTextView);
            addProductImageView = (ImageView) item.findViewById(R.id.addProductImageView);
            removeProductImageView = (ImageView) item.findViewById(R.id.removeProductImageView);
        }
    }

}
