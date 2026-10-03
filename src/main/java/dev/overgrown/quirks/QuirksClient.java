package dev.overgrown.quirks;

import dev.overgrown.quirks.armor.client.ModEntityModelLayers;
import dev.overgrown.quirks.armor.client.SkinArmorRenderer;
import dev.overgrown.quirks.item.ModItems;
import dev.overgrown.quirks.particle.client.BlueflameParticle;
import dev.overgrown.quirks.particle.client.HellflameParticle;
import dev.overgrown.quirks.particle.client.WarpGateParticle;
import dev.overgrown.quirks.particle.registry.ModParticles;
import dev.overgrown.quirks.status_effect.invisibility.blinded.BlindedEffectHandler;
import dev.overgrown.quirks.status_effect.invisibility.blinded.render.BlindedOverlayRenderer;
import dev.overgrown.quirks.status_effect.somnambulist.dreaming.DreamingEffectHandler;
import dev.overgrown.quirks.status_effect.somnambulist.dreaming.render.DreamingOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class QuirksClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(ModParticles.BLUEFLAME, BlueflameParticle.Factory::new);
        registry.register(ModParticles.HELLFLAME, HellflameParticle.Factory::new);
        registry.register(ModParticles.WARP_GATE, WarpGateParticle.Factory::new);

        ModEntityModelLayers.register();

                        SkinArmorRenderer.register(
                Quirks.identifier("textures/models/armor/gamma_layer_1.png"),
                Quirks.identifier("textures/models/armor/gamma_layer_2.png"),
                Quirks.identifier("textures/models/armor/gamma_layer_1_slim.png"),
                Quirks.identifier("textures/models/armor/gamma_layer_2_slim.png"),
                ModItems.GAMMA_CHESTPLATE,
                ModItems.GAMMA_LEGGINGS,
                ModItems.GAMMA_BOOTS
        );
        
        // Register violet dress armor renderer
        SkinArmorRenderer.register(
                Quirks.identifier("textures/models/armor/violet_dress_layer_1.png"),
                Quirks.identifier("textures/models/armor/violet_dress_layer_2.png"),
                Quirks.identifier("textures/models/armor/violet_dress_layer_1_slim.png"),
                Quirks.identifier("textures/models/armor/violet_dress_layer_2_slim.png"),
                ModItems.VIOLET_DRESS_CHESTPLATE,
                ModItems.VIOLET_DRESS_LEGGINGS,
                ModItems.VIOLET_DRESS_BOOTS
        );

        BlindedEffectHandler.initialize();
        DreamingEffectHandler.initialize();

        HudRenderCallback.EVENT.register(BlindedOverlayRenderer::renderOverlay);
        HudRenderCallback.EVENT.register(DreamingOverlayRenderer::renderOverlay);
    }
}