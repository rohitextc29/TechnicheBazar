package com.rohit.techniche.parser;

import com.rohit.techniche.bean.CategoryItem;
import com.rohit.techniche.bean.ChildrenItem;
import com.rohit.techniche.bean.ProductItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 28/09/17.
 */

public class TechicheJSONParser {
    static List<CategoryItem> categoryItemList;
    public static List<CategoryItem> parseData(String content, String currentCity) {
        JSONArray categoryArray = null;
        CategoryItem categoryItem = null;
        try {

            categoryArray = new JSONArray(content);
            categoryItemList = new ArrayList<>();

            for (int i = 0; i < categoryArray.length(); i++) {

                JSONObject category = categoryArray.getJSONObject(i);
                categoryItem = new CategoryItem();

                categoryItem.set_id(category.getString("_id"));
                categoryItem.setName(category.getString("name"));
                categoryItem.setStatus(category.getBoolean("status"));
                categoryItem.setType(category.getString("type"));

                System.out.println("_id"+category.getString("_id"));

                List<ChildrenItem> childrenItemList = new ArrayList<>();

                JSONArray childrensArray = category.getJSONArray("childrens");
                for(int j=0; j < childrensArray.length(); j++){
                    JSONObject children = childrensArray.getJSONObject(j);
                    ChildrenItem childrenItem = new ChildrenItem();

                    childrenItem.set_id(children.getString("_id"));
                    childrenItem.setName(children.getString("name"));
                    childrenItem.setParent(children.getString("parent"));
                    childrenItem.setType(children.getString("type"));
                    childrenItem.setStatus(children.getBoolean("status"));

                    JSONArray productsArray = children.getJSONArray("products");

                    List<ProductItem> productItemList = new ArrayList<>();

                    for(int k=0; k < productsArray.length(); k++){
                        JSONObject products = productsArray.getJSONObject(k);
                        ProductItem productItem = new ProductItem();

                        productItem.set_id(products.getString("_id"));
                        productItem.setName(products.getString("name"));
                        productItem.setCategory(products.getString("category"));
                        productItem.setCode(products.getInt("code"));
                        productItem.setImage(products.getString("image"));

                        JSONArray pricesArray = products.getJSONArray("prices");
                        for(int l=0; l < pricesArray.length(); l++){
                            JSONObject prices = pricesArray.getJSONObject(l);
                            String region = prices.getString("region");
                            if(region.equalsIgnoreCase(currentCity)){
                                productItem.setAmount(prices.getDouble("amount"));
                                productItem.setTax(prices.getDouble("tax"));
                            }
                        }
                        productItemList.add(k,productItem);
                    }

                    childrenItem.setProductItemList(productItemList);
                    childrenItemList.add(j,childrenItem);
                }

                categoryItem.setChildrenItemList(childrenItemList);

                categoryItemList.add(categoryItem);
            }
            return categoryItemList;

        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
