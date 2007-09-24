/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01742
 *
 *  Web Site: http://www.concord.org
 *  Email: info@concord.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * END LICENSE */

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
        if(a == 0) return Math.abs(b) < eps;
        return Math.abs((a-b)/a) < eps;
    }
    public static boolean equalWithTolerance(double a,double b,double eps){
        if(a == 0) return Math.abs(b) < eps;
        return Math.abs((a-b)/a) < eps;
    }
    public static boolean equals(float a, float b){
    	if (b == 0) return equals(a+1, b+1);
    	float c = a/b;
    	return equalWithTolerance(c, 1, 0.00001);
    }
    public static boolean equals(double a, double b){
    	if (b == 0) return equals(a+1, b+1);
    	double c = a/b;
    	return equalWithTolerance(c, 1, 0.00001);
    }
    
	/**
	 * Round a float to a particular power of 10
	 * 
	 * @param val	float value to display
	 */
	public static float roundPower10(float val, int powerOf10)
	{
		double precision = Math.pow(10, powerOf10);
		double mantisa = Math.floor((val / precision ) + 0.5);
		float value = (float)(mantisa * precision);
		return value;
	}

	
}
