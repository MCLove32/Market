package azisaba.net.market;

import azisaba.net.mmoutils.MMOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Market extends JavaPlugin {

    private static Market market;
    public Market() {market = this;}
    public static Market superMarket() {return market;}

    private File file;
    private FileConfiguration fileConfiguration;
    private File file2;
    private FileConfiguration fileConfiguration2;
    public static final String id = "market";

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        create();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BoughtListener(), this);

        Bukkit.getOnlinePlayers().forEach(player -> MMOUtils.getUtils().packetSetUP(player, id, new PacketListener(player)));
        Bukkit.getScheduler().runTaskAsynchronously(this, this::check);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getOnlinePlayers().forEach(player -> MMOUtils.getUtils().packetRemove(player, id));
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
}
