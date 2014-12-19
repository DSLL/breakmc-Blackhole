package code.BreakMC.devmode;

import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.block.*;
import org.bukkit.*;
import java.util.*;

public class BlockUtils
{
    public static void regenerateBlocks(final Collection<Block> blocks, final Material type, final byte data, final int blocksPerTime, final long delay, final Comparator<Block> comparator) {
        final List<Block> orderedBlocks = new ArrayList<Block>();
        orderedBlocks.addAll(blocks);
        if (comparator != null) {
            Collections.sort(orderedBlocks, comparator);
        }
        final int size = orderedBlocks.size();
        if (size > 0) {
            new BukkitRunnable(size) {
                int index = n - 1;
                
                public void run() {
                    for (int i = 0; i < blocksPerTime; ++i) {
                        if (this.index < 0) {
                            this.cancel();
                            return;
                        }
                        final Block block = orderedBlocks.get(this.index);
                        BlockUtils.regenerateBlock(block, type, data);
                        --this.index;
                    }
                }
            }.runTaskTimer((Plugin)DevMode.instance, 0L, delay);
        }
    }
    
    public static void regenerateBlocks(final Collection<BlockState> blocks, final int blocksPerTime, final long delay, final Comparator<BlockState> comparator) {
        final List<BlockState> orderedBlocks = new ArrayList<BlockState>();
        orderedBlocks.addAll(blocks);
        if (comparator != null) {
            Collections.sort(orderedBlocks, comparator);
        }
        final int size = orderedBlocks.size();
        if (size > 0) {
            new BukkitRunnable(size) {
                int index = n - 1;
                
                public void run() {
                    for (int i = 0; i < blocksPerTime; ++i) {
                        if (this.index < 0) {
                            this.cancel();
                            return;
                        }
                        final BlockState state = orderedBlocks.get(this.index);
                        BlockUtils.regenerateBlock(state.getBlock(), state.getType(), state.getData().getData());
                        --this.index;
                    }
                }
            }.runTaskTimer((Plugin)DevMode.instance, 0L, delay);
        }
    }
    
    public static void regenerateBlock(final Block block, final Material type, final byte data) {
        final Location loc = block.getLocation();
        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, (type == Material.AIR) ? block.getType().getId() : type.getId());
        block.setType(type);
        block.setData(data);
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
    
    public static List<Block> getRelativeBlocks(final Location center, final int radius, final Collection<Block> startBlocks, final Set<Material> types) {
        final List<Block> relative = new ArrayList<Block>();
        for (int x = -radius; x < radius; ++x) {
            for (int y = -radius; y < radius; ++y) {
                for (int z = -radius; z < radius; ++z) {
                    final Block newBlock = center.getBlock().getRelative(x, y, z);
                    if (types.contains(newBlock.getType()) && newBlock.getLocation().distance(center) <= radius) {
                        relative.add(newBlock);
                    }
                }
            }
        }
        return relative;
    }
    
    public static List<Block> getNearbyBlocks(final Location center, final int radius) {
        final List<Location> locs = circle(center, radius, radius, true, true, 0);
        final List<Block> blocks = new ArrayList<Block>();
        for (final Location loc : locs) {
            blocks.add(loc.getBlock());
        }
        return blocks;
    }
}
