package com.tomoteam.tomocraft.blocks;

import com.tomoteam.tomocraft.TomoEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class SeatBlock extends RotatableBlock
{
    public double sitHeight;
    public SeatBlock(Settings settings, VoxelShape shape, double sitHeight)
    {
        super(settings, shape);
        this.sitHeight = sitHeight;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        if(world.isClient) return ActionResult.CONSUME;
        if(player.hasVehicle()) return ActionResult.PASS;
        if(!world.canPlayerModifyAt(player, pos)) return ActionResult.PASS;
        if(player.isSneaking()) return ActionResult.PASS;

        Entity seat = TomoEntities.SEAT_ENTITY_TYPE.create(world);
        seat.setPosition(pos.getX() + 0.5f, pos.getY() + sitHeight/16D, pos.getZ() + 0.5f);
        seat.setYaw(state.get(Properties.HORIZONTAL_FACING).asRotation());
        player.setYaw(seat.getYaw());
        world.spawnEntity(seat);
        player.startRiding(seat);
        return ActionResult.CONSUME;
    }
}
