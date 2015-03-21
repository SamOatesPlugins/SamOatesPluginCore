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
     * @param titleColor
     * @param subtitle  The subtitle to show (or null to not show one)
     * @param subTitleColor
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
     * 
     * @param player
     * @param title 
     */
    public static void sendTitle(Player player, String title) {
        sendTitle(player, title, ChatColor.WHITE, null, ChatColor.WHITE, 1, 3, 1);
    }
    
    /**
     * 
     * @param player
     * @param title 
     * @param color 
     */
    public static void sendTitle(Player player, String title, ChatColor color) {
        sendTitle(player, title, color, null, ChatColor.WHITE, 1, 3, 1);
    }
    
    /**
     * 
     * @param player
     * @param title 
     * @param titleColor 
     * @param subTitle 
     */
    public static void sendTitle(Player player, String title, ChatColor titleColor, String subTitle) {
        sendTitle(player, title, titleColor, subTitle, ChatColor.WHITE, 1, 3, 1);
    }
    
    /**
     * 
     * @param player
     * @param title 
     * @param titleColor 
     * @param subtitleColor 
     * @param subTitle 
     */
    public static void sendTitle(Player player, String title, ChatColor titleColor, String subTitle, ChatColor subtitleColor) {
        sendTitle(player, title, titleColor, subTitle, subtitleColor, 1, 3, 1);
    }
}
