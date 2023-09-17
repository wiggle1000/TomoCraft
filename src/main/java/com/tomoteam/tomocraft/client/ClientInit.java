package com.tomoteam.tomocraft.client;

import com.tomoteam.tomocraft.TomoBlocks;
import com.tomoteam.tomocraft.TomoEntities;
import com.tomoteam.tomocraft.blocks.BlockDeerScare;
import com.tomoteam.tomocraft.blocks.BlockToaster;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EmptyEntityRenderer;

@Environment(EnvType.CLIENT)
public class ClientInit implements ClientModInitializer
{
    //public static EntityModelLayer DEER_SCARE_LAYER = new EntityModelLayer(TomoCraft.ModId("deer_scare"), "main");
    @Override
    public void onInitializeClient()
    {
        //EntityModelLayerRegistry.registerModelLayer(DEER_SCARE_LAYER);
        BlockEntityRendererFactories.register(TomoBlocks.DEER_SCARE.bet(), BlockDeerScare.DeerScareRenderer::new);
        BlockEntityRendererFactories.register(TomoBlocks.TOASTER.bet(), BlockToaster.ToasterRenderer::new);

        EntityRendererRegistry.register(TomoEntities.SEAT_ENTITY_TYPE, EmptyEntityRenderer::new);
    }
}
