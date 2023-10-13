package azisaba.net.market;

import azisaba.net.mmoutils.MMOUtils;
import azisaba.net.mmoutils.utils.MythicUtil;
import net.akarian.auctionhouse.listings.Listing;
import net.akarian.auctionhouse.utils.events.ListingBoughtEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BoughtListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBought(@NotNull ListingBoughtEvent e) {

        Listing listing = e.getListing();
        ItemStack item = listing.getItemStack();

        if (!(MythicUtil.isMythicItem(item))) return;
        String mmid = MythicUtil.getMythicID(item);

        if (mmid == null) return;

        int amount = item.getAmount();
        double money = listing.getPrice();

        Bukkit.getScheduler().runTaskAsynchronously(Market.superMarket(), ()-> {
            MarketConfig.save1(mmid, amount, money);
            MarketConfig.save2(mmid, amount);
        });
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {

        Player p = e.getPlayer();
        MMOUtils.getUtils().packetSetUP(p, Market.id, new PacketListener(p));
    }
}
