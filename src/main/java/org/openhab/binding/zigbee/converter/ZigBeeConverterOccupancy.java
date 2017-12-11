/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zigbee.converter;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.zigbee.ZigBeeBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zsmartsystems.zigbee.ZigBeeEndpoint;
import com.zsmartsystems.zigbee.zcl.ZclAttribute;
import com.zsmartsystems.zigbee.zcl.ZclAttributeListener;
import com.zsmartsystems.zigbee.zcl.ZclCommand;
import com.zsmartsystems.zigbee.zcl.ZclCommandListener;
import com.zsmartsystems.zigbee.zcl.clusters.ZclBasicCluster;
import com.zsmartsystems.zigbee.zcl.clusters.ZclOccupancySensingCluster;
import com.zsmartsystems.zigbee.zcl.clusters.onoff.OffCommand;
import com.zsmartsystems.zigbee.zcl.clusters.onoff.OnCommand;
import com.zsmartsystems.zigbee.zcl.protocol.ZclClusterType;

/**
 * Converter for the occupancy sensor.
 *
 * @author Chris Jackson - Initial Contribution
 *
 */
public class ZigBeeConverterOccupancy extends ZigBeeBaseChannelConverter
        implements ZclAttributeListener, ZclCommandListener {
    private Logger logger = LoggerFactory.getLogger(ZigBeeConverterOccupancy.class);

    private ZclOccupancySensingCluster clusterOccupancy;
    private String channelLabel = "Occupancy";

    private boolean initialised = false;

    @Override
    public void initializeConverter() {
        if (initialised == true) {
            return;
        }
        logger.debug("{}: Initialising device occupancy cluster", endpoint.getIeeeAddress());

        clusterOccupancy = (ZclOccupancySensingCluster) endpoint.getInputCluster(ZclOccupancySensingCluster.CLUSTER_ID);
        if (clusterOccupancy == null) {
            logger.error("{}: Error opening occupancy cluster", endpoint.getIeeeAddress());
            return;
        }

        clusterOccupancy.bind();

        // Add a listener, then request the status
        clusterOccupancy.addAttributeListener(this);
        clusterOccupancy.addCommandListener(this);

        clusterOccupancy.getOccupancy(0);

        // Configure reporting - no faster than once per second - no slower than 10 minutes.
        clusterOccupancy.setOccupancyReporting(1, 600);
        initialised = true;
    }

    @Override
    public void disposeConverter() {
        if (initialised == false) {
            return;
        }

        logger.debug("{}: Closing device occupancy cluster", endpoint.getIeeeAddress());

        if (clusterOccupancy != null) {
            clusterOccupancy.removeAttributeListener(this);
        }
    }

    @Override
    public void handleRefresh() {
        if (initialised == false) {
            return;
        }
    }

    @Override
    public Channel getChannel(ThingUID thingUID, ZigBeeEndpoint endpoint) {
        if (endpoint.getInputCluster(ZclOccupancySensingCluster.CLUSTER_ID) == null) {
            return null;
        }

        ZclBasicCluster basicCluster = (ZclBasicCluster) endpoint.getInputCluster(ZclBasicCluster.CLUSTER_ID);
        if (basicCluster == null) {
            logger.error("{}: Error opening device baisic controls", endpoint.getIeeeAddress());
        }

        // Get cluster location descriptor for channel label
        if (basicCluster != null) {
            channelLabel = basicCluster.getLocationDescription(0);
        }

        return createChannel(thingUID, endpoint, ZigBeeBindingConstants.CHANNEL_OCCUPANCY_SENSOR,
                ZigBeeBindingConstants.ITEM_TYPE_SWITCH, channelLabel);
    }

    @Override
    public void attributeUpdated(ZclAttribute attribute) {
        logger.debug("{}: ZigBee attribute reports {}", endpoint.getIeeeAddress(), attribute);
        if (attribute.getCluster() == ZclClusterType.OCCUPANCY_SENSING
                && attribute.getId() == ZclOccupancySensingCluster.ATTR_OCCUPANCY) {
            Integer value = (Integer) attribute.getLastValue();
            if (value != null && value == 1) {
                updateChannelState(OnOffType.ON);
            } else {
                updateChannelState(OnOffType.OFF);
            }
        }
    }

    @Override
    public void commandReceived(ZclCommand command) {
        OnOffType state = null;
        if (command instanceof OnCommand) {
            state = OnOffType.ON;
        } else if (command instanceof OffCommand) {
            state = OnOffType.OFF;
        }
        if (state != null) {
            updateChannelState(state);
        }
    }
}
