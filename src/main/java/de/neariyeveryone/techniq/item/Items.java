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

    public static final RegistryObject<Item> RAW_AMBER = registerSimpleItem("raw_amber");
    public static final RegistryObject<Item> AMBER = registerSimpleItem("amber");

    public static RegistryObject<Item> registerSimpleItem(String name) {
        var toReturn = ITEMS.register(name, () -> new Item(new Item.Properties().tab(Techniq.TAB)));
        simpleItems.add(toReturn);
        return toReturn;
    }
}
