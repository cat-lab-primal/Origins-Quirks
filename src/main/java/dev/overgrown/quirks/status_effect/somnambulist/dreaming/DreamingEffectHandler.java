package dev.overgrown.quirks.status_effect.somnambulist.dreaming;

import dev.overgrown.quirks.sound.ModSounds;
import dev.overgrown.quirks.status_effect.registry.ModStatusEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class DreamingEffectHandler {
    private static boolean wasDreaming = false;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                wasDreaming = false;
                return;
            }
            boolean isDreaming = client.player.hasStatusEffect(ModStatusEffects.DREAMING);
            if (isDreaming && !wasDreaming) {
                playDreamingSound(client);
            }
            wasDreaming = isDreaming;
        });
    }

    private static void playDreamingSound(MinecraftClient client) {
        if (client.player != null && ModSounds.SOMNAMBULIST_DREAMING != null) {
            client.player.playSound(ModSounds.SOMNAMBULIST_DREAMING, 1.0F, 1.0F);
        }
    }
}
