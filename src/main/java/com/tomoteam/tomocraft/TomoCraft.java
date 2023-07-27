package com.tomoteam.tomocraft;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomoCraft implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("TomoCraft");
    public static final String MOD_ID = "tomocraft";

    public static Identifier ModId(String value) { return new Identifier(MOD_ID, value); }

    @Override
    public void onInitialize()
    {
        LOGGER.info("Beginning mod initialization. Hii!!! :D :D :D");
        TomoRegistry.init();
    }
}
