package dev.overgrown.quirks;

import dev.overgrown.quirks.item.ModItems;
import dev.overgrown.quirks.particle.registry.ModParticles;
import dev.overgrown.quirks.sound.ModSounds;
import dev.overgrown.quirks.status_effect.registry.ModStatusEffects;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quirks implements ModInitializer {
	public static final String MOD_ID = "quirks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModSounds.initialize();
		ModParticles.registerParticles();
		ModItems.registerItems();
		ModStatusEffects.initialize();
	}
}