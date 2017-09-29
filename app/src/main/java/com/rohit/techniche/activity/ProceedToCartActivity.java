package com.rohit.techniche.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.rohit.techniche.R;
import com.rohit.techniche.adapter.CustomAdapterProceedToCart;
import com.rohit.techniche.bean.ProductItem;
import com.rohit.techniche.cart.ProceedToCart;

import java.util.List;

/**
 * Created by macbookpro on 29/09/17.
 */

public class ProceedToCartActivity extends AppCompatActivity {

    private ListView cartListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceed_cart_layout);
        cartListView = (ListView) findViewById(R.id.cartListView);
        List<ProductItem> productItemList = ProceedToCart.getCartItems();
        cartListView.setAdapter(new CustomAdapterProceedToCart(ProceedToCartActivity.this, productItemList));
        getSupportActionBar().setTitle("My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
