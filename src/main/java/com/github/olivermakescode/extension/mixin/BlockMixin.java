package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    public static void dropStacks(BlockState state, World world, BlockPos pos, @Nullable BlockEntity blockEntity, Entity entity, ItemStack stack) {
    }

    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/block/Block;dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V"),method="afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V")
    private void stopBlockDrop(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack) {
        if (state.getBlock() instanceof TntBlock && extension.alphaTnt.getValue()) return;
        dropStacks(state,world,pos,blockEntity,entity,stack);
    }

    @Inject(at=@At("HEAD"),method="onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
    public void updateSponge(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!extension.alphaSponges.getValue() || !(state.getBlock() instanceof SpongeBlock)) return;
        Iterable<BlockPos> iterate = BlockPos.iterate(pos.getX()-3,pos.getY()-3,pos.getZ()-3,pos.getX()+3,pos.getY()+3,pos.getZ()+3);
        for (BlockPos newPos : iterate)
            world.updateNeighbors(newPos,world.getBlockState(newPos).getBlock());
    }
}
