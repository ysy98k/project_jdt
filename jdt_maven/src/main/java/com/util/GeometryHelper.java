package com.util;

/**
 * @author ysy
 * @date 2018/6/28 19:03
 * @description
 */

public class GeometryHelper {
    /// <summary>
    /// 计算给定折线的左右偏移线
    /// </summary>
    /// <param name="xs">给定拆线的x坐标数组</param>
    /// <param name="ys">给定拆线的y坐标数组</param>
    /// <param name="offsetL">左偏移量</param>
    /// <param name="offsetR">右偏移量</param>
    /// <returns>第一个元素为左偏移线，第二个元素为右偏移线</returns>



    public static GeoPoly[] getParallel(double[] xs, double[] ys, double offsetL, double offsetR){
        if (xs.length != ys.length || xs.length == 0 || ys.length == 0) {
            try {
            } catch (Exception e) {

            }
        }
            double offsetRX, offsetRY;
            double offsetLX, offsetLY;
            double a1, a2;
            int n = xs.length;

            double[] intersect_leftx = new double[n];
            double[] intersect_lefty = new double[n];
            double[] intersect_rightx = new double[n];
            double[] intersect_righty = new double[n];
            //依次求转角点

            for (int i = 0; i < n; i++) {
                if (i == 0) {
                    a1 = getAngle(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                    offsetRX = -offsetL * Math.sin(a1);
                    offsetRY = offsetL * Math.cos(a1);
                    offsetLX = -offsetR * Math.sin(a1);
                    offsetLY = offsetR * Math.cos(a1);
                } else if (i == n - 1) {
                    a1 = getAngle(xs[i - 1], ys[i - 1], xs[i], ys[i]);
                    offsetRX = -offsetL * Math.sin(a1);
                    offsetRY = offsetL * Math.cos(a1);
                    offsetLX = -offsetR * Math.sin(a1);
                    offsetLY = offsetR * Math.cos(a1);
                } else {
                    a1 = getAngle(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                    a2 = getAngle(xs[i], ys[i], xs[i - 1], ys[i - 1]);
                    offsetRX = offsetL * (Math.cos(a1) + Math.cos(a2)) / (Math.sin(a2 - a1));
                    offsetRY = offsetL * (Math.sin(a1) + Math.sin(a2)) / (Math.sin(a2 - a1));
                    offsetLX = offsetR * (Math.cos(a1) + Math.cos(a2)) / (Math.sin(a2 - a1));
                    offsetLY = offsetR * (Math.sin(a1) + Math.sin(a2)) / (Math.sin(a2 - a1));
                }
                intersect_leftx[i] = xs[i] + offsetLX;
                intersect_lefty[i] = ys[i] + offsetLY;
                intersect_rightx[i] = xs[i] - offsetRX;
                intersect_righty[i] = ys[i] - offsetRY;
            }
            return new GeoPoly[]{new GeoPoly(intersect_leftx, intersect_lefty), new GeoPoly(intersect_rightx, intersect_righty)};
        }

    /// <summary>
    /// 在指定长度比例处打断，返回打断后的两条线
    /// </summary>
    /// <param name="xs"></param>
    /// <param name="ys"></param>
    /// <param name="length"></param>
    /// <returns></returns>
    public static  GeoPoly[] breakByLength(GeoPoly poly, double lRatio){
            double s = poly.getLength();
            GeoPoly[] geoPoly = new GeoPoly[2];
            geoPoly[0] = new GeoPoly();
            geoPoly[1] = new GeoPoly();
            double breakL = s * lRatio;
            double total = 0.0;
            int flag = 0;
            boolean flag2 = false;
            geoPoly[0].addPoint(poly.get_PtArray().get(0));
            for (int i = 1; i < poly.get_PtArray().size(); i++)
            {
                double lsection = Math.sqrt(Math.pow(poly.get_PtArray().get(i).getX() - poly.get_PtArray().get(i-1).getX(), 2)
                        + Math.pow(poly.get_PtArray().get(i).getY() - poly.get_PtArray().get(i-1).getY(), 2));
                total += lsection;
                if (total >= breakL && !flag2)
                {
                    double ratio = 1 - (total - breakL) / lsection;
                    GeoPoint ptInsert = new GeoPoint();
                    ptInsert.setX(poly.get_PtArray().get(i-1).getX() + ratio * (poly.get_PtArray().get(i).getX() - poly.get_PtArray().get(i-1).getX()));
                    ptInsert.setY(poly.get_PtArray().get(i-1).getY() + ratio * (poly.get_PtArray().get(i).getY() - poly.get_PtArray().get(i-1).getY())) ;
                    geoPoly[0].addPoint(ptInsert);
                    geoPoly[1].addPoint(ptInsert);
                    flag = 1;
                    flag2 = true;
                }
                geoPoly[flag].addPoint(poly.get_PtArray().get(i));
            }
            return geoPoly;
        }

    private static  double getAngle(double x1, double y1, double x2, double y2){
        double a = Math.atan2(y2 - y1, x2 - x1);
        if (a < 0){
            a = a + 2 * Math.PI;
        }
        return a;
    }
}


