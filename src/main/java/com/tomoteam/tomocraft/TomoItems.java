package com.tomoteam.tomocraft;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.tomoteam.tomocraft.TomoCraft.ModId;

public class TomoItems
{
    public static final Item SLICED_BREAD = BuildFoodItem(1, FoodComponents.BREAD.getSaturationModifier());
    public static final Item TOAST = BuildFoodItem(3, 1);
    public static final Item BURNT_TOAST = BuildFoodItem(5, 0);
    public static void RegisterItems()
    {
        addItem(SLICED_BREAD, "sliced_bread");
        addItem(TOAST, "toast");
        addItem(BURNT_TOAST, "burnt_toast");
    }

    public static void addItem(Item item, String name)
    {
        Registry.register(Registries.ITEM, ModId(name), item);
        TomoRegistry.creativeTabItemItems.add(item);
    }
    public static Item BuildFoodItem(int hunger, float saturationModifier)
    {
        return new Item(new Item.Settings().food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturationModifier).build()));
    }
}
