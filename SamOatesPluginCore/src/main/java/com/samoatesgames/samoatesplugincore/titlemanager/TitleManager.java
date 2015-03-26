package com.samoatesgames.samoatesplugincore.titlemanager;

import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleManager {

    /**
     * Send a title message to a player.
     * @param player    The player to send it too
     * @param title     The title to show (or null to not show one)
     * @param titleColor The colour of the title.
     * @param subtitle  The subtitle to show (or null to not show one)
     * @param subTitleColor The colour of the subtitle.
     * @param fadeIn    Fade in time
     * @param stay      How long to stay
     * @param fadeOut   Fade out time
     */
    public static void sendTitle(Player player, String title, ChatColor titleColor, String subtitle, ChatColor subTitleColor, int fadeIn, int stay, int fadeOut) {
        
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;

        if (title != null) {
            IChatBaseComponent titleJSON = ChatSerializer.a("{'text': '" + title + "', 'color':'" + titleColor.name() + "'}");
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleJSON, fadeIn * 20, stay * 20, fadeOut * 20);
            connection.sendPacket(titlePacket);
        }

        if (subtitle != null) {
            IChatBaseComponent subtitleJSON = ChatSerializer.a("{'text': '" + subtitle + "', 'color':'" + subTitleColor.name() + "'}");
            PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleJSON);
            connection.sendPacket(subtitlePacket);
        }
        
    }

    /**
     * Send a title message to a player. (in white text, showing for 5 seconds)
     * @param player Player to send it too.
     * @param title The title to show.
     */
    public static void sendTitle(Player player, String title) {
        sendTitle(player, title, ChatColor.WHITE, null, ChatColor.WHITE, 1, 3, 1);
    }
    
    /**
     * Send a title message to a player. (showing for 5 seconds)
     * @param player Player to send it too.
     * @param title The title to show.
     * @param color The colour of the title.
     */
    public static void sendTitle(Player player, String title, ChatColor color) {
        sendTitle(player, title, color, null, ChatColor.WHITE, 1, 3, 1);
    }
    
    /**
     * Send a title message to a player, with a white subtitle (showing for 5 seconds)
     * @param player Player to send it too.
     * @param title The title to show.
     * @param titleColor The colour of the title.
     * @param subTitle The subtitle to show.
     */
    public static void sendTitle(Player player, String title, ChatColor titleColor, String subTitle) {
        sendTitle(player, title, titleColor, subTitle, ChatColor.WHITE, 1, 3, 1);
    }
    
    /**
     * Send a title message to a player, with a subtitle (showing for 5 seconds)
     * @param player Player to send it too.
     * @param title The title to show.
     * @param titleColor The colour of the title.
     * @param subTitle The subtitle to show.
     * @param subtitleColor The colour of the subtitle.
     */
    public static void sendTitle(Player player, String title, ChatColor titleColor, String subTitle, ChatColor subtitleColor) {
        sendTitle(player, title, titleColor, subTitle, subtitleColor, 1, 3, 1);
    }
}
