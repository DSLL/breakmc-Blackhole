package code.BreakMC.devmode;

import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;
import java.util.*;
import java.lang.reflect.*;
import org.bukkit.*;

public class FireworkPacket
{
    DevMode plugin;
    private Class<?> packetPlayOutEntityDestroy;
    private Method getPlayerHandle;
    private Method getFireworkHandle;
    private Field getPlayerConnection;
    private Method sendPacket;
    
    public FireworkPacket(final DevMode plugin) {
        super();
        try {
            this.plugin = plugin;
            this.packetPlayOutEntityDestroy = this.getMCClass("PacketPlayOutEntityDestroy");
            this.getPlayerHandle = this.getCraftClass("entity.CraftPlayer").getMethod("getHandle", (Class<?>[])new Class[0]);
            this.getFireworkHandle = this.getCraftClass("entity.CraftFirework").getMethod("getHandle", (Class<?>[])new Class[0]);
            this.getPlayerConnection = this.getMCClass("EntityPlayer").getDeclaredField("playerConnection");
            this.sendPacket = this.getMCClass("PlayerConnection").getMethod("sendPacket", this.getMCClass("Packet"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendFireworkPacket(final Location loc, final FireworkEffect fe) {
        final Firework fw = (Firework)loc.getWorld().spawn(loc, (Class)Firework.class);
        final FireworkMeta data = fw.getFireworkMeta();
        data.clearEffects();
        data.addEffect(fe);
        data.setPower(0);
        fw.setFireworkMeta(data);
        this.detonateFirework(fw);
    }
    
    public void sendFireworkPacket(final Player player, final Location loc, final FireworkEffect fe) {
        final Firework fw = (Firework)loc.getWorld().spawn(loc, (Class)Firework.class);
        final FireworkMeta data = fw.getFireworkMeta();
        data.clearEffects();
        data.addEffect(fe);
        data.setPower(0);
        fw.setFireworkMeta(data);
        try {
            final Object dpacket = this.packetPlayOutEntityDestroy.newInstance();
            final Field a = this.packetPlayOutEntityDestroy.getDeclaredField("a");
            a.setAccessible(true);
            a.set(dpacket, new int[] { fw.getEntityId() });
            for (final Player pl : fw.getWorld().getPlayers()) {
                if (!pl.equals(player)) {
                    this.sendPacket(pl, dpacket);
                }
            }
            this.detonateFirework(fw);
        }
        catch (NoSuchFieldException | SecurityException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public void detonateFirework(final Firework fw) {
        try {
            final Object nms_firework = this.getFireworkHandle.invoke(fw, new Object[0]);
            final Field a = nms_firework.getClass().getDeclaredField("ticksFlown");
            a.setAccessible(true);
            a.set(nms_firework, 2);
            final Field b = nms_firework.getClass().getDeclaredField("expectedLifespan");
            b.setAccessible(true);
            b.set(nms_firework, 3);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendPacket(final Player player, final Object packet) {
        try {
            this.sendPacket.invoke(this.getPlayerConnection.get(this.getPlayerHandle.invoke(player, new Object[0])), packet);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    private Class<?> getMCClass(final String name) throws ClassNotFoundException {
        final String version = String.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]) + ".";
        final String className = "net.minecraft.server." + version + name;
        return Class.forName(className);
    }
    
    private Class<?> getCraftClass(final String name) throws ClassNotFoundException {
        final String version = String.valueOf(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]) + ".";
        final String className = "org.bukkit.craftbukkit." + version + name;
        return Class.forName(className);
    }
}
