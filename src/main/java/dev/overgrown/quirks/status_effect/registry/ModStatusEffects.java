package dev.overgrown.quirks.status_effect.registry;

import dev.overgrown.quirks.Quirks;
import dev.overgrown.quirks.status_effect.invisibility.blinded.BlindedStatusEffect;
import dev.overgrown.quirks.status_effect.somnambulist.dreaming.DreamingStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {
    public static final StatusEffect BLINDED = register("blinded", new BlindedStatusEffect());
    public static final StatusEffect DREAMING = register("dreaming", new DreamingStatusEffect());

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Quirks.MOD_ID, name), effect);
    }

    public static void initialize() {
        Quirks.LOGGER.info("Registering status effects for {}", Quirks.MOD_ID);
    }
}
