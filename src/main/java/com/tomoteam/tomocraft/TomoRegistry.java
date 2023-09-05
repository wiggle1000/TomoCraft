package com.tomoteam.tomocraft;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.ArrayList;

import static com.tomoteam.tomocraft.TomoCraft.ModId;

public class TomoRegistry
{
    //Item group
    public static final RegistryKey<ItemGroup> itemGroup = RegistryKey.of(RegistryKeys.ITEM_GROUP, ModId("main"));

    public static ArrayList<Item> creativeTabBlockItems = new ArrayList<>();

    public static void init() {
        //Create item group
        Registry.register(Registries.ITEM_GROUP, itemGroup,
                FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.ALLIUM))
                .displayName(Text.translatable("itemGroup."+TomoCraft.MOD_ID+".main"))
                .build()
        );

        TomoBlocks.RegisterBlocks();
        TomoItems.RegisterItems();
        TomoSounds.RegisterSounds();
        TomoEntities.RegisterEntities();

        //add items to group, in order of appearance in group
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
            for (Item item : creativeTabBlockItems)
            {
                content.add(item);
            }
        });
    }
}
