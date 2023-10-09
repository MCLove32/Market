package azisaba.net.market;

import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MarketConfig {

    public static final int limit = Market.superMarket().getConfig().getInt("market-limit", 30);
    public static final Map<String, Double> marketMap = new HashMap<>();
    public static final Map<String, Double> market2Map = new HashMap<>();

    @Contract(pure = true)
    public static void save1(String mmid, int amount, double money) {

        Configuration config = Market.superMarket().getMarketConfig();
        List<Double> list = new ArrayList<>();
        list = getList(list, config, mmid);

        int count = list.size() + amount;
        list = removeBeforeOne(list, count, config, mmid);
        Market.superMarket().saveMarketConfig();

        double get = money / amount;
        for (int one = 1; one <= amount; one++) {
            list.add(get);
        }
        config.set("market." + mmid + ".amount", list);
        Market.superMarket().saveMarketConfig();

        checkMarket(mmid);
    }

    @Contract(pure = true)
    public static void save2(String mmid, int amount) {

        Configuration config = Market.superMarket().getMarket2Config();
        List<Integer> bought = new ArrayList<>();
        bought = getBoughtList(bought, config, mmid);

        int count = bought.size() + 1;
        bought = removeBeforeBoughtOne(bought, count, config, mmid);
        Market.superMarket().saveMarket2Config();

        bought.add(amount);
        config.set("market." + mmid + ".bought", bought);
        Market.superMarket().saveMarket2Config();

        checkMarket2(mmid);
    }

    public static void checkMarket2(String mmid) {

        Configuration config2 = Market.superMarket().getMarket2Config();
        Configuration config = Market.superMarket().getMarketConfig();
        List<Integer> list = new ArrayList<>();
        List<Double> bought = new ArrayList<>();
        list = getBoughtList(list, config2, mmid);
        bought = getList(bought, config, mmid);

        int count = 0;
        double value = 0;

        for (int in: list) {
            count+= in;
        }
        for (double dob: bought) {
            value+= dob;
        }
        if (count == 0 || value == 0) {
            market2Map.put(mmid, value);
            return;
        }
        market2Map.put(mmid, value / count);
    }

    public static void checkMarket(String mmid) {

        Configuration config = Market.superMarket().getMarketConfig();
        List<Double> list = new ArrayList<>();
        list = getList(list, config, mmid);

        int listed = Math.min(list.size(), limit);
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

    public static List<Double> removeBeforeOne(List<Double> list, int count, Configuration config, String mmid) {

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
            for (double get : gets) {
                list.remove(get);
            }
            config.set("market." + mmid + ".amount", list);
        }
        return list;
    }

    public static List<Integer> removeBeforeBoughtOne(List<Integer> list, int count, Configuration config, String mmid) {

        if (count > limit) {

            int rem = count - limit;
            list = config.getIntegerList("market." + mmid + ".bought");
            List<Integer> gets = new ArrayList<>();
            for (int in : list) {
                if (rem > 0) {
                    gets.add(in);
                    rem--;
                }
            }
            for (int get : gets) {
                list.remove(get);
            }
            config.set("market." + mmid + ".bought", list);
        }
        return list;
    }

    public static List<Double> getList(List<Double> list, @NotNull Configuration config, String mmid) {

        if (config.isSet("market." + mmid)) {
            list = config.getDoubleList("market." + mmid + ".amount");
            if (list.isEmpty()) {
                list = new ArrayList<>(Collections.singleton(config.getDouble("market." + mmid + ".amount")));
            }
        }
        return list;
    }

    public static List<Integer> getBoughtList(List<Integer> list, @NotNull Configuration config, String mmid) {

        if (config.isSet("market." + mmid)) {
            list = config.getIntegerList("market." + mmid + ".bought");
            if (list.isEmpty()) {
                list = new ArrayList<>(Collections.singleton(config.getInt("market." + mmid + ".bought")));
            }
        }
        return list;
    }
}
