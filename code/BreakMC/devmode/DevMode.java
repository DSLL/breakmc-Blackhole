package code.BreakMC.devmode;

import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.*;
import org.bukkit.util.*;
import org.bukkit.scheduler.*;
import java.util.*;
import org.bukkit.event.block.*;
import org.bukkit.event.*;

public class DevMode extends JavaPlugin implements Listener
{
    public static DevMode instance;
    public ArrayList<UUID> devmode;
    public ArrayList<UUID> blackhole;
    public ArrayList<UUID> fireWorkMode;
    public HashMap<UUID, Integer> TID;
    
    public DevMode() {
        super();
        this.devmode = new ArrayList<UUID>();
        this.blackhole = new ArrayList<UUID>();
        this.fireWorkMode = new ArrayList<UUID>();
        this.TID = new HashMap<UUID, Integer>();
    }
    
    public void onEnable() {
        DevMode.instance = this;
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdLabel, final String[] args) {
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("devmode") && p.hasPermission("breakmc.devmode")) {
            if (!this.devmode.contains(p.getUniqueId())) {
                p.sendMessage("§aYour greater knowledge of coding escapes your body.");
                this.devmode.add(p.getUniqueId());
                this.startScheduler(p);
                p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
            }
            else {
                p.sendMessage("§cYour greater knowledge of coding returns to your body.");
                this.devmode.remove(p.getUniqueId());
                this.stopScheduler(p);
                p.playSound(p.getLocation(), Sound.WITHER_DEATH, 1.0f, 1.0f);
            }
        }
        if (cmd.getName().equalsIgnoreCase("blackhole")) {
            if (!p.getName().equalsIgnoreCase("Young_Explicit")) {
                return false;
            }
            if (!this.blackhole.contains(p.getUniqueId())) {
                p.sendMessage("§aBlackHole activated. Prepare for mass destruction.");
                this.blackhole.add(p.getUniqueId());
                this.startScheduler(p);
            }
            else {
                p.sendMessage("§cBlackHole deactivated. Thank the Gaberino.");
                this.blackhole.remove(p.getUniqueId());
                this.stopScheduler(p);
            }
        }
        if (cmd.getName().equalsIgnoreCase("clearfb")) {
            if (!p.hasPermission("breakmc.clearfb")) {
                return false;
            }
            p.sendMessage("§aClearing All Falling Blocks");
            for (final Entity ents : Bukkit.getWorld("world").getEntities()) {
                if (ents.getType() == EntityType.FALLING_BLOCK) {
                    final FallingBlock fb = (FallingBlock)ents;
                    fb.remove();
                }
            }
        }
        return false;
    }
    
    public void startScheduler(final Player p) {
        final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                if (DevMode.this.devmode.contains(p.getUniqueId())) {
                    ParticleEffect.ENCHANTMENT_TABLE.display(0.0f, 0.0f, 0.0f, 1.0f, 15, p.getLocation().add(0.0, 2.0, 0.0), 100.0);
                    for (final Entity ent : p.getNearbyEntities(4.0, 4.0, 4.0)) {
                        if (ent instanceof Player) {
                            final Player nearby = (Player)ent;
                            final Location loc = nearby.getLocation();
                            final Vector direction = nearby.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction.setX(direction.getX() * 1.0);
                            direction.setY(1);
                            direction.setZ(direction.getZ() * 1.0);
                            nearby.setVelocity(direction);
                            nearby.sendMessage("§cYou cannot comprehend the developers knowledge.");
                            ParticleEffect.FLAME.display(0.0f, 0.0f, 1.0f, 1.0f, 50, loc, 100.0);
                            nearby.playSound(loc, Sound.ENDERDRAGON_HIT, 1.0f, 1.0f);
                            p.playSound(loc, Sound.ENDERDRAGON_HIT, 1.0f, 1.0f);
                        }
                    }
                    for (final Entity ent : p.getNearbyEntities(4.0, 4.0, 4.0)) {
                        if (ent instanceof Entity) {
                            final Entity nearby2 = ent;
                            final Location loc = nearby2.getLocation();
                            final Vector direction = nearby2.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction.setX(direction.getX() * 1.0);
                            direction.setY(1);
                            direction.setZ(direction.getZ() * 1.0);
                            nearby2.setVelocity(direction);
                            ParticleEffect.FLAME.display(0.0f, 0.0f, 1.0f, 1.0f, 50, loc, 100.0);
                            p.playSound(loc, Sound.ENDERDRAGON_HIT, 1.0f, 1.0f);
                        }
                    }
                }
                if (DevMode.this.blackhole.contains(p.getUniqueId())) {
                    final List<BlockState> blocks = new ArrayList<BlockState>();
                    for (final Block nearby3 : DevMode.getNearbyBlocks(p.getLocation(), 10)) {
                        if (nearby3.getType() != Material.AIR) {
                            final Byte blockData = 0;
                            final FallingBlock fb = nearby3.getLocation().getWorld().spawnFallingBlock(nearby3.getLocation(), nearby3.getType(), (byte)blockData);
                            blocks.add(nearby3.getState());
                            nearby3.setType(Material.AIR);
                            fb.setDropItem(false);
                        }
                    }
                    for (final Entity near : p.getNearbyEntities(5.0, 5.0, 5.0)) {
                        if (!(near instanceof Player)) {
                            final Vector direction2 = near.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction2.setX(direction2.getX() * -5.0);
                            direction2.setZ(direction2.getZ() * -5.0);
                            direction2.setY(direction2.getY() * -5.0);
                            near.setVelocity(direction2);
                        }
                    }
                    for (final Entity near : p.getNearbyEntities(10.0, 10.0, 10.0)) {
                        if (!(near instanceof Player)) {
                            final Vector direction2 = near.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction2.setX(direction2.getX() * -1.0);
                            direction2.setZ(direction2.getZ() * -1.0);
                            direction2.setY(direction2.getY() * -1.0);
                            near.setVelocity(direction2);
                        }
                    }
                    for (final Entity near : p.getNearbyEntities(20.0, 20.0, 20.0)) {
                        if (!(near instanceof Player)) {
                            final Vector direction2 = near.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                            direction2.setX(direction2.getX() * -1.0);
                            direction2.setZ(direction2.getZ() * -1.0);
                            direction2.setY(direction2.getY() * -1.0);
                            near.setVelocity(direction2);
                        }
                    }
                    for (final Entity near : p.getNearbyEntities(1.0, 1.0, 1.0)) {
                        if (!(near instanceof Player)) {
                            near.remove();
                        }
                    }
                    DevMode.performWorldRegen(blocks, p.getWorld().getSpawnLocation(), 12, 120L);
                }
            }
        }, 0L, 2L);
        this.TID.put(p.getUniqueId(), id);
    }
    
    public void stopScheduler(final Player p) {
        if (this.TID.containsKey(p.getUniqueId())) {
            final int id = this.TID.get(p.getUniqueId());
            Bukkit.getScheduler().cancelTask(id);
            this.TID.remove(p.getUniqueId());
        }
    }
    
    public static void performWorldRegen(final List<BlockState> blocks, final Location center, final int blocksPerTime, final long delay) {
        new BukkitRunnable() {
            public void run() {
                BlockUtils.regenerateBlocks(blocks, blocksPerTime, 12L, new Comparator<BlockState>() {
                    @Override
                    public int compare(final BlockState state1, final BlockState state2) {
                        return Double.compare(state1.getLocation().distance(center), state2.getLocation().distance(center));
                    }
                });
            }
        }.runTaskLater((Plugin)DevMode.instance, delay);
    }
    
    public static List<Location> Circle(final Location loc, final Integer r, final Integer h, final Boolean hollow, final Boolean sphere, final int plus_y) {
        final List<Location> circleblocks = new ArrayList<Location>();
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; ++x) {
            for (int z = cz - r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - r) : cy; y < (sphere ? (cy + r) : (cy + h)); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1) * (r - 1))) {
                        final Location l = new Location(loc.getWorld(), (double)x, (double)(y + plus_y), (double)z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public static List<Block> getNearbyBlocks(final Location center, final int radius) {
        final List<Location> locs = circle(center, radius, radius, false, true, 0);
        final List<Block> blocks = new ArrayList<Block>();
        for (final Location loc : locs) {
            blocks.add(loc.getBlock());
        }
        return blocks;
    }
    
    public static List<Location> circle(final Location loc, final int radius, final int height, final boolean hollow, final boolean sphere, final int plusY) {
        final List<Location> circleblocks = new ArrayList<Location>();
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        for (int x = cx - radius; x <= cx + radius; ++x) {
            for (int z = cz - radius; z <= cz + radius; ++z) {
                for (int y = sphere ? (cy - radius) : cy; y < (sphere ? (cy + radius) : (cy + height)); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < radius * radius && (!hollow || dist >= (radius - 1) * (radius - 1))) {
                        final Location l = new Location(loc.getWorld(), (double)x, (double)(y + plusY), (double)z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    @EventHandler
    public void onLeafDecay(final LeavesDecayEvent e) {
        e.setCancelled(true);
    }
}
