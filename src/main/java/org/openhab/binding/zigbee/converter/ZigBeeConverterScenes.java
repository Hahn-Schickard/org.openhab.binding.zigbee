package org.openhab.binding.zigbee.converter;

import java.math.BigDecimal;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.zigbee.ZigBeeBindingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zsmartsystems.zigbee.CommandListener;
import com.zsmartsystems.zigbee.ZigBeeDevice;
import com.zsmartsystems.zigbee.zcl.ZclAttribute;
import com.zsmartsystems.zigbee.zcl.ZclAttributeListener;
import com.zsmartsystems.zigbee.zcl.ZclCommand;
import com.zsmartsystems.zigbee.zcl.clusters.ZclBasicCluster;
import com.zsmartsystems.zigbee.zcl.clusters.ZclScenesCluster;
import com.zsmartsystems.zigbee.zcl.protocol.ZclCommandType;

public class ZigBeeConverterScenes extends ZigBeeChannelConverter implements ZclAttributeListener, CommandListener {
    private static Logger logger = LoggerFactory.getLogger(ZigBeeConverterScenes.class);

    private ZclScenesCluster clusterScenes;
    private ZclBasicCluster basicCluster;

    private String channelLabel;
    private Integer currentScene;
    private Integer groupID = new Integer(0x00);
    private boolean initialised = false;

    @Override
    public void initializeConverter() {
        if (initialised == true) {
            return;
        }
        clusterScenes = (ZclScenesCluster) device.getCluster(ZclScenesCluster.CLUSTER_ID);

        if (clusterScenes == null) {
            logger.error("{}: Error opening device scene sensor controls", device.getIeeeAddress());
            return;
        }

        clusterScenes.addAttributeListener(this);
        clusterScenes.addCommandListener(this);

        currentScene = clusterScenes.getCurrentScene(0);
        // TODO: no scene response yet, skip
        if (currentScene != null) {
            initialised = true;
        }
        // debug helper command
        initialised = true;
    }

    @Override
    public void disposeConverter() {
        if (initialised == false) {
            return;
        }

        logger.debug("{}: Closing device occupancy sensor cluster", device.getIeeeAddress());

        if (clusterScenes != null) {
            clusterScenes.removeAttributeListener(this);
        }
    }

    @Override
    public void handleRefresh() {
        if (initialised == false) {
            return;
        }

        Integer value = clusterScenes.getCurrentScene(0);
        if (value != null) {
            updateChannelState(new DecimalType(BigDecimal.valueOf(value, 0)));
        }
    }

    @Override
    public Runnable handleCommand(final Command command) {
        return new Runnable() {
            @Override
            public void run() {
                if (initialised == false) {
                    return;
                }

                if (command instanceof DecimalType) {
                    clusterScenes.recallSceneCommand(groupID, new Integer(((DecimalType) command).intValue()));
                } else {
                    logger.error("{}: Unhandeled command in {}", device.getIeeeAddress(),
                            ZigBeeConverterScenes.class.getName());
                }
            }
        };
    }

    @Override
    public Channel getChannel(ThingUID thingUID, ZigBeeDevice device) {
        if (device.getCluster(ZclScenesCluster.CLUSTER_ID) == null) {
            return null;
        }
        basicCluster = (ZclBasicCluster) device.getCluster(ZclBasicCluster.CLUSTER_ID);
        if (basicCluster == null) {
            logger.error("{}: Error opening device baisic controls", device.getIeeeAddress());
        }

        // Get cluster location descriptor for channel label
        if (basicCluster != null) {
            channelLabel = basicCluster.getLocationDescription(0);
        } else {
            channelLabel = "Scenes";
        }

        return createChannel(device, thingUID, ZigBeeBindingConstants.CHANNEL_SCENE,
                ZigBeeBindingConstants.ITEM_TYPE_NUMBER, channelLabel);
    }

    @Override
    public void attributeUpdated(ZclAttribute attribute) {
        logger.debug("{}: ZigBee attribute reports {}", device.getIeeeAddress(), attribute);
        if (attribute.getId() == ZclScenesCluster.ATTR_CURRENTSCENE) {
            Integer value = (Integer) attribute.getLastValue();
            if (value != null && Integer.lowestOneBit(value) == 1) {
                updateChannelState(new DecimalType(BigDecimal.valueOf(value, 0)));
            }
        }
    }

    @Override
    public void commandReceived(final com.zsmartsystems.zigbee.Command command) {

        ZclCommand zclCommand = (ZclCommand) command;

        if (zclCommand == null) {
            logger.debug("No command received");
            return;
        }

        if (zclCommand.getCommandId() == Integer.valueOf(ZclCommandType.ADD_SCENE_RESPONSE.getId())) {
            // TODO: implement add scene response
        }
    }
}