package me.kiiya.spectatoroptions.listeners.bedwars2023;

import com.tomkeuper.bedwars.api.events.player.PlayerJoinArenaEvent;
import me.kiiya.spectatoroptions.SpectatorOptions;
import me.kiiya.spectatoroptions.player.CachedOptions;
import me.kiiya.spectatoroptions.player.SpectatorManager;
import me.kiiya.spectatoroptions.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArenaJoin implements Listener {
    @EventHandler
    public void onArenaJoin(PlayerJoinArenaEvent e) {
        Player p = e.getPlayer();

        if (!e.isSpectator()) return;

        ItemStack item = Utils.getOptionsItem(p);
        int slot = SpectatorOptions.getInstance().getMainConfig().getInt("options-item.slot");

        if (!p.hasPermission("bw.spectatoroptions")) return;

        Bukkit.getScheduler().runTaskLater(SpectatorOptions.getInstance(), () -> {
            p.getInventory().setItem(slot, item);
        }, 20L);

        CachedOptions cachedOptions = SpectatorManager.getInstance().getCachedOptions(p);
        if (cachedOptions == null) return;

        if (cachedOptions.isToggleSpectators()) {
            for (Player spectator : e.getArena().getSpectators()) {
                p.hidePlayer(spectator);
            }
        } else {
            for (Player spectator : e.getArena().getSpectators()) {
                p.showPlayer(spectator);
            }
        }

        if (cachedOptions.isNightVision()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        } else {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }
}
