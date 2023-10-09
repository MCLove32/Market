package azisaba.net.market;

import com.google.protobuf.ByteString;
import io.lumine.mythic.core.utils.FriendlyByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutCustomPayload;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class Market extends JavaPlugin implements PluginMessageListener {

    private static Market market;
    public Market() {market = this;}
    public static Market superMarket() {return market;}

    private File file;
    private FileConfiguration fileConfiguration;
    private File file2;
    private FileConfiguration fileConfiguration2;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setChannelMessage();

        saveDefaultConfig();
        create();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BoughtListener(), this);


        Bukkit.getScheduler().runTaskAsynchronously(this, this::check);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unSetChannelMessage();
    }

    public FileConfiguration getMarketConfig() {
        return fileConfiguration;
    }
    public FileConfiguration getMarket2Config() {return fileConfiguration2;}

    private void create() {

        file = new File(getDataFolder(), "market.yml");
        file2 = new File(getDataFolder(), "market2.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource("market.yml", false);
        }
        if (!file2.exists()) {
            file2.getParentFile().mkdirs();
            saveResource("market2.yml", false);
        }

        fileConfiguration = new YamlConfiguration();
        fileConfiguration2 = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
            fileConfiguration2.load(file2);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveMarketConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMarket2Config() {
        try {
            fileConfiguration2.save(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void check() {

        ConfigurationSection section = getMarketConfig().getConfigurationSection("market");
        if (section == null) return;

        for (String s : section.getKeys(false)) {
            if (s == null) continue;
            MarketConfig.checkMarket(s);
        }

        ConfigurationSection section2 = getMarket2Config().getConfigurationSection("market");
        if (section2 == null) return;
        for (String s: section2.getKeys(false)) {
            if (s == null) continue;
            MarketConfig.checkMarket2(s);
        }
    }

    public void setChannelMessage() {
        getServer().getMessenger().registerIncomingPluginChannel(this, "mmo:market", this);
    }

    public void unSetChannelMessage() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String s, @NotNull Player player, byte[] bytes) {

        if (s.equals("mmo:market")) {

            String mmid = ByteString.copyFrom(bytes).toStringUtf8();
            double d = 0;
            double d2 = 0;

            if (MarketConfig.marketMap.containsKey(mmid)) d = MarketConfig.marketMap.get(mmid);
            if (MarketConfig.market2Map.containsKey(mmid)) d2 = MarketConfig.market2Map.get(mmid);

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeDouble(d);
            buf2.writeDouble(d2);

            MinecraftKey key = new MinecraftKey("mmo:market1");
            MinecraftKey key2 = new MinecraftKey("mmo:market2");


            PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(key, new PacketDataSerializer(buf));
            PlayerUtil.sendPacketPlayer(player, packet);

            PacketPlayOutCustomPayload packet2 = new PacketPlayOutCustomPayload(key2, new PacketDataSerializer(buf2));
            PlayerUtil.sendPacketPlayer(player, packet2);
        }
    }
}
