package com.tomoteam.tomocraft.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tomoteam.tomocraft.TomoBlocks;
import com.tomoteam.tomocraft.TomoCraft;
import com.tomoteam.tomocraft.TomoItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

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
        registerHorizFacingModel(TomoBlocks.DEER_SCARE.block(), TomoCraft.ModId("block/deer_scare"), gen);
        registerHorizFacingModel(TomoBlocks.TOASTER.block(), TomoCraft.ModId("block/toaster"), gen);
        //gen.registerSimpleState(TomoBlocks.DEER_SCARE.block());
        genDyedModelsSimpleCubeAll(TomoBlocks.SMALL_TILES, gen);
        //gen.modelCollector.accept(TomoCraft.ModId("block/deer_scare_moving"), );
        registerHorizFacingDyedModel(TomoBlocks.BEAN_BAG, TomoCraft.ModId("block/bean_bag"), gen);
    }


    @Override
    public void generateItemModels(ItemModelGenerator gen)
    {
        gen.register(TomoItems.SLICED_BREAD, GENERATED);
        gen.register(TomoItems.TOAST, GENERATED);
        gen.register(TomoItems.BURNT_TOAST, GENERATED);
    }

    public static final BlockStateVariantMap HORIZONTAL_FACING_ROTATION_VARIANT_MAP = BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
            .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R0))
            .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R180))
            .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R270));
    public void registerHorizFacingModel(Block block, Identifier modelId, BlockStateModelGenerator gen)
    {
        gen.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId))
                .coordinate(HORIZONTAL_FACING_ROTATION_VARIANT_MAP)
        );
        gen.registerParentedItemModel(block, modelId);
    }
    public void registerHorizFacingDyedModel(HashMap<DyeColor, Block> blockMap, Identifier modelIdBase, BlockStateModelGenerator gen)
    {
        for (DyeColor key: blockMap.keySet())
        {
            Block value = blockMap.get(key);
            registerHorizFacingModel(value, modelIdBase.withSuffixedPath("_"+key.toString()), gen);
        }
        genDyedModelsFromParent(blockMap, modelIdBase, gen);
    }
    public void genDyedModelsSimpleCubeAll(HashMap<DyeColor, Block> blockMap, BlockStateModelGenerator gen)
    {
        for (Block value : blockMap.values())
        {
            gen.registerSimpleCubeAll(value);
        }
    }
    public void genDyedModelsFromParent(HashMap<DyeColor, Block> blockMap, Identifier modelIdBase, BlockStateModelGenerator gen)
    {
        for (DyeColor key: blockMap.keySet())
        {
            Block value = blockMap.get(key);
            gen.modelCollector.accept(modelIdBase.withSuffixedPath("_"+key.toString()),
                    //Use this as a shell class to just generate a whole json basically
                    new SimpleModelSupplier(modelIdBase)
                    {
                        @Override
                        public JsonElement get()
                        {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("parent", modelIdBase.toString());
                            JsonObject textures = new JsonObject();
                            textures.addProperty("0", modelIdBase.withSuffixedPath("_"+key.toString()).toString());
                            textures.addProperty("particle", modelIdBase.withSuffixedPath("_"+key.toString()).toString());
                            jsonObject.add("textures", textures);
                            return jsonObject;
                        }
                    });

        }
    }
}
