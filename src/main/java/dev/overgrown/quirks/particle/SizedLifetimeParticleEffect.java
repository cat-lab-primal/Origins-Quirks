package dev.overgrown.quirks.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;

import java.util.Locale;

/**
 * A {@link ParticleEffect} that carries both a {@code size} and a {@code lifetime}
 * (in ticks). Used by particles where the caller wants to control how long the
 * particle stays alive — pass {@link Integer#MAX_VALUE} to keep it effectively
 * forever and manage despawning yourself.
 */
public class SizedLifetimeParticleEffect implements ParticleEffect {
    public static final float MIN_SIZE = 0.01F;
    public static final float MAX_SIZE = 16.0F;

    private final ParticleType<SizedLifetimeParticleEffect> type;
    private final float size;
    private final int lifetime;

    public SizedLifetimeParticleEffect(ParticleType<SizedLifetimeParticleEffect> type, float size, int lifetime) {
        this.type = type;
        this.size = MathHelper.clamp(size, MIN_SIZE, MAX_SIZE);
        this.lifetime = Math.max(lifetime, 1);
    }

    public static Codec<SizedLifetimeParticleEffect> createCodec(ParticleType<SizedLifetimeParticleEffect> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("size").forGetter(SizedLifetimeParticleEffect::getSize),
                Codec.INT.fieldOf("lifetime").forGetter(SizedLifetimeParticleEffect::getLifetime)
        ).apply(instance, (size, lifetime) -> new SizedLifetimeParticleEffect(type, size, lifetime)));
    }

    public static final ParticleEffect.Factory<SizedLifetimeParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
        @Override
        public SizedLifetimeParticleEffect read(ParticleType<SizedLifetimeParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            int lifetime = reader.readInt();
            return new SizedLifetimeParticleEffect(type, size, lifetime);
        }

        @Override
        public SizedLifetimeParticleEffect read(ParticleType<SizedLifetimeParticleEffect> type, PacketByteBuf buf) {
            return new SizedLifetimeParticleEffect(type, buf.readFloat(), buf.readVarInt());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.size);
        buf.writeVarInt(this.lifetime);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %d",
                Registries.PARTICLE_TYPE.getId(this.getType()), this.size, this.lifetime);
    }

    public float getSize() {
        return this.size;
    }

    public int getLifetime() {
        return this.lifetime;
    }
}
