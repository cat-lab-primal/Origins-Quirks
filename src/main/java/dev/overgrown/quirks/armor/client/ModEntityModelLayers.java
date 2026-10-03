package dev.overgrown.quirks.armor.client;

import dev.overgrown.quirks.Quirks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

/**
 * Default {@link EntityModelLayer} entries used by {@link SkinArmorRenderer}.
 *
 * <p>Built from {@link PlayerEntityModel#getTexturedModelData} so cuboid sizes and UV
 * regions match a 64x64 player skin (including the second-skin overlay for jacket,
 * sleeves, and pants). The dilation values are kept just above zero so each layer sits
 * a hair outside the previous one (skin → inner armor → outer armor) without z-fighting.
 *
 * <p>Slim and wide layers are registered separately so the renderer can pick whichever
 * matches the wearer's skin model.
 */
@Environment(EnvType.CLIENT)
public final class ModEntityModelLayers {

    public static final EntityModelLayer SKIN_ARMOR_OUTER_WIDE =
            new EntityModelLayer(Quirks.identifier("skin_armor_wide"), "outer");
    public static final EntityModelLayer SKIN_ARMOR_INNER_WIDE =
            new EntityModelLayer(Quirks.identifier("skin_armor_wide"), "inner");
    public static final EntityModelLayer SKIN_ARMOR_OUTER_SLIM =
            new EntityModelLayer(Quirks.identifier("skin_armor_slim"), "outer");
    public static final EntityModelLayer SKIN_ARMOR_INNER_SLIM =
            new EntityModelLayer(Quirks.identifier("skin_armor_slim"), "inner");

    private ModEntityModelLayers() {
    }

    public static void register() {
        registerLayer(SKIN_ARMOR_OUTER_WIDE, new Dilation(0.5F), false);
        registerLayer(SKIN_ARMOR_INNER_WIDE, new Dilation(0.25F), false);
        registerLayer(SKIN_ARMOR_OUTER_SLIM, new Dilation(0.5F), true);
        registerLayer(SKIN_ARMOR_INNER_SLIM, new Dilation(0.25F), true);
    }

    private static void registerLayer(EntityModelLayer layer, Dilation dilation, boolean slim) {
        EntityModelLayerRegistry.registerModelLayer(layer,
                () -> TexturedModelData.of(PlayerEntityModel.getTexturedModelData(dilation, slim), 64, 64));
    }
}
