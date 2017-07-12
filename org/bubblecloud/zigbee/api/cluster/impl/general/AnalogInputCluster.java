/*
   Copyright 2008-2013 CNR-ISTI, http://isti.cnr.it
   Institute of Information Science and Technologies
   of the Italian National Research Council


   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.bubblecloud.zigbee.api.cluster.impl.general;

import org.bubblecloud.zigbee.api.cluster.impl.api.core.Attribute;
import org.bubblecloud.zigbee.api.cluster.impl.api.general.AnalogInput;
import org.bubblecloud.zigbee.api.cluster.impl.attribute.Attributes;
import org.bubblecloud.zigbee.api.cluster.impl.core.AttributeImpl;
import org.bubblecloud.zigbee.api.cluster.impl.core.ZCLClusterBase;
import org.bubblecloud.zigbee.network.ZigBeeEndpoint;

public class AnalogInputCluster extends ZCLClusterBase implements AnalogInput {

    private final AttributeImpl description;
    private final AttributeImpl maxPresentValue;
    private final AttributeImpl minPresentValue;
    private final AttributeImpl outOfService;
    private final AttributeImpl presentValue;
    private final AttributeImpl reliability;
    private final AttributeImpl resolution;
    private final AttributeImpl statusFlags;
    private final AttributeImpl engineeringUnits;
    private final AttributeImpl applicationType;

    // Quellcode Nathi

    private final AttributeImpl valueX;
    private final AttributeImpl valueY;
    private final AttributeImpl valueZ;

    // Ende Nathi

    private final Attribute[] attributes;

    public AnalogInputCluster(ZigBeeEndpoint zbDevice) {
        super(zbDevice);

        description = new AttributeImpl(zbDevice, this, Attributes.DESCRIPTION);
        maxPresentValue = new AttributeImpl(zbDevice, this, Attributes.MAX_PRESENT_VALUE);
        minPresentValue = new AttributeImpl(zbDevice, this, Attributes.MIN_PRESENT_VALUE);
        outOfService = new AttributeImpl(zbDevice, this, Attributes.OUT_OF_SERVICE);
        presentValue = new AttributeImpl(zbDevice, this, Attributes.PRESENT_VALUE);
        reliability = new AttributeImpl(zbDevice, this, Attributes.RELIABILITY);
        resolution = new AttributeImpl(zbDevice, this, Attributes.RESOLUTION);
        statusFlags = new AttributeImpl(zbDevice, this, Attributes.STATUS_FLAGS);
        engineeringUnits = new AttributeImpl(zbDevice, this, Attributes.ENGINEERING_UNITS);
        applicationType = new AttributeImpl(zbDevice, this, Attributes.APPLICATION_TYPE);

        // Quellcode Nathi

        valueX = new AttributeImpl(zbDevice, this, Attributes.MEASURED_VALUE_X);
        valueY = new AttributeImpl(zbDevice, this, Attributes.MEASURED_VALUE_Y);
        valueZ = new AttributeImpl(zbDevice, this, Attributes.MEASURED_VALUE_Z);

        // Ende Nathi

        attributes = new AttributeImpl[] { description, maxPresentValue, minPresentValue, outOfService, presentValue,
                reliability, resolution, statusFlags, engineeringUnits, applicationType, valueX, valueY, valueZ };
    }

    @Override
    public short getId() {
        return AnalogInput.ID;
    }

    @Override
    public String getName() {
        return AnalogInput.NAME;
    }

    @Override
    public Attribute[] getStandardAttributes() {
        return attributes;
    }

    @Override
    public Attribute getAttributeDescription() {
        return description;
    }

    @Override
    public Attribute getAttributeMaxPresentValue() {
        return maxPresentValue;
    }

    @Override
    public Attribute getAttributeMinPresentValue() {
        return minPresentValue;
    }

    @Override
    public Attribute getAttributeOutOfService() {
        return outOfService;
    }

    @Override
    public Attribute getAttributePresentValue() {
        return presentValue;
    }

    @Override
    public Attribute getAttributeReliability() {
        return reliability;
    }

    @Override
    public Attribute getAttributeResolution() {
        return resolution;
    }

    @Override
    public Attribute getAttributeStatusFlags() {
        return statusFlags;
    }

    @Override
    public Attribute getAttributeEngineeringUnits() {
        return engineeringUnits;
    }

    @Override
    public Attribute getAttributeApplicationType() {
        return applicationType;
    }

    @Override
    public Attribute getAttributeValueX() {
        return valueX;
    }

    @Override
    public Attribute getAttributeValueY() {
        return valueY;
    }

    @Override
    public Attribute getAttributeValueZ() {
        return valueZ;
    }

}
