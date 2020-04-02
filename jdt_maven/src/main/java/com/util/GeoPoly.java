package com.util;


import java.util.ArrayList;

/**
 * @author ysy
 * @date 2018/6/28 14:02
 * @description
 */

public class GeoPoly {

    public GeoPoly()
    {

    }

    private ArrayList<GeoPoint> _ptArray = new ArrayList<GeoPoint>();

    public ArrayList<GeoPoint> PtArray;

    public ArrayList<GeoPoint> getPtArray() {
        return PtArray;
    }

    public ArrayList<GeoPoint> get_PtArray() {
        return _ptArray;
    }

    public void set_PtArray(ArrayList<GeoPoint> ptArray) {
        this._ptArray = ptArray;
    }

    private double Length;

    public double getLength() {
        double s = 0.0;
        for (int i = 1; i < _ptArray.size(); i++ )
        {
            /*s += Math.sqrt(Math.pow(_ptArray.set(i,geoPoint.getX()) - _ptArray.set(i-1,geoPoint.getX()), 2)
                + Math.pow(_ptArray.set(i,geoPoint.getY()) - _ptArray.set(i-1,geoPoint.getY()), 2));*/

            s += Math.sqrt(Math.pow(_ptArray.get(i).getX() - _ptArray.get(i-1).getX(), 2)
                    + Math.pow(_ptArray.get(i).getY() -_ptArray.get(i-1).getY(), 2));
    }
        return s;
    }

    public void setLength(double length) {
        Length = length;
    }

    public GeoPoly(double[] xs, double[] ys)
    {
        for (int i = 0; i < xs.length; i++)
        {
            addPoint(xs[i], ys[i]);
        }
    }

    public GeoPoly(ArrayList<GeoPoint> pts)
    {
        _ptArray = pts;
    }

    public void addPoint(GeoPoint pt)
    {
        _ptArray.add(pt);
    }
    public void addPoint(double x, double y)
    {
        addPoint(new GeoPoint(x, y));
    }
}
