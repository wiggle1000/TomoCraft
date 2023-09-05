package com.tomoteam.tomocraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class RotatableBlock extends HorizontalFacingBlock
{
    protected VoxelShape NORTH_AABB = Block.createCuboidShape(0, 0, 0, 16, 16, 16);
    protected VoxelShape SOUTH_AABB = NORTH_AABB;
    protected VoxelShape WEST_AABB = NORTH_AABB;
    protected VoxelShape EAST_AABB = NORTH_AABB;

    //Rotates a VoxelShape clockwise 90 degrees
    public static VoxelShape RotateVoxelShapeOnYAxis(int times, VoxelShape shape)
    {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};
        for (int i = 0; i < times; i++)
        {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }
    public RotatableBlock(Settings settings, VoxelShape shape)
    {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
        NORTH_AABB = shape;
        EAST_AABB = RotateVoxelShapeOnYAxis(1, NORTH_AABB);
        SOUTH_AABB = RotateVoxelShapeOnYAxis(1, EAST_AABB);;
        WEST_AABB = RotateVoxelShapeOnYAxis(1, SOUTH_AABB);;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        return switch (dir)
        {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case EAST -> EAST_AABB;
            case WEST -> WEST_AABB;
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if(ctx.getPlayer() != null && ctx.getPlayer().isSneaking())
        {
            return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
        }
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
}
