package azisaba.net.market;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketConfig {

    public static final int limit = Market.superMarket().getConfig().getInt("market-limit", 30);
    public static final Map<String, Double> marketMap = new HashMap<>();

    @Contract(pure = true)
    public static void save(String mmid, int amount, double money) {

        Configuration config = Market.superMarket().getMarketConfig();

        int i = 0;
        if (config.isSet("market." + mmid + ".amount")) {
            i+= config.getDoubleList("market." + mmid + ".amount").size();
        }

        Bukkit.broadcast(Component.text("あああ" + i));

        saveOne(i, amount, money, mmid);
        checkMarket(mmid);
    }

    public static void checkMarket(String mmid) {

        Configuration config = Market.superMarket().getMarketConfig();
        List<Double> list = config.getDoubleList("market." + mmid + ".amount");

        double count = 0;
        for (double dob : list) {
            count+= dob;
        }
        if (count == 0) {
            marketMap.put(mmid, count);
            return;
        }
        marketMap.put(mmid, count / limit);
    }

    public static void saveOne(int i, int amount, double money, String mmid) {

        Configuration config = Market.superMarket().getMarketConfig();
        double get = money / amount;

        int count = i + amount;
        Bukkit.broadcast(Component.text(count));
        if (count > limit) {

            int rem = count - limit;
            List<Double> list = new ArrayList<>();
            List<Double> gets = config.getDoubleList("market." + mmid + ".amount");
            for (double m : gets) {
                if (rem > 0) list.add(m);
                rem--;
            }
            gets.removeAll(list);
            config.set("market." + mmid + ".amount", gets);
            Market.superMarket().saveMarketConfig();
        }

        for (int one = 0; one < amount; one++) {
            config.set("market." + mmid + ".amount", get);
            Market.superMarket().saveMarketConfig();

            Bukkit.broadcast(Component.text("あ"));
        }
    }
}
