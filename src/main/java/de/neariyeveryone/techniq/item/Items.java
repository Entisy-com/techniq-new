package de.neariyeveryone.techniq.item;

import de.neariyeveryone.techniq.Techniq;
import de.neariyeveryone.techniq.TechniqConstants;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public class Items {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechniqConstants.MOD_ID);
    public static final ArrayList<RegistryObject<Item>> simpleItems = new ArrayList<>();

    public static final RegistryObject<Item> ROD_PRESS = registerPressItem("rod_press");
    public static final RegistryObject<Item> PLATE_PRESS = registerPressItem("plate_press");

    public static final RegistryObject<Item> IRON_ROD = registerSimpleItem("iron_rod");
    public static final RegistryObject<Item> GOLD_ROD = registerSimpleItem("gold_rod");
    public static final RegistryObject<Item> DIAMOND_ROD = registerSimpleItem("diamond_rod");

    public static final RegistryObject<Item> IRON_PLATE = registerSimpleItem("iron_plate");
    public static final RegistryObject<Item> GOLD_PLATE = registerSimpleItem("gold_plate");
    public static final RegistryObject<Item> DIAMOND_PLATE = registerSimpleItem("diamond_plate");

//    public static final RegistryObject<Item> RAW_AMBER = registerSimpleItem("raw_amber");
//    public static final RegistryObject<Item> AMBER = registerSimpleItem("amber");

    public static RegistryObject<Item> registerPressItem(String name) {
        var toReturn = ITEMS.register(name, () -> new Item(new Item.Properties().tab(Techniq.TAB).stacksTo(1)));
        simpleItems.add(toReturn);
        return toReturn;
    }

    public static RegistryObject<Item> registerSimpleItem(String name) {
        var toReturn = ITEMS.register(name, () -> new Item(new Item.Properties().tab(Techniq.TAB)));
        simpleItems.add(toReturn);
        return toReturn;
    }
}
