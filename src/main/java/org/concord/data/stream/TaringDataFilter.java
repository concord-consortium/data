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
 * Last modification information:
 * $Revision: 1.3 $
 * $Date: 2006-10-03 21:07:21 $
 * $Author: scytacki $
 *
 * Licence Information
 * Copyright 2004 The Concord Consortium 
*/
package org.concord.data.stream;

import java.util.Vector;

import org.concord.framework.data.stream.DataConsumer;
import org.concord.framework.data.stream.DataListener;
import org.concord.framework.data.stream.DataProducer;
import org.concord.framework.data.stream.DataStreamDescription;
import org.concord.framework.data.stream.DataStreamEvent;

/**
 * TaringDataFilter
 * Class name and description
 *
 * Date created: Jun 2, 2005
 *
 * @author scott<p>
 *
 */
public class TaringDataFilter extends DataProducerFilter
{
    private boolean doingTare = false;
    protected float tareOffset = 0;
        
    /**
     * Do the actual tare.  This will offset the channel that is being
     * tared.  It will probably do this by adding another listener to the 
     * data.  So the tare might take one sample before it takes effect
     *
     */
    public void tare()
    {
    	doingTare = true;
    }

    @Override
    protected float filter(float value) {
        if(doingTare) {
            // save this for the calibration
        	tareOffset = -value;        	
            doingTare = false;
        } 

        return value + tareOffset;
    }    
}
