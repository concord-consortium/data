

/*
 *  Copyright (C) 2004  The Concord Consortium, Inc.,
 *  10 Concord Crossing, Concord, MA 01741
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
 */
package org.concord.data;

import java.util.Vector;

import org.concord.data.event.ConditionEvent;
import org.concord.data.event.ConditionListener;

/**
 * @author Edward Burke
 *
 * The ConditionValue class will evaluate and monitor a set of indexed
 * values retrieved from its Data interface. Any class that implements
 * ConditionValue.Data can be used by a ConditionValue instance as a
 * source. The ConditionValue instance can then be set with one of its
 * internal functions  - Single, Total, Mean, Sigma and Count -
 * to calculate aggregate values for the data.
 * .
 * Appropriate adjustment of range values can result in events generated
 * when the calculated values fall within the specified ranges.
 */
public class ConditionValue
{
	// This defines the Predicate interface used to check whether
	// event notification should occur. An external implementation
	// of this interface can be set on an instance of ConditionValue
	// by using the setPredicate method.
	public interface Predicate
	{
		public boolean check(double value);
	}
	public interface Function
	{
		public double evaluate(double [] valueArray);
	}
	public interface Data
	{
		public double getValue(int index);
		public int getNumberOfValues();
	}
    public static final int NO_INTERVAL = -1;
    protected boolean notifying = false;
    protected Data data;
    protected int[] indices;
    protected int[] allIndices;
    protected int runningCount = 0;
    protected double runningMean = 0.0;
    protected boolean running = false;
    protected double min = Double.MIN_VALUE;
    protected double max = Double.MAX_VALUE;
    protected double[] values;
    protected boolean negate = false;
    protected int type = 0;
    protected int timedAverageInterval = NO_INTERVAL;
    protected int[] singleIndex = { 0 };
    protected Vector listeners = new Vector();
    protected Vector valueList;
    protected ConditionEvent conditionEvent = new ConditionEvent(this);
	
	
    public final Function Single = new Function()
	{
		public double evaluate(double [] valueArray)
		{
			int n = valueArray.length;
			if (n == 0)
				return 0.0;
			return valueArray[0];
		}
	};
    public final Function Total = new Function()
	{
		public double evaluate(double [] valueArray)
		{
			int n = valueArray.length;
			double value = 0.0;
			for (int i = 0; i < n; i++)
				value += valueArray[i];
			return value;
		}
	};
    public final Function Mean = new Function()
	{
		public double evaluate(double [] valueArray)
		{
			int n = valueArray.length;
			if (n == 0)
				return 0.0;
			return Total.evaluate(valueArray) / (double) n;
		}
	};
    public final Function Sigma = new Function()
	{
		public double evaluate(double [] valueArray)
		{
			int n = valueArray.length;
			double m = Mean.evaluate(valueArray);
			double value = 0.0;
			for (int i = 0; i < n; i++)
			{
				double diff = valueArray[i] - m;
				value += diff * diff;
			}
			return Math.sqrt(value / (double) n);
		}
	};
    public Function Count = new Function()
	{
		public double evaluate(double [] valueArray)
		{
			int n = valueArray.length;
			double value = 0.0;
			for (int i = 0; i < n; i++)
			{
				if (predicate.check(valueArray[i]))
					value++;
			}
			return value;
		}
	};
	protected Function function = Single;
	
	Function [] functions = { Single, Total, Mean, Sigma, Count };
	public final static int SINGLE = 0;
	public final static int TOTAL = SINGLE + 1;
	public final static int MEAN = TOTAL + 1;
	public final static int SIGMA = MEAN + 1;
	public final static int COUNT = SIGMA + 1;
	
	public final Predicate InsideRange = new Predicate()
	{
		public boolean check(double value)
		{
			return value <= max && value >= min;
		}
	};
	public final Predicate OutsideRange = new Predicate()
	{
		public boolean check(double value)
		{
			return ! InsideRange.check(value);
		}
	};
    protected Predicate predicate = InsideRange;
	
    public double getValue()
	{
		int n = data.getNumberOfValues();
		if (indices == null)
		{
			if (allIndices == null || allIndices.length != n)
			{
				allIndices = new int[n];
				for (int i = 0; i < n; i++)
					allIndices[i] = i;
			}
			indices = allIndices;
		}
		n = indices.length;
		if (values == null || values.length != n)
			values = new double[n];
		for (int i = 0; i < values.length; i++)
			values[i] = data.getValue(indices[i]);
		return getValue(values);
    }
    
    protected double getValue(double[] valueArray)
	{
		double value = function.evaluate(valueArray);
		if (running)
		{
			if (runningCount >= timedAverageInterval && timedAverageInterval > 0 && valueList instanceof Vector)
			{
				double last = ((Double) valueList.lastElement()).doubleValue();
				valueList.removeElementAt(valueList.size() - 1);
				runningMean = (((double) runningCount * runningMean - last) / (double) (runningCount - 1));
				runningCount--;
			}
			if (runningCount < timedAverageInterval	|| timedAverageInterval < 0)
			{
				runningMean = ((value + (double) runningCount * runningMean) / (double) (runningCount + 1));
				if (valueList instanceof Vector)
					valueList.insertElementAt(new Double(value), 0);
				value = runningMean;
				runningCount++;
			}
		}
		if (predicate.check(value))
			notifyConditionListeners(conditionEvent);
		return value;
    }
    
    public void setData(Data data)
    {
    	this.data = data;
    }
    
    public void setIndex(int i)
	{
		singleIndex[0] = i;
		setIndices(singleIndex);
    }
    
    public double getMax()
	{
		return max;
    }
    
    public void setMax(double d)
	{
		max = d;
    }
    
    public double getMin()
	{
		return min;
    }
    
    public void setMin(double d)
	{
		min = d;
    }
    
    public void setNegate(boolean bool)
	{
		if (bool)
			setPredicate(OutsideRange);
		else
			setPredicate(InsideRange);
    }
    
    public void setRange(double lower, double upper)
	{
		setMin(lower);
		setMax(upper);
    }
    
    public void resetRange()
	{
		setMin(Double.MIN_VALUE);
		setMax(Double.MAX_VALUE);
    }
	
	public void setType(int type)
	{
		if ((type >= 0) && (type < functions.length))
			setFunction(functions[type]);
	}
    
    public void setFunction(Function f)
	{
		function = f;
    }
    
    public void setPredicate(Predicate p)
	{
		predicate = p;
    }
    
    public void setIndices(int min, int max)
	{
    	int length = max - min;
    	indices = new int[length];
    	for (int i = 0; i < length; i++)
    		indices[i] = min + i;
    }
    
    public void setIndices(int[] is)
	{
    	indices = is;
    }
    
    public void setIndices(Vector vector)
	{
		if (vector != null)
		{
			indices = new int[vector.size()];
			for (int i = 0; i < indices.length; i++)
			indices[i] = ((Integer) vector.elementAt(i)).intValue();
		}
		else
			indices = null;
    }
    
    public boolean isTimedAverage()
	{
		return running;
    }
    
    public void resetTimedAverage()
	{
		runningCount = 0;
		runningMean = 0.0;
		if (valueList instanceof Vector)
			valueList.removeAllElements();
    }
    
    public void setTimedAverageInterval(int i)
	{
		timedAverageInterval = i;
		boolean test = i > 0;
		valueList = test ? new Vector() : null;
		resetTimedAverage();
		running = test || i < 0;
    }
    
    public void setTimedAverage(boolean test)
	{
		setTimedAverageInterval(test ? NO_INTERVAL : 0);
    }
    
    public void addConditionListener(ConditionListener conditionlistener)
	{
		listeners.addElement(conditionlistener);
    }
    
    public void removeConditionListener(ConditionListener conditionlistener)
	{
		listeners.removeElement(conditionlistener);
    }
    
    protected void notifyConditionListeners(ConditionEvent conditionevent)
	{
		if (!notifying)
		{
			notifying = true;
			for (int i = 0; i < listeners.size(); i++)
			{
				ConditionListener conditionlistener	= (ConditionListener) listeners.elementAt(i);
				conditionlistener.conditionUpdate(conditionevent);
			}
			notifying = false;
		}
    }
}
