package dev.overgrown.quirks.mixin.somnambulist;

import dev.overgrown.quirks.status_effect.registry.ModStatusEffects;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class DreamingPlayerEntityMixin {

    @Inject(method = "updatePose", at = @At("HEAD"), cancellable = true)
    private void quirks$forceDreamingPose(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self.hasStatusEffect(ModStatusEffects.DREAMING)) {
            self.setPose(EntityPose.SWIMMING);
            ci.cancel();
        }
    }
}
