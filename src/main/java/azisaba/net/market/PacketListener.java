package azisaba.net.market;

import azisaba.net.mmoutils.utils.PlayerUtil;
import com.google.protobuf.ByteString;
import io.lumine.mythic.core.utils.FriendlyByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.entity.Player;

public class PacketListener extends azisaba.net.mmoutils.PacketListener {

    private static final MinecraftKey key = new MinecraftKey("mmo:market");
    private final Player p;
    public PacketListener(Player p) {
        super(p);
        this.p = p;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof ServerboundCustomPayloadPacket packet && packet.a().a().equals(key)) {

            ByteBuf payload = ((ServerboundCustomPayloadPacket.UnknownPayload)packet.a()).data();
            String mmid = ByteString.copyFrom(new byte[payload.readableBytes()]).toStringUtf8();

            double d = 0;
            if (MarketConfig.marketMap.containsKey(mmid)) d = MarketConfig.marketMap.get(mmid);
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeDouble(d);
            MinecraftKey key1 = new MinecraftKey("market1");

            PacketDataSerializer data1 = new PacketDataSerializer(buf);
            PlayerUtil.sendPacketPlayer(p, new ClientboundCustomPayloadPacket(data1.a(key1)));

            double d2 = 0;
            if (MarketConfig.market2Map.containsKey(mmid)) d2 = MarketConfig.market2Map.get(mmid);
            FriendlyByteBuf buf2 = new FriendlyByteBuf(Unpooled.buffer());
            buf2.writeDouble(d2);
            MinecraftKey key2 = new MinecraftKey("market2");

            PacketDataSerializer data2 = new PacketDataSerializer(buf);
            PlayerUtil.sendPacketPlayer(p,new ClientboundCustomPayloadPacket(data2.a(key2)));
        }
        super.channelRead(ctx, msg);
    }
}
