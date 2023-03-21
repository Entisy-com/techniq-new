package de.neariyeveryone.techniq.screen;

import de.neariyeveryone.techniq.TechniqConstants;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS,
            TechniqConstants.MOD_ID);

    public static final RegistryObject<MenuType<TestBlockMenu>> TEST_MENU = register(
            TestBlockMenu::new, "test_block_menu"
    );

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(
            IContainerFactory<T> factory, String name) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }
}
