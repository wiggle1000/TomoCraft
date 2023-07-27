package com.tomoteam.tomocraft;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tomoteam.tomocraft.TomoCraft.ModId;

public class TomoBlocks
{
    public static final Block TEST_BLOCK = new Block(FabricBlockSettings.create().strength(4.0f));
    public static HashMap<DyeColor, Block> SMALL_TILES = new HashMap<DyeColor, Block>();

    public static void RegisterBlocks()
    {
        addBlock(TEST_BLOCK, "test_block");
        addDyeableBlock(SMALL_TILES, "small_tile", FabricBlockSettings.create().strength(4.0f));
    }

    public static void addBlock(Block block, String name)
    {
        Registry.register(Registries.BLOCK, ModId(name), block);
        BlockItem bi = Registry.register(Registries.ITEM,  ModId(name), new BlockItem(block, new FabricItemSettings()));
        TomoRegistry.creativeTabBlockItems.add(bi);
    }
    public static void addDyeableBlock(HashMap<DyeColor, Block> blockMap, String name, FabricBlockSettings settings)
    {
        for (DyeColor value : DyeColor.values())
        {
            Block block = new Block(settings);
            blockMap.put(value, Registry.register(Registries.BLOCK, ModId(name+"_"+value.getName()), block));
            BlockItem bi = Registry.register(Registries.ITEM,  ModId(name+"_"+value.getName()), new BlockItem(block, new FabricItemSettings()));
            TomoRegistry.creativeTabBlockItems.add(bi);
        }
    }
}
