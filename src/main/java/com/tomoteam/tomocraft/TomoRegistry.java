package com.tomoteam.tomocraft;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import static com.tomoteam.tomocraft.TomoCraft.ModId;
import static net.minecraft.registry.Registries.ITEM_GROUP;

public class TomoRegistry
{
    //Item group
    public static final RegistryKey<ItemGroup> itemGroup = RegistryKey.of(RegistryKeys.ITEM_GROUP, ModId("main"));

    public static void init() {
        //Create item group
        Registry.register(Registries.ITEM_GROUP, itemGroup,
                FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.ALLIUM))
                .displayName(Text.translatable("itemGroup."+TomoCraft.MOD_ID+".main"))
                .build()
        );

        //add items to group, in order of appearance in group
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
            content.add(Items.ACACIA_BOAT);
        });
    }
}
