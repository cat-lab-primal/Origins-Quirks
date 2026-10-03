package dev.overgrown.quirks.status_effect.invisibility.blinded;

import dev.overgrown.quirks.sound.ModSounds;
import dev.overgrown.quirks.status_effect.registry.ModStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class BlindedEffectHandler {
    private static boolean wasBlinded = false;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                wasBlinded = false;
                return;
            }
            boolean isBlinded = client.player.hasStatusEffect(ModStatusEffects.BLINDED);
            if (isBlinded && !wasBlinded) {
                playBlindedSound(client);
            }
            wasBlinded = isBlinded;
        });
    }

    private static void playBlindedSound(MinecraftClient client) {
        if (client.player != null && ModSounds.INVISIBILITY_BLINDED != null) {
            client.player.playSound(ModSounds.INVISIBILITY_BLINDED, 1.0F, 1.0F);
        }
    }
}
