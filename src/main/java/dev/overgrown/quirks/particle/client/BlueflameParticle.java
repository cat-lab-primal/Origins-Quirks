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
import net.minecraft.util.math.MathHelper;

/**
 * A flame particle with a configurable {@code size} value. Behavior matches vanilla
 * {@link net.minecraft.client.particle.FlameParticle}: slow, opaque, ignores world collision.
 */
@Environment(EnvType.CLIENT)
public class BlueflameParticle extends AbstractSlowingParticle {

    private BlueflameParticle(ClientWorld world, double x, double y, double z,
                              double vx, double vy, double vz, float size) {
        super(world, x, y, z, vx, vy, vz);
        this.scale *= size;
        this.maxAge = (int) Math.max(this.maxAge * size, 4);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public float getSize(float tickDelta) {
        float age = (this.age + tickDelta) / this.maxAge;
        return this.scale * (1.0F - age * age * 0.5F);
    }

    @Override
    public int getBrightness(float tint) {
        float age = MathHelper.clamp((this.age + tint) / this.maxAge, 0.0F, 1.0F);
        int packed = super.getBrightness(tint);
        int block = packed & 0xFF;
        int sky = packed >> 16 & 0xFF;
        block += (int) (age * 15.0F * 16.0F);
        if (block > 240) {
            block = 240;
        }
        return block | sky << 16;
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
            BlueflameParticle particle = new BlueflameParticle(world, x, y, z, vx, vy, vz, effect.getSize());
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
