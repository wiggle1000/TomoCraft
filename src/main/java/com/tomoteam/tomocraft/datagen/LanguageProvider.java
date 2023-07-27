package com.tomoteam.tomocraft.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class LanguageProvider extends FabricLanguageProvider
{

    protected LanguageProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder)
    {
        builder.add("itemGroup.tomocraft.main", "TomoCraft");
    }
}
