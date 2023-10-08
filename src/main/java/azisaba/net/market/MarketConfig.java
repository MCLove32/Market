package azisaba.net.market;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.Contract;

import java.util.*;

public class MarketConfig {

    public static final int limit = Market.superMarket().getConfig().getInt("market-limit", 30);
    public static final Map<String, Double> marketMap = new HashMap<>();

    @Contract(pure = true)
    public static void save(String mmid, int amount, double money) {

        Configuration config = Market.superMarket().getMarketConfig();

        int i = 0;
        List<Double> list = new ArrayList<>();

        if (config.isSet("market." + mmid)) {
            list = config.getDoubleList("market." + mmid + ".amount");
            if (list.isEmpty()) {
                list = new ArrayList<>(Collections.singleton(config.getDouble("market." + mmid + ".amount")));
            }
        }

        i+= list.size();

        saveOne(i, amount, money, mmid);
    }

    public static void checkMarket(String mmid) {

        Configuration config = Market.superMarket().getMarketConfig();
        List<Double> list = new ArrayList<>();

        if (config.isSet("market." + mmid)) {
            list = config.getDoubleList("market." + mmid + ".amount");
            if (list.isEmpty()) {
                list = new ArrayList<>(Collections.singleton(config.getDouble("market." + mmid + ".amount")));
            }
        }

        int listed;
        if (list.size() == limit) listed = limit;
        else listed = list.size();

        double count = 0;
        for (double dob : list) {
            count+= dob;
        }
        if (count == 0) {
            marketMap.put(mmid, count);
            return;
        }
        marketMap.put(mmid, count / listed);
    }

    public static void saveOne(int i, int amount, double money, String mmid) {

        Configuration config = Market.superMarket().getMarketConfig();
        List<Double> list = new ArrayList<>();

        int count = i + amount;
        if (count > limit) {

            int rem = count - limit;

            list = config.getDoubleList("market." + mmid + ".amount");
            List<Double> gets = new ArrayList<>();
            for (double dom : list) {
                if (rem > 0) {
                    gets.add(dom);
                    rem--;
                }
            }
            for (Double get : gets) {
                list.remove(get);
            }
            config.set("market." + mmid + ".amount", list);
            Market.superMarket().saveMarketConfig();
        }

        if (config.isSet("market." + mmid)) {

            list = config.getDoubleList("market." + mmid + ".amount");

            if (list.isEmpty()) {
                list = new ArrayList<>(Collections.singleton(config.getDouble("market." + mmid + ".amount")));
            }
        }

        double get = money / amount;
        Bukkit.broadcast(Component.text(get));

        for (int one = 1; one <= amount; one++) {
            list.add(get);
        }

        config.set("market." + mmid + ".amount", list);
        Market.superMarket().saveMarketConfig();

        checkMarket(mmid);
    }
}
