package dev.overgrown.quirks.status_effect.invisibility.blinded;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BlindedStatusEffect extends StatusEffect {

    public BlindedStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x1D1D2D);
    }
}
