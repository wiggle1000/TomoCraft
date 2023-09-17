package com.tomoteam.tomocraft.blocks;

import com.tomoteam.tomocraft.TomoBlocks;
import com.tomoteam.tomocraft.TomoCraft;
import com.tomoteam.tomocraft.TomoItems;
import com.tomoteam.tomocraft.TomoSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
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
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

public class BlockToaster extends Block implements BlockEntityProvider
{
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public BlockToaster(Settings settings)
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
        if(world.getBlockEntity(pos) instanceof ToasterBlockEntity ent)
        {
            if(!world.isClient())
            {
                ent.doServersideUse(world, pos, player, hand);
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
        if(!type.equals(TomoBlocks.TOASTER.bet())) return BlockEntityProvider.super.getTicker(world, state, type);
        return (world1, pos, state1, be) -> ToasterBlockEntity.tick(world1, pos, state1, (ToasterBlockEntity) be);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ToasterBlockEntity(pos, state);
    }

    public static class ToasterBlockEntity extends BlockEntity implements GeoBlockEntity
    {
        private static final float COOK_TIME = 9.15f * 20; //in ticks
        private int heldItem = 0; //0: nothing, 1: bread, 2: toast, 3:burnt
        public long toastTime = 0;

        public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public ToasterBlockEntity(BlockPos pos, BlockState state)
        {
            super(TomoBlocks.TOASTER.bet(), pos, state);
        }

        public static void tick(World world, BlockPos pos, BlockState state, ToasterBlockEntity be)
        {
            if(be.toastTime > 0)
            {
                be.toastTime -= 1;
                if(be.toastTime <= 0)
                {
                    if(be.heldItem == 1) //cook toast
                    {
                        be.heldItem = 2;
                    }
                    else if(be.heldItem == 2) //burn toast
                    {
                        be.heldItem = 3;
                    }
                }
            }
        }

        @Nullable
        @Override
        public Packet<ClientPlayPacketListener> toUpdatePacket() {
            return BlockEntityUpdateS2CPacket.create(this);
        }

        //TODO: SAVE TOAST STATE TO NBT
        @Override
        public NbtCompound toInitialChunkDataNbt() {
            return createNbt();
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar)
        {
            controllerRegistrar.add(
                    new AnimationController<>(this,  "controller", 1, state -> PlayState.STOP)
                            .triggerableAnim("cook", RawAnimation.begin().thenPlay("animation.toaster.cook"))
                            .triggerableAnim("burn", RawAnimation.begin().thenPlay("animation.toaster.burn"))
                            .triggerableAnim("idle", RawAnimation.begin().thenPlay("animation.toaster.idle"))
                    );

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
        public void doServersideUse(World world, BlockPos pos, PlayerEntity player, Hand hand)
        {
            if(toastTime <= 0 && heldItem == 0 && player.getStackInHand(hand).getItem() == TomoItems.SLICED_BREAD) //TODO: Bread Tag?
            {
                heldItem = 1;  //bread
                player.getStackInHand(hand).decrement(1);
                triggerAnim("controller", "cook");
                toastTime = (long) COOK_TIME;

                //world.playSound(null, getPos(), TomoSounds.DEER_SCARE, SoundCategory.BLOCKS, 2f, world.getRandom().nextBetween(900, 1100) / 1000f);
                lastUseTimestamp = world.getTime();
            }
            else if(toastTime <= 0 && heldItem == 0 && player.getStackInHand(hand).getItem() == TomoItems.TOAST)
            {
                heldItem = 2;  //toast
                player.getStackInHand(hand).decrement(1);
                triggerAnim("controller", "burn");
                toastTime = (long) COOK_TIME;

                //world.playSound(null, getPos(), TomoSounds.DEER_SCARE, SoundCategory.BLOCKS, 2f, world.getRandom().nextBetween(900, 1100) / 1000f);
                lastUseTimestamp = world.getTime();
            }
            else if(heldItem == 2 && toastTime <= 0) //toast
            {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TomoItems.TOAST, 1)));
                heldItem = 0;
                triggerAnim("controller", "idle");
            }
            else if(heldItem == 3 && toastTime <= 0) //burnt
            {
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TomoItems.BURNT_TOAST, 1)));
                heldItem = 0;
                triggerAnim("controller", "idle");
            }
        }
    }
    @Environment(EnvType.CLIENT)
    public static class ToasterModel extends GeoModel<ToasterBlockEntity>
    {

        @Override
        public Identifier getModelResource(ToasterBlockEntity animatable)
        {
            return TomoCraft.ModId("geo/toaster.geo.json");
        }

        @Override
        public Identifier getTextureResource(ToasterBlockEntity animatable)
        {
            return TomoCraft.ModId("textures/block/toaster.png");
        }

        @Override
        public Identifier getAnimationResource(ToasterBlockEntity animatable)
        {
            return TomoCraft.ModId("animations/toaster.animation.json");
        }
    }
    @Environment(EnvType.CLIENT)
    public static class ToasterRenderer extends GeoBlockRenderer<ToasterBlockEntity>
    {

        public ToasterRenderer(BlockEntityRendererFactory.Context context)
        {
            super(new ToasterModel());
        }
    }
}
