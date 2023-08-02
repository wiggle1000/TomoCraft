package com.tomoteam.tomocraft.client;

import com.tomoteam.tomocraft.TomoBlocks;
import com.tomoteam.tomocraft.blocks.BlockDeerScare;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class ClientInit implements ClientModInitializer
{
    //public static EntityModelLayer DEER_SCARE_LAYER = new EntityModelLayer(TomoCraft.ModId("deer_scare"), "main");
    @Override
    public void onInitializeClient()
    {
        //EntityModelLayerRegistry.registerModelLayer(DEER_SCARE_LAYER);
        BlockEntityRendererFactories.register(TomoBlocks.DEER_SCARE.bet(), BlockDeerScare.DeerScareRenderer::new);
    }
}
