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

package org.bubblecloud.zigbee.api.cluster.impl.api.measurement_sensing;

import org.bubblecloud.zigbee.api.cluster.impl.api.core.Attribute;
import org.bubblecloud.zigbee.api.cluster.impl.api.core.ZCLCluster;

/**
 * This class represent the <b>Pressure Metering</b> Cluster as defined by the document:
 * <i>ZigBee Cluster Library</i> public release version 075123r01ZB
 *
 * @author Nathalie Hipp, Hahn-Schickard
 * @author <a href="mailto:giancarlo.riolo@isti.cnr.it">Giancarlo Riolo</a>
 * @version $LastChangedRevision: 799 $ ($LastChangedDate: 2013-08-06 18:00:05 +0200 (mar, 06 ago 2013) $)
 * @since 0.8.0
 */

public interface PressureMeasurement extends ZCLCluster {

    static final short ID = 0x0403;
    static final String NAME = "Pressure Measurement";
    static final String DESCRIPTION = "Attributes and commands for configuring the measurement"
            + " of pressure, and reporting pressure measurements";

    /*
     * Auskommentiert Nathi
     * public Attribute getAttributeDescription();
     */

    // Quellcode Nathi
    public Attribute getAttributeMeasuredValue();

    public Attribute getAttributeMinMeasuredValue();

    public Attribute getAttributeMaxMeasuredValue();

    public Attribute getAttributeTolerance();
    // Ende Quelcode Nathi

}
