package com.muhammaddaffa.nextgens.hooks.papi;

import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.utils.Config;
import com.muhammaddaffa.mdlib.utils.TimeFormat;
import com.muhammaddaffa.nextgens.events.Event;
import com.muhammaddaffa.nextgens.events.managers.EventManager;
import com.muhammaddaffa.nextgens.generators.managers.GeneratorManager;
import com.muhammaddaffa.nextgens.generators.runnables.CorruptionTask;
import com.muhammaddaffa.nextgens.users.User;
import com.muhammaddaffa.nextgens.users.managers.UserManager;
import com.muhammaddaffa.nextgens.utils.FormatBalance;
import com.muhammaddaffa.nextgens.utils.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GensExpansion extends PlaceholderExpansion {

    private final GeneratorManager generatorManager;
    private final UserManager userManager;
    private final EventManager eventManager;

    public GensExpansion(GeneratorManager generatorManager, UserManager userManager, EventManager eventManager) {
        this.generatorManager = generatorManager;
        this.userManager = userManager;
        this.eventManager = eventManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nextgens";
    }

    @Override
    public @NotNull String getAuthor() {
        return "aglerr";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        final User user = this.userManager.getUser(player);


        if (params.equalsIgnoreCase("statistics_totalsell_formatted")) {
            return Utils.formatBalance(user.getTotalSell());
        }
        if (params.equalsIgnoreCase("statistics_totalsell")) {
            return Common.digits(user.getTotalSell());
        }
        if (params.equalsIgnoreCase("statistics_sellwandsell_formatted")) {
            return Utils.formatBalance(user.getSellwandSell());
        }
        if (params.equalsIgnoreCase("statistics_sellwandsell")) {
            return Common.digits(user.getSellwandSell());
        }
        if (params.equalsIgnoreCase("statistics_commandsell_formatted")) {
            return Utils.formatBalance(user.getNormalSell());
        }
        if (params.equalsIgnoreCase("statistics_commandsell")) {
            return Common.digits(user.getNormalSell());
        }
        if (params.equalsIgnoreCase("statistics_itemsold_formatted")) {
            return Utils.formatBalance(user.getItemsSold());
        }
        if (params.equalsIgnoreCase("statistics_itemsold")) {
            return Common.digits(user.getItemsSold());
        }
        if (params.equalsIgnoreCase("statistics_earnings_formatted")) {
            return Utils.formatBalance((long) user.getEarnings());
        }
        if (params.equalsIgnoreCase("statistics_earnings")) {
            return Common.digits(user.getEarnings());
        }
        if (params.equalsIgnoreCase("multiplier")) {
            return Common.digits(user.getMultiplier());
        }
        if (params.equalsIgnoreCase("currentplaced")) {
            return Common.digits(this.generatorManager.getGeneratorCount(player));
        }
        if (params.equalsIgnoreCase("max")) {
            return Common.digits(this.userManager.getMaxSlot(player));
        }
        if (params.equalsIgnoreCase("total_generator")) {
            return Common.digits(this.generatorManager.getActiveGenerator().size());
        }
        if (params.equalsIgnoreCase("corrupt_time")) {
            return TimeFormat.parse(CorruptionTask.getTimeLeft());
        }
        if (params.equalsIgnoreCase("event_name")) {
            Event event = this.eventManager.getActiveEvent();
            if (event == null) {
                return Config.getFileConfiguration("events.yml").getString("events.placeholders.no-event");
            }
            return Config.getFileConfiguration("events.yml").getString("events.placeholders.active-event")
                    .replace("{display_name}", event.getDisplayName());
        }
        if (params.equalsIgnoreCase("event_time")) {
            Event event = this.eventManager.getActiveEvent();
            if (event == null) {
                return Config.getFileConfiguration("events.yml").getString("events.placeholders.no-event-timer")
                        .replace("{timer}", TimeFormat.parse((long) this.eventManager.getWaitTime()));
            }
            return Config.getFileConfiguration("events.yml").getString("events.placeholders.active-event-timer")
                    .replace("{timer}", TimeFormat.parse((long) event.getDuration()));
        }

        return null; // Placeholder is unknown by the Expansion
    }

}
