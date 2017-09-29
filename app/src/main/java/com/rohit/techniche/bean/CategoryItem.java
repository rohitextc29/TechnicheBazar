package com.rohit.techniche.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macbookpro on 28/09/17.
 */

public class CategoryItem implements Serializable{
    private String _id;
    private String name;
    private String type;
    private boolean status;
    private List<ChildrenItem> childrenItemList;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ChildrenItem> getChildrenItemList() {
        return childrenItemList;
    }

    public void setChildrenItemList(List<ChildrenItem> childrenItemList) {
        this.childrenItemList = childrenItemList;
    }

}
