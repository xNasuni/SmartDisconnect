package dev.xnasuni.smartdisconnect;

import dev.xnasuni.smartdisconnect.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartDisconnect implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("smart-disconnect");

    @Override
    public void onInitialize() {
        ModConfig.init();
        LOGGER.info("Smart Disconnect Initialized");
    }
}
