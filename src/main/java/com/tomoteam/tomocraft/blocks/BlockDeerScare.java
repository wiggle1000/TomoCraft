package com.tomoteam.tomocraft.blocks;

import com.tomoteam.tomocraft.TomoBlocks;
import com.tomoteam.tomocraft.TomoCraft;
import com.tomoteam.tomocraft.TomoRegistry;
import com.tomoteam.tomocraft.TomoSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

public class BlockDeerScare extends Block implements BlockEntityProvider
{
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public BlockDeerScare(Settings settings)
    {
        super(settings.nonOpaque());
        setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        if(world.getBlockEntity(pos) instanceof DeerScareBlockEntity ent)
        {
            if(!world.isClient())
            {
                ent.doServersideUse(world, pos);
                ent.fillAmt = 0;
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
    {
        if(world.random.nextBetween(1, 10)==1)
        {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), TomoSounds.FOUNTAIN, SoundCategory.BLOCKS, 1f, 1, true);
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
    {
        if(!type.equals(TomoBlocks.DEER_SCARE.bet())) return BlockEntityProvider.super.getTicker(world, state, type);
        return (world1, pos, state1, be) -> DeerScareBlockEntity.tick(world1, pos, state1, (DeerScareBlockEntity) be);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DeerScareBlockEntity(pos, state);
    }

    public static class DeerScareBlockEntity extends BlockEntity implements GeoBlockEntity
    {
        private static final float COOLDOWN_SECONDS = 3f;
        private float fillAmt = 0;

        public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public DeerScareBlockEntity(BlockPos pos, BlockState state)
        {
            super(TomoBlocks.DEER_SCARE.bet(), pos, state);
        }

        @Nullable
        @Override
        public Packet<ClientPlayPacketListener> toUpdatePacket() {
            return BlockEntityUpdateS2CPacket.create(this);
        }

        @Override
        public NbtCompound toInitialChunkDataNbt() {
            return createNbt();
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar)
        {
            controllerRegistrar.add(
                    new AnimationController<>(this,  "controller", 1, state -> PlayState.STOP)
                    .triggerableAnim("plonk", RawAnimation.begin().thenPlay("deer_scare.animate")
                    ));

        }
        public static void tick(World world, BlockPos pos, BlockState state, DeerScareBlockEntity be)
        {
            if(world.isClient()) return;
            if(world.isRaining() && world.hasRain(pos))
            {
                be.fillAmt += 0.01f;
            }
            else
            {
                be.fillAmt += 0.002f;
            }
            if(be.fillAmt > 1)
            {
                be.doServersideUse(world, pos);
                be.fillAmt = 0;
            }
        }

        private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState)
        {
            return PlayState.CONTINUE;
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache()
        {
            return cache;
        }

        @Override
        public double getTick(Object blockEntity)
        {
            return RenderUtils.getCurrentTick();
        }

        private long lastUseTimestamp = 0;
        public void doServersideUse(World world, BlockPos pos)
        {
            if (world.getTime()/20f > lastUseTimestamp + COOLDOWN_SECONDS)
            {
                triggerAnim("controller", "plonk");
                world.playSound(null, getPos(), TomoSounds.DEER_SCARE, SoundCategory.BLOCKS, 2f, world.getRandom().nextBetween(900, 1100) / 1000f);
                lastUseTimestamp = world.getTime()/20;
            }
        }
    }
    @Environment(EnvType.CLIENT)
    public static class DeerScareModel extends GeoModel<DeerScareBlockEntity>
    {

        @Override
        public Identifier getModelResource(DeerScareBlockEntity animatable)
        {
            return TomoCraft.ModId("geo/deer_scare.geo.json");
        }

        @Override
        public Identifier getTextureResource(DeerScareBlockEntity animatable)
        {
            return TomoCraft.ModId("textures/block/deer_scare.png");
        }

        @Override
        public Identifier getAnimationResource(DeerScareBlockEntity animatable)
        {
            return TomoCraft.ModId("animations/deer_scare.animation.json");
        }
    }
    @Environment(EnvType.CLIENT)
    public static class DeerScareRenderer extends GeoBlockRenderer<DeerScareBlockEntity>
    {

        public DeerScareRenderer(BlockEntityRendererFactory.Context context)
        {
            super(new DeerScareModel());
        }
    }
}
