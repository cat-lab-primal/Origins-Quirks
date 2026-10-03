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
 * A {@link ParticleEffect} that carries a single {@code size} value, similar to how
 * {@link net.minecraft.particle.AbstractDustParticleEffect} carries its {@code scale}.
 *
 * <p>One effect class is reused for any sized particle — the actual {@link ParticleType}
 * is stored on the instance so the same effect can back multiple registered types
 * (blueflame, hellflame, warp gate, ...).
 */
public class SizedParticleEffect implements ParticleEffect {
    public static final float MIN_SIZE = 0.01F;
    public static final float MAX_SIZE = 16.0F;

    private final ParticleType<SizedParticleEffect> type;
    private final float size;

    public SizedParticleEffect(ParticleType<SizedParticleEffect> type, float size) {
        this.type = type;
        this.size = MathHelper.clamp(size, MIN_SIZE, MAX_SIZE);
    }

    public static Codec<SizedParticleEffect> createCodec(ParticleType<SizedParticleEffect> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("size").forGetter(SizedParticleEffect::getSize)
        ).apply(instance, size -> new SizedParticleEffect(type, size)));
    }

    public static final ParticleEffect.Factory<SizedParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
        @Override
        public SizedParticleEffect read(ParticleType<SizedParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new SizedParticleEffect(type, reader.readFloat());
        }

        @Override
        public SizedParticleEffect read(ParticleType<SizedParticleEffect> type, PacketByteBuf buf) {
            return new SizedParticleEffect(type, buf.readFloat());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.size);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f",
                Registries.PARTICLE_TYPE.getId(this.getType()), this.size);
    }

    public float getSize() {
        return this.size;
    }
}
