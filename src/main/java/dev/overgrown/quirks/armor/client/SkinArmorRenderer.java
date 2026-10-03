package dev.overgrown.quirks.armor.client;

import dev.overgrown.quirks.mixin.client.PlayerEntityModelAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Skin-style armor renderer using a player-skin layout (64x64) with second-skin overlays.
 *
 * <p>Two textures per skin model — layer_1 (helmet, chestplate, boots) and layer_2 (leggings) —
 * mirroring the vanilla armor convention. Slim and wide variants are supplied separately.
 */
@Environment(EnvType.CLIENT)
public class SkinArmorRenderer implements ArmorRenderer {

    private static final Map<EntityModelLayer, PlayerEntityModel<LivingEntity>> MODEL_CACHE = new HashMap<>();
    private static final Map<Item, SkinArmorRenderer> BY_ITEM = new HashMap<>();

    private final EntityModelLayer outerWide;
    private final EntityModelLayer innerWide;
    private final EntityModelLayer outerSlim;
    private final EntityModelLayer innerSlim;
    private final Identifier wideLayer1;
    private final Identifier wideLayer2;
    private final Identifier slimLayer1;
    private final Identifier slimLayer2;

    public SkinArmorRenderer(EntityModelLayer outerWide, EntityModelLayer innerWide,
                             EntityModelLayer outerSlim, EntityModelLayer innerSlim,
                             Identifier wideLayer1, Identifier wideLayer2,
                             Identifier slimLayer1, Identifier slimLayer2) {
        this.outerWide = outerWide;
        this.innerWide = innerWide;
        this.outerSlim = outerSlim;
        this.innerSlim = innerSlim;
        this.wideLayer1 = wideLayer1;
        this.wideLayer2 = wideLayer2;
        this.slimLayer1 = slimLayer1;
        this.slimLayer2 = slimLayer2;
    }

    /**
     * Registers a skin-style armor renderer with separate layer_1 / layer_2 textures for both
     * the wide and slim player models.
     *
     * @param wideLayer1 outer layer for HEAD / CHEST / FEET on wide-arm players (helmet,
     *                   chestplate body+arms, boot region of legs)
     * @param wideLayer2 inner layer for LEGS on wide-arm players (leggings)
     * @param slimLayer1 outer layer for HEAD / CHEST / FEET on slim-arm players
     * @param slimLayer2 inner layer for LEGS on slim-arm players
     */
    public static void register(Identifier wideLayer1, Identifier wideLayer2,
                                Identifier slimLayer1, Identifier slimLayer2,
                                ItemConvertible... items) {
        SkinArmorRenderer renderer = new SkinArmorRenderer(
                ModEntityModelLayers.SKIN_ARMOR_OUTER_WIDE, ModEntityModelLayers.SKIN_ARMOR_INNER_WIDE,
                ModEntityModelLayers.SKIN_ARMOR_OUTER_SLIM, ModEntityModelLayers.SKIN_ARMOR_INNER_SLIM,
                wideLayer1, wideLayer2, slimLayer1, slimLayer2);
        ArmorRenderer.register(renderer, items);
        for (ItemConvertible item : items) {
            BY_ITEM.put(item.asItem(), renderer);
        }
    }

    @Nullable
    public static SkinArmorRenderer getForItem(Item item) {
        return BY_ITEM.get(item);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       ItemStack stack, LivingEntity entity, EquipmentSlot slot,
                       int light, BipedEntityModel<LivingEntity> contextModel) {
        boolean slim = isSlimFromContext(contextModel, entity);
        boolean inner = slot == EquipmentSlot.LEGS;
        PlayerEntityModel<LivingEntity> model = getOrBakeModel(pickLayer(inner, slim));

        contextModel.copyBipedStateTo(model);
        copyOverlayTransforms(model);
        setVisible(model, slot);

        Identifier texture = pickTexture(inner, slim);
        RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCull(texture);
        VertexConsumer consumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, renderLayer, false, stack.hasGlint());
        model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders this armor's matching arm + sleeve onto the player's first-person hand. The
     * {@code slim} flag is taken from the renderer's own model (via mixin accessor) rather than
     * {@link AbstractClientPlayerEntity#getModel()} so that the choice always agrees with which
     * variant of {@code PlayerEntityRenderer} is actually drawing the arm.
     */
    public void renderFirstPersonArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                     int light, AbstractClientPlayerEntity player, boolean rightArm,
                                     boolean slim, ItemStack stack) {
        PlayerEntityModel<LivingEntity> model = getOrBakeModel(slim ? this.outerSlim : this.outerWide);

        model.handSwingProgress = 0.0F;
        model.sneaking = false;
        model.leaningPitch = 0.0F;
        model.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        ModelPart arm = rightArm ? model.rightArm : model.leftArm;
        ModelPart sleeve = rightArm ? model.rightSleeve : model.leftSleeve;
        arm.visible = true;
        sleeve.visible = true;
        arm.pitch = 0.0F;
        sleeve.copyTransform(arm);

        Identifier texture = slim ? this.slimLayer1 : this.wideLayer1;
        RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCull(texture);
        VertexConsumer consumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, renderLayer, false, stack.hasGlint());
        arm.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
        sleeve.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
    }

    private EntityModelLayer pickLayer(boolean inner, boolean slim) {
        if (slim) {
            return inner ? this.innerSlim : this.outerSlim;
        }
        return inner ? this.innerWide : this.outerWide;
    }

    private Identifier pickTexture(boolean inner, boolean slim) {
        if (slim) {
            return inner ? this.slimLayer2 : this.slimLayer1;
        }
        return inner ? this.wideLayer2 : this.wideLayer1;
    }

    private static boolean isSlimFromContext(BipedEntityModel<LivingEntity> contextModel, LivingEntity entity) {
        if (contextModel instanceof PlayerEntityModel<?> playerModel) {
            return ((PlayerEntityModelAccessor) playerModel).quirks$thinArms();
        }
        return entity instanceof AbstractClientPlayerEntity p && "slim".equals(p.getModel());
    }

    private static void copyOverlayTransforms(PlayerEntityModel<LivingEntity> model) {
        model.jacket.copyTransform(model.body);
        model.leftSleeve.copyTransform(model.leftArm);
        model.rightSleeve.copyTransform(model.rightArm);
        model.leftPants.copyTransform(model.leftLeg);
        model.rightPants.copyTransform(model.rightLeg);
    }

    private static void setVisible(PlayerEntityModel<?> model, EquipmentSlot slot) {
        model.setVisible(false);
        switch (slot) {
            case HEAD -> {
                model.head.visible = true;
                model.hat.visible = true;
            }
            case CHEST -> {
                model.body.visible = true;
                model.jacket.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
                model.rightSleeve.visible = true;
                model.leftSleeve.visible = true;
            }
            case LEGS -> {
                model.body.visible = true;
                model.jacket.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                model.rightPants.visible = true;
                model.leftPants.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                model.rightPants.visible = true;
                model.leftPants.visible = true;
            }
            default -> {
            }
        }
    }

    private static PlayerEntityModel<LivingEntity> getOrBakeModel(EntityModelLayer layer) {
        return MODEL_CACHE.computeIfAbsent(layer, l -> {
            boolean slim = layer.getId().getPath().endsWith("_slim");
            return new PlayerEntityModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(l), slim);
        });
    }
}
