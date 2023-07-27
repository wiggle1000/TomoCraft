package com.tomoteam.tomocraft.datagen;

import com.tomoteam.tomocraft.TomoBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

import java.util.HashMap;

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
        builder.add(TomoBlocks.TEST_BLOCK, "Test Block");
        generateColoredBlockNames(TomoBlocks.SMALL_TILES, "Small %s Tiles", builder);
    }

    public String toFriendlyColorName(String inStr)
    {
        if(inStr.contains("_"))
        {
            StringBuilder s = new StringBuilder();
            for (String s1 : inStr.split("_"))
            {
                s.append(toFriendlyColorName(s1)).append(" ");
            }
            return s.deleteCharAt(s.length()-1).toString();
        }
        return inStr.substring(0,1).toUpperCase() + inStr.substring(1).toLowerCase();
    }
    public void generateColoredBlockNames(HashMap<DyeColor, Block> blockSet, String namePattern, TranslationBuilder builder)
    {
        blockSet.forEach((dye, block)->
        {
            builder.add(block, String.format(namePattern, toFriendlyColorName(dye.getName())));
        });
    }

}
