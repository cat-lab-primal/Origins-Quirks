package dev.overgrown.quirks.status_effect.somnambulist.dreaming;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class DreamingStatusEffect extends StatusEffect {

    public DreamingStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x8B5FCF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            if (player.isSprinting()) {
                player.setSprinting(false);
            }
            if (player.isFallFlying()) {
                player.stopFallFlying();
            }
            player.setVelocity(player.getVelocity().multiply(0.3, 1.0, 0.3));
        }
    }
}
