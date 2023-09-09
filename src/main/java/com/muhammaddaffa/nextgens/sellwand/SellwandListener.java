package com.muhammaddaffa.nextgens.sellwand;

import com.griefcraft.lwc.LWC;
import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.nextgens.events.managers.EventManager;
import com.muhammaddaffa.nextgens.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public record SellwandListener(
        SellwandManager sellwandManager
) implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack stack = event.getItem();
        // if item is not a sellwand, skip
        if (!this.sellwandManager.isSellwand(stack)) {
            return;
        }
        if (block.getState() instanceof Container container) {
            // LWC check
            if (Bukkit.getPluginManager().getPlugin("LWC") != null && !LWC.getInstance().canAccessProtection(player, block)) {
                // send a message and do nothing
                Common.configMessage("config.yml", player, "messages.sellwand-failed");
                // cancel the event
                event.setCancelled(true);
                // bass sound
                Utils.bassSound(player);
                return;
            }
            // try to sell the content of the chest
            if (this.sellwandManager.action(player, stack, container.getInventory())) {
                event.setCancelled(true);
            }
        }
    }

}