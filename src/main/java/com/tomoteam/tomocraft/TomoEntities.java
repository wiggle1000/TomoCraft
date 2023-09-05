package com.tomoteam.tomocraft;

import com.tomoteam.tomocraft.entity.SeatEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class TomoEntities
{
    public static final EntityType<Entity> SEAT_ENTITY_TYPE  = Registry.register(
            Registries.ENTITY_TYPE,
            TomoCraft.ModId("seat_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SeatEntity::new).dimensions(EntityDimensions.fixed(0.00001F, 0.00001F)).build()
    );

    public static void RegisterEntities()
    {

    }
}
