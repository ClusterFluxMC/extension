package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.extension;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BucketItem.class)
public class WaterPlaceMixin {
    @Redirect(
            at=@At(
                    value="INVOKE",
                    target="Lnet/minecraft/fluid/Fluid;isIn(Lnet/minecraft/tag/Tag;)Z"
            ),
            method="placeFluid(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/hit/BlockHitResult;)Z"
    )
    public boolean isInRedirect(Fluid fluid, Tag<Fluid> tag) {
        if (extension.netherWater.getValue()) return false;
        return fluid.isIn(tag);
    }
}
