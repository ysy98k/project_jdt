package com.raising.forward.entity;

import java.util.ArrayList;
import java.util.List;

public class SeriesData {
    String name;
    List data;

    public SeriesData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        List newData = new ArrayList();
        newData.addAll(data);
        this.data = newData;
    }
}
