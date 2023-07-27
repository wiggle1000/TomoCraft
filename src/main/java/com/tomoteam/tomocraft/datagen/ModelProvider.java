package com.tomoteam.tomocraft.datagen;

import com.tomoteam.tomocraft.TomoItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

import static net.minecraft.data.client.Models.GENERATED;

public class ModelProvider extends FabricModelProvider
{
    public ModelProvider(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen)
    {

    }

    @Override
    public void generateItemModels(ItemModelGenerator gen)
    {
        //gen.register(TomoItems., GENERATED);
    }
}
