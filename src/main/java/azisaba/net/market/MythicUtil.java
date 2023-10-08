package azisaba.net.market;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.utils.jnbt.CompoundTag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MythicUtil {

    public static boolean isMythicItem(ItemStack item) {

        if (item == null || !item.hasItemMeta()) return false;
        CompoundTag tag = MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().getNBTData(item);
        if (tag == null) return false;
        return tag.containsKey("MYTHIC_TYPE");
    }

    public static @Nullable String getMythicID(ItemStack item) {

        if (isMythicItem(item)) {
            CompoundTag tag = MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().getNBTData(item);
            return tag.getString("MYTHIC_TYPE");
        }
        return null;
    }

    @Nullable
    public static ItemStack getMythicItem(String mmid) {
        return MythicBukkit.inst().getItemManager().getItemStack(mmid);
    }
}
