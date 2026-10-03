package dev.overgrown.quirks.particle.registry;

import com.mojang.serialization.Codec;
import dev.overgrown.quirks.Quirks;
import dev.overgrown.quirks.particle.SizedLifetimeParticleEffect;
import dev.overgrown.quirks.particle.SizedParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticles {

    public static final ParticleType<SizedParticleEffect> BLUEFLAME = registerSized("blueflame");
    public static final ParticleType<SizedParticleEffect> HELLFLAME = registerSized("hellflame");
    public static final ParticleType<SizedLifetimeParticleEffect> WARP_GATE = registerSizedLifetime("warp_gate");

    public static void registerParticles() {
    }

    private static ParticleType<SizedParticleEffect> registerSized(String path) {
        return Registry.register(Registries.PARTICLE_TYPE, Quirks.identifier(path),
                new ParticleType<>(false, SizedParticleEffect.PARAMETERS_FACTORY) {
                    @Override
                    public Codec<SizedParticleEffect> getCodec() {
                        return SizedParticleEffect.createCodec(this);
                    }
                });
    }

    private static ParticleType<SizedLifetimeParticleEffect> registerSizedLifetime(String path) {
        return Registry.register(Registries.PARTICLE_TYPE, Quirks.identifier(path),
                new ParticleType<>(false, SizedLifetimeParticleEffect.PARAMETERS_FACTORY) {
                    @Override
                    public Codec<SizedLifetimeParticleEffect> getCodec() {
                        return SizedLifetimeParticleEffect.createCodec(this);
                    }
                });
    }
}
