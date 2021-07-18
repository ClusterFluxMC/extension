package com.github.olivermakescode.extension;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BeetrootsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class RightClickHarvest {
    public static BoolRuleHelper RCH;

    public static void register() {
        RCH = (BoolRuleHelper) GameruleHelper.register("RightClickHarvest",false);
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player.isSneaking() && !RCH.getValue()) return ActionResult.PASS;
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());
            BlockPos pos = hitResult.getBlockPos();
            if (blockState.getBlock() instanceof BeetrootsBlock) {
                int age = blockState.get(Properties.AGE_3);
                if (age == 3) {
                    world.breakBlock(pos,true,player);
                    world.setBlockState(pos,blockState.with(BeetrootsBlock.AGE,0));
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS;
            }
            if (blockState.getBlock() instanceof CropBlock) {
                int age = blockState.get(CropBlock.AGE);
                if (age == 7) {
                    world.breakBlock(pos,true,player);
                    world.setBlockState(pos,blockState.with(CropBlock.AGE,0));
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS;
            }
            if (blockState.getBlock() instanceof NetherWartBlock) {
                int age = blockState.get(NetherWartBlock.AGE);
                if (age == 3) {
                    world.breakBlock(pos,true,player);
                    world.setBlockState(pos,blockState.with(NetherWartBlock.AGE,0));
                    return ActionResult.SUCCESS;
                }
                return ActionResult.PASS;
            }
            return ActionResult.PASS;
        });
    }
}
