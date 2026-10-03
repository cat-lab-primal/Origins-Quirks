package dev.overgrown.quirks.mixin.client;

import dev.overgrown.quirks.armor.client.SkinArmorRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @Inject(
            method = "renderArm",
            at = @At(
                    "RETURN"
            )
    )
    private void quirks$renderFirstPersonSkinArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                                   int light, AbstractClientPlayerEntity player,
                                                   ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
        SkinArmorRenderer renderer = SkinArmorRenderer.getForItem(chestStack.getItem());
        if (renderer == null) {
            return;
        }
        PlayerEntityRenderer self = (PlayerEntityRenderer) (Object) this;
        PlayerEntityModel<AbstractClientPlayerEntity> model = self.getModel();
        boolean rightArm = arm == model.rightArm;
        boolean slim = ((PlayerEntityModelAccessor) model).quirks$thinArms();
        renderer.renderFirstPersonArm(matrices, vertexConsumers, light, player, rightArm, slim, chestStack);
    }
}
