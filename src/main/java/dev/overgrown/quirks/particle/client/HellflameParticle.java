package dev.overgrown.quirks.particle.client;

import dev.overgrown.quirks.particle.SizedParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.AbstractSlowingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

/**
 * A soul-fire-flame style particle with a configurable {@code size} value. Behavior matches
 * vanilla {@link net.minecraft.client.particle.SoulParticle}: animates through atlas frames
 * over the particle's lifetime, renders translucent.
 */
@Environment(EnvType.CLIENT)
public class HellflameParticle extends AbstractSlowingParticle {
    private final SpriteProvider spriteProvider;

    private HellflameParticle(ClientWorld world, double x, double y, double z,
                              double vx, double vy, double vz, float size, SpriteProvider spriteProvider) {
        super(world, x, y, z, vx, vy, vz);
        this.spriteProvider = spriteProvider;
        this.scale(1.5F * size);
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SizedParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(FabricSpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SizedParticleEffect effect, ClientWorld world,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            HellflameParticle particle = new HellflameParticle(world, x, y, z, vx, vy, vz,
                    effect.getSize(), this.spriteProvider);
            particle.setAlpha(1.0F);
            return particle;
        }
    }
}
