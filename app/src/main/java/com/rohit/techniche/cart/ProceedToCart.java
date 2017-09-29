package com.rohit.techniche.cart;

import com.rohit.techniche.bean.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by macbookpro on 28/09/17.
 */

public class ProceedToCart {
    private static List<ProductItem> productItemList = new ArrayList<>();
    private static HashMap<ProductItem,Integer> cartItemCountHashMap = new HashMap<>();

    public static void addProductToList(ProductItem productItem){

        if(cartItemCountHashMap.containsKey(productItem)){
            cartItemCountHashMap.put(productItem,cartItemCountHashMap.get(productItem)+1);
        }else{
            productItemList.add(productItem);
            cartItemCountHashMap.put(productItem,1);
        }
    }

    public static void removeProductToList(ProductItem productItem){
        int currentProductCount = cartItemCountHashMap.get(productItem);
        if(currentProductCount > 0) {
            if(currentProductCount == 1){
                cartItemCountHashMap.remove(productItem);
                productItemList.remove(productItem);
            }else{
                cartItemCountHashMap.put(productItem,currentProductCount - 1);
            }
        }
    }

    public static List<ProductItem> getCartItems(){
        return productItemList;
    }

    public static int getCartItemTotalCount(){
        int totalCount=0;
        for (Map.Entry<ProductItem, Integer> entry : cartItemCountHashMap.entrySet()) {
            ProductItem productItem = entry.getKey();
            int count = entry.getValue();
            totalCount += count;
        }
        return totalCount;
    }

    public static int getParticularProductCount(ProductItem productItem){
        if(cartItemCountHashMap.containsKey(productItem)){
            return cartItemCountHashMap.get(productItem);
        }
        return 0;
    }

    public static double getCartItemTotalPrice(){
        double totalAmount = 0.0;
        for (Map.Entry<ProductItem, Integer> entry : cartItemCountHashMap.entrySet()) {
            ProductItem productItem = entry.getKey();
            int count = entry.getValue();
            double amount = productItem.getAmount()+productItem.getTax();
            totalAmount += (count * amount);
        }
        return totalAmount;
    }

    public static double getParticularProductTotalPrice(ProductItem productItem){
        if(cartItemCountHashMap.containsKey(productItem)){
            int count = cartItemCountHashMap.get(productItem);
            double amount = productItem.getAmount()+productItem.getTax();
            double totalAmount = count * amount;
            return totalAmount;
        }
        return 0.0;
    }

}
