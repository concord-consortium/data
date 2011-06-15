package org.concord.data.state;

import org.concord.framework.otrunk.OTResourceList;

public interface OTDataChannelProducerFilter extends OTDataProducerFilter {
    public OTResourceList getChannelsToDiscard();
}
