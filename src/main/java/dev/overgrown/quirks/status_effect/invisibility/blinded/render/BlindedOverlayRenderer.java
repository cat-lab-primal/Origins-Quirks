package dev.overgrown.quirks.status_effect.invisibility.blinded.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.overgrown.quirks.Quirks;
import dev.overgrown.quirks.status_effect.registry.ModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BlindedOverlayRenderer {
    private static final Identifier OVERLAY_TEXTURE = Quirks.identifier("textures/overlay/sprites/vision/invisibility/blinded.png");

    // Increased opacity for maximum brightness effect
    private static final float OPACITY = 0.95f;

    public static void renderOverlay(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !client.player.hasStatusEffect(ModStatusEffects.BLINDED)) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        MatrixStack matrices = context.getMatrices();

        matrices.push();

        // Enable additive blending for maximum brightness
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1); // GL_ONE for additive blending

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);

        // Pure white color for maximum brightness
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, OPACITY);

        // Draw the overlay
        context.drawTexture(OVERLAY_TEXTURE, 0, 0, 0, 0, width, height, width, height);

        // Reset render state
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc(); // Reset to default blend mode
        RenderSystem.disableBlend();
        matrices.pop();
    }
}