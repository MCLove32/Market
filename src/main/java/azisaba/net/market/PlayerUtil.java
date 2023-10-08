package azisaba.net.market;

import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class PlayerUtil {

    @NotNull
    public static Channel getChannel(@NotNull Player player) throws IllegalAccessException, NoSuchFieldException {

        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        Field manager = connection.getClass().getDeclaredField("h");
        manager.setAccessible(true);
        NetworkManager object = (NetworkManager) manager.get(connection);

        return object.m;
    }

    public static void sendPacketPlayer(Player p, Packet<?> packet) {

        ((CraftPlayer) p).getHandle().b.a(packet);
    }
}
