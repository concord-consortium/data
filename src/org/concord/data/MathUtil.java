/*
 * Created on May 4, 2004
 *
 */
package org.concord.data;

/**
 * @author dima
 *
 */
public final class MathUtil {
    public static boolean equalWithTolerance(float a,float b,float eps){
        if(a == 0) return Math.abs(a) < eps;
        return Math.abs((a-b)/a) < eps;
    }
    public static boolean equalWithTolerance(double a,double b,double eps){
        if(a == 0) return Math.abs(a) < eps;
        return Math.abs((a-b)/a) < eps;
    }
}
