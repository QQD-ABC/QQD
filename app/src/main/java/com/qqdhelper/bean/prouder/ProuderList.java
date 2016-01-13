package com.qqdhelper.bean.prouder;

import com.qqdhelper.bean.BaseBean;

import java.util.List;

public class ProuderList extends BaseBean {
    private List<ProuderItem> a;


    public void setA(List<ProuderItem> a) {
        this.a = a;
    }

    public List<ProuderItem> getA() {
        return this.a;
    }
}
