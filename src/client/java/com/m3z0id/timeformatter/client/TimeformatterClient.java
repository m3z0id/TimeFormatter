package com.m3z0id.timeformatter.client;

import com.m3z0id.timeformatter.client.configuration.Configuration;
import com.m3z0id.timeformatter.client.listeners.ChatListener;
import net.fabricmc.api.ClientModInitializer;

import java.util.logging.Logger;

public class TimeformatterClient implements ClientModInitializer {
    public static final String MOD_ID = "timeformatter";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);
    @Override
    public void onInitializeClient() {
        ChatListener.register();
    }
}
