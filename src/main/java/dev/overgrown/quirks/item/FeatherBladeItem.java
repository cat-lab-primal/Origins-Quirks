package dev.overgrown.quirks.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class FeatherBladeItem extends ToolItem {
    private final float attackDamage;
    private final float attackSpeed;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public FeatherBladeItem(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, settings.maxDamageIfAbsent(material.getDurability()));
        this.attackDamage = attackDamage + material.getAttackDamage();
        this.attackSpeed = attackSpeed;
        
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
            EntityAttributes.GENERIC_ATTACK_DAMAGE,
            new EntityAttributeModifier(Item.ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double) this.attackDamage, EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(
            EntityAttributes.GENERIC_ATTACK_SPEED,
            new EntityAttributeModifier(Item.ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", (double) this.attackSpeed, EntityAttributeModifier.Operation.ADDITION)
        );
        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        EquipmentSlot slot = attacker.getMainHandStack() == stack ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(slot));
        return true;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !world.isClient) {
            int useTime = this.getMaxUseTime(stack) - remainingUseTicks;
            if (useTime >= 10) {
                applyChargeAttack(player, stack, useTime);
            }
        }
    }

    private void applyChargeAttack(PlayerEntity player, ItemStack stack, int useTime) {
        float chargeMultiplier = Math.min(useTime / 20f, 3f);
        float damage = this.attackDamage * chargeMultiplier;

        Box damageBox = player.getBoundingBox().expand(2.0);
        for (LivingEntity entity : player.getWorld().getEntitiesByClass(LivingEntity.class, damageBox,
                entity -> entity != player && entity.isAlive() && player.distanceTo(entity) <= 2.0)) {
            entity.damage(player.getDamageSources().playerAttack(player), damage);

            if (player.getRandom().nextInt(5) == 0) {
                EquipmentSlot slot = player.getMainHandStack() == stack ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.damage(1, player, e -> e.sendEquipmentBreakStatus(slot));
            }
        }
    }
}