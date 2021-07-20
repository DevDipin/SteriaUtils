package net.helydev.com.listeners.advanced;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.helydev.com.SteriaUtils;
import org.bukkit.event.Listener;

public class AntiTabHandler implements Listener
{
    private final ProtocolManager protocolManager;

    public AntiTabHandler() {
        (this.protocolManager = ProtocolLibrary.getProtocolManager()).addPacketListener(new PacketAdapter(SteriaUtils.getPlugin(), PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Client.TAB_COMPLETE || event.getPlayer().isOp()) {
                    return;
                }
                final PacketContainer packetContainer = event.getPacket();
                final String mess = packetContainer.getSpecificModifier(String.class).read(0).toLowerCase();
                if (mess.equals("/")) {
                    event.setCancelled(true);
                }
                if (mess.contains(":")) {
                    event.setCancelled(true);
                }
                for (final String blocked : SteriaUtils.getPlugin().getConfig().getStringList("anti-tab.removed-list")) {
                    if (mess.startsWith("/" + blocked)) {
                        event.setCancelled(true);
                    }
                }
            }
        });
    }
}