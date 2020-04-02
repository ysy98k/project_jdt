package com.util;

import org.springframework.stereotype.Component;

/**
 * @author ysy
 * @date 2018/6/28 13:51
 * @description
 */

public class GeoPoint {
    private double X;
    private double Y;

   public GeoPoint(){

   }

    public GeoPoint(double x,double y){
        X = x;
        Y = y;
    }
    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }
}
