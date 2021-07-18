package com.github.olivermakescode.extension.mixin;

import com.github.olivermakescode.extension.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AxolotlEntity.class)
public class SoupAxolotlMixin {
    @Shadow @Final
    private static TrackedData<Integer> VARIANT;

    public int soupCount;

    @Inject(at=@At("RETURN"),method= "baseTick()V")
    public void soupInject(CallbackInfo ci) {
        if (!extension.soupAxolotls.getValue()) return;

        AxolotlEntity self = (AxolotlEntity) (Object) this;

        Text name = self.getCustomName();

        if (self.hasCustomName() && (name == Text.of("soup") || name == Text.of("Soup"))) {
            soupCount++;

            DataTracker tracker = self.getDataTracker();
            tracker.set(VARIANT,soupCount/5%5);

            List<ServerPlayerEntity> players = GameruleHelper.server.getWorld(self.getEntityWorld().getRegistryKey()).getPlayers();

            for (ServerPlayerEntity player: players)
                player.networkHandler.sendPacket(new EntityTrackerUpdateS2CPacket(self.getId(), tracker,true));
        }
    }
}
