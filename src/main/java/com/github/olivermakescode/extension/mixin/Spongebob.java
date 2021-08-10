package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpongeBlock.class)
public class Spongebob {
    @Inject(at=@At("HEAD"),method="absorbWater(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z",cancellable=true)
    private void cancelAbsorb(World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!extension.alphaSponges.getValue()) return;
        Iterable<BlockPos> iterate = BlockPos.iterate(pos.getX()-2,pos.getY()-2,pos.getZ()-2,pos.getX()+2,pos.getY()+2,pos.getZ()+2);
        for (BlockPos newPos : iterate) {
            FluidState fluidState = world.getFluidState(newPos);
            BlockState blockState = world.getBlockState(newPos);
            Material material = blockState.getMaterial();
            if (fluidState.isIn(FluidTags.WATER)) {
                if (blockState.getBlock() instanceof FluidDrainable drainable)
                    drainable.tryDrainFluid(world, newPos, blockState);
                if (blockState.getBlock() instanceof FluidBlock)
                    world.setBlockState(newPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                if (material == Material.UNDERWATER_PLANT || material == Material.REPLACEABLE_UNDERWATER_PLANT) {
                    BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(newPos) : null;
                    Block.dropStacks(blockState, world, newPos, blockEntity);
                    world.setBlockState(newPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                }
            }
        }
        cir.setReturnValue(false);
    }
}
