package dev.overgrown.quirks.item;

import dev.overgrown.quirks.Quirks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {

    public static final ArmorItem GAMMA_CHESTPLATE = new ArmorItem(ModArmorMaterials.GAMMA, ArmorItem.Type.CHESTPLATE, baseSettings());
    public static final ArmorItem GAMMA_LEGGINGS = new ArmorItem(ModArmorMaterials.GAMMA, ArmorItem.Type.LEGGINGS, baseSettings());
    public static final ArmorItem GAMMA_BOOTS = new ArmorItem(ModArmorMaterials.GAMMA, ArmorItem.Type.BOOTS, baseSettings());
    
    // Violet Dress Armor - Leather level protection
    public static final ArmorItem VIOLET_DRESS_CHESTPLATE = new ArmorItem(ModArmorMaterials.VIOLET_DRESS, ArmorItem.Type.CHESTPLATE, baseSettings());
    public static final ArmorItem VIOLET_DRESS_LEGGINGS = new ArmorItem(ModArmorMaterials.VIOLET_DRESS, ArmorItem.Type.LEGGINGS, baseSettings());
    public static final ArmorItem VIOLET_DRESS_BOOTS = new ArmorItem(ModArmorMaterials.VIOLET_DRESS, ArmorItem.Type.BOOTS, baseSettings());
    
    // Custom items
    public static final Item FIERCE_WINGS_FEATHER = new Item(baseSettings());
    public static final FeatherBladeItem FEATHER_BLADE = new FeatherBladeItem(ModToolMaterials.FEATHER_BLADE, 3, -2.4F, baseSettings());

    public static void registerItems() {
        register("gamma_chestplate", GAMMA_CHESTPLATE);
        register("gamma_leggings", GAMMA_LEGGINGS);
        register("gamma_boots", GAMMA_BOOTS);
        
        // Register violet dress armor
        register("violet_dress_chestplate", VIOLET_DRESS_CHESTPLATE);
        register("violet_dress_leggings", VIOLET_DRESS_LEGGINGS);
        register("violet_dress_boots", VIOLET_DRESS_BOOTS);
        
        // Register custom items
        register("fierce_wings_feather", FIERCE_WINGS_FEATHER);
        register("feather_blade", FEATHER_BLADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(GAMMA_CHESTPLATE);
            entries.add(GAMMA_LEGGINGS);
            entries.add(GAMMA_BOOTS);
            
            // Add violet dress armor to combat tab
            entries.add(VIOLET_DRESS_CHESTPLATE);
            entries.add(VIOLET_DRESS_LEGGINGS);
            entries.add(VIOLET_DRESS_BOOTS);
            
            // Add custom items to combat tab
            entries.add(FIERCE_WINGS_FEATHER);
            entries.add(FEATHER_BLADE);
        });
    }

    private static Item.Settings baseSettings() {
        return new Item.Settings().maxCount(1);
    }

    private static void register(String path, Item item) {
        Registry.register(Registries.ITEM, Quirks.identifier(path), item);
    }
}
