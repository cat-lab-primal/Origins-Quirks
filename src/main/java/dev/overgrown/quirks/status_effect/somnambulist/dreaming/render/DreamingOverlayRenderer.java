package dev.overgrown.quirks.status_effect.somnambulist.dreaming.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.overgrown.quirks.Quirks;
import dev.overgrown.quirks.status_effect.registry.ModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class DreamingOverlayRenderer {
    private static final Identifier STATUS_TEXTURE = Quirks.identifier("textures/overlay/sprites/vision/somnambulist/status.png");
    private static final Identifier VIGNETTE_TEXTURE = Quirks.identifier("textures/overlay/sprites/vision/somnambulist/vignette.png");

    // Vignette settings
    private static final float VIGNETTE_RED = 1.0f;
    private static final float VIGNETTE_GREEN = 0.9f;
    private static final float VIGNETTE_BLUE = 0.95f;
    private static final float VIGNETTE_BASE_STRENGTH = 0.25f;

    // Status overlay opacity
    private static final float STATUS_OPACITY = 1.0f;

    public static void renderOverlay(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !client.player.hasStatusEffect(ModStatusEffects.DREAMING)) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        MatrixStack matrices = context.getMatrices();

        matrices.push();

        // Adds subtle pulsing effect based on game time
        float pulse = (float) (Math.sin(client.player.age * 0.2f) * 0.05f + 0.95f);
        float vignetteStrength = VIGNETTE_BASE_STRENGTH * pulse;

        // Render vignette first
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);

        // Makes vignette brighter with more transparent color for the pulsing effect
        RenderSystem.setShaderColor(VIGNETTE_RED, VIGNETTE_GREEN, VIGNETTE_BLUE, vignetteStrength);

        // Draw vignette overlay
        context.drawTexture(VIGNETTE_TEXTURE, 0, 0, 0, 0, width, height, width, height);

        // Render status overlay on top
        RenderSystem.setShaderTexture(0, STATUS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, STATUS_OPACITY);

        // Draw status overlay
        context.drawTexture(STATUS_TEXTURE, 0, 0, 0, 0, width, height, width, height);

        // Reset render state
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        matrices.pop();
    }
}