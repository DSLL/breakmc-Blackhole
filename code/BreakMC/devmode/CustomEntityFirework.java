package code.BreakMC.devmode;

import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R4.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.*;

public class CustomEntityFirework extends EntityFireworks
{
    Player[] players;
    boolean gone;
    
    public CustomEntityFirework(final World world, final Player... p) {
        super(world);
        this.players = null;
        this.gone = false;
        this.players = p;
        this.a(0.25f, 0.25f);
    }
    
    public void h() {
        if (this.gone) {
            return;
        }
        if (!this.world.isStatic) {
            this.gone = true;
            if (this.players != null && this.players.length > 0) {
                Player[] players;
                for (int length = (players = this.players).length, i = 0; i < length; ++i) {
                    final Player player = players[i];
                    ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutEntityStatus((Entity)this, (byte)17));
                }
                this.die();
                return;
            }
            this.world.broadcastEntityEffect((Entity)this, (byte)17);
            this.die();
        }
    }
    
    public static void spawn(final Location location, final FireworkEffect effect, final Player... players) {
        try {
            final CustomEntityFirework firework = new CustomEntityFirework((World)((CraftWorld)location.getWorld()).getHandle(), players);
            final FireworkMeta meta = ((Firework)firework.getBukkitEntity()).getFireworkMeta();
            meta.addEffect(effect);
            ((Firework)firework.getBukkitEntity()).setFireworkMeta(meta);
            firework.setPosition(location.getX(), location.getY(), location.getZ());
            if (((CraftWorld)location.getWorld()).getHandle().addEntity((Entity)firework)) {
                firework.setInvisible(true);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
