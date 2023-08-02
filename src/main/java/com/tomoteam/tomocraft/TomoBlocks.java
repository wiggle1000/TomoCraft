package com.tomoteam.tomocraft;

import com.tomoteam.tomocraft.blocks.BlockDeerScare;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
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
    public static final BERecord<BlockDeerScare.DeerScareBlockEntity> DEER_SCARE = new BERecord(
            new BlockDeerScare(FabricBlockSettings.create().strength(4.0f)),
            BlockDeerScare.DeerScareBlockEntity::new
            );

    public static HashMap<DyeColor, Block> SMALL_TILES = new HashMap<>();

    public static void RegisterBlocks()
    {
        addBlock(TEST_BLOCK, "test_block");
        addDyeableBlock(SMALL_TILES, "small_tile", FabricBlockSettings.create().strength(4.0f));
        addBlockWithBE(DEER_SCARE, "deer_scare", BlockDeerScare.DeerScareBlockEntity::new);
    }

    public static void addBlock(Block block, String name)
    {
        Registry.register(Registries.BLOCK, ModId(name), block);
        BlockItem bi = Registry.register(Registries.ITEM,  ModId(name), new BlockItem(block, new FabricItemSettings()));
        TomoRegistry.creativeTabBlockItems.add(bi);
    }
    public static void addBlockWithBE(BERecord block, String name, FabricBlockEntityTypeBuilder.Factory<BlockEntity> entity)
    {
        Registry.register(Registries.BLOCK, ModId(name), block.block);
        BlockItem bi = Registry.register(Registries.ITEM,  ModId(name), new BlockItem(block.block, new FabricItemSettings()));
        TomoRegistry.creativeTabBlockItems.add(bi);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, ModId(name+"_entity"), block.bet);
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

    public record BERecord<T extends BlockEntity>(Block block, FabricBlockEntityTypeBuilder.Factory<T> be, BlockEntityType<T> bet)
    {
        public BERecord(Block  block, FabricBlockEntityTypeBuilder.Factory<T> be)
        {
            this(block, be, FabricBlockEntityTypeBuilder.create(be, block).build());
        }
    }
}
