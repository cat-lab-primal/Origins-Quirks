package dev.overgrown.quirks.particle.client;

import dev.overgrown.quirks.particle.SizedLifetimeParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

/**
 * A camera-facing, fully stationary particle. Holds its spawn position, never collides,
 * and never moves. Size and lifetime (in ticks) come from the
 * {@link SizedLifetimeParticleEffect}. Pass {@link Integer#MAX_VALUE} as the lifetime
 * to keep it alive effectively forever.
 */
@Environment(EnvType.CLIENT)
public class WarpGateParticle extends SpriteBillboardParticle {

    private WarpGateParticle(ClientWorld world, double x, double y, double z, float size, int lifetime, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.velocityX = 0.0D;
        this.velocityY = 0.0D;
        this.velocityZ = 0.0D;
        this.gravityStrength = 0.0F;
        this.collidesWithWorld = false;
        this.scale = size;
        this.maxAge = lifetime;
        this.setSprite(spriteProvider.getSprite(this.random));
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        }
    }

    @Override
    public void move(double dx, double dy, double dz) {
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SizedLifetimeParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(FabricSpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SizedLifetimeParticleEffect effect, ClientWorld world,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            WarpGateParticle particle = new WarpGateParticle(world, x, y, z,
                    effect.getSize(), effect.getLifetime(), this.spriteProvider);
            particle.setAlpha(1.0F);
            return particle;
        }
    }
}
