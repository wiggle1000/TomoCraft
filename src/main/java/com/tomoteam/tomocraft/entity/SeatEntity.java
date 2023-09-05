package com.tomoteam.tomocraft.entity;

import com.tomoteam.tomocraft.TomoEntities;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SeatEntity extends Entity
{

    public SeatEntity(EntityType<Entity> type, World level)
    {
        super(type, level);
    }

    public SeatEntity(World level, Direction facing)
    {
        super(TomoEntities.SEAT_ENTITY_TYPE, level);
        noClip = true;
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        discard();
        Vec3d dismountPos = Dismounting.findRespawnPos(TomoEntities.SEAT_ENTITY_TYPE, getWorld(), getBlockPos(), false);
        if(dismountPos != null) return dismountPos;
        return getBlockPos().toCenterPos();
    }

    @Override
    protected void initDataTracker(){}

    @Override
    public void readCustomDataFromNbt(NbtCompound nbtCompound) {}

    @Override
    public void writeCustomDataToNbt(NbtCompound nbtCompound) {}

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket()
    {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater)
    {
        if (!this.hasPassenger(passenger)) {
            return;
        }

        //calculate head yaw difference
        float headYawDifference = MathHelper.wrapDegrees(passenger.getYaw() - getYaw());
        float headSign = Math.signum(MathHelper.wrapDegrees(passenger.getYaw() - getYaw()));
        //System.out.println(headSign + ", " + headYawDifference);
        if(Math.abs(headYawDifference) < 45)
        {
            passenger.setBodyYaw(getYaw());
        }
        else if(Math.abs(headYawDifference) < 80+45)
        {
            passenger.setBodyYaw(getYaw() + headYawDifference - 45 * headSign);
        }
        else
        {
            passenger.setBodyYaw(getYaw() + 80 * headSign + (Math.abs(headYawDifference)-(80+45)) * 0.6f * headSign);
        }

        //-0.3 makes it to where 0 actually looks like sitting on the floor
        double d = this.getY() + passenger.getHeightOffset() - 0.3f;
        positionUpdater.accept(passenger, this.getX(), d, this.getZ());
    }
}
