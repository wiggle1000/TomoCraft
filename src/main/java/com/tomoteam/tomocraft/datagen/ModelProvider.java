package com.tomoteam.tomocraft.datagen;

import com.tomoteam.tomocraft.TomoBlocks;
import com.tomoteam.tomocraft.TomoCraft;
import com.tomoteam.tomocraft.TomoItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.HashMap;

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
        gen.registerSimpleCubeAll(TomoBlocks.TEST_BLOCK);
        genDyedModelsSimpleCubeAll(TomoBlocks.SMALL_TILES, gen);
    }

    public void genDyedModelsSimpleCubeAll(HashMap<DyeColor, Block> blockMap, BlockStateModelGenerator gen)
    {
        for (Block value : blockMap.values())
        {
            gen.registerSimpleCubeAll(value);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen)
    {
        //gen.register(TomoItems., GENERATED);
    }
}
