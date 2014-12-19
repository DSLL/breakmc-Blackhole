package code.BreakMC.devmode;

import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;
import java.lang.reflect.*;

public enum ParticleEffect
{
    HUGE_EXPLOSION("HUGE_EXPLOSION", 0, "hugeexplosion"), 
    LARGE_EXPLODE("LARGE_EXPLODE", 1, "largeexplode"), 
    FIREWORKS_SPARK("FIREWORKS_SPARK", 2, "fireworksSpark"), 
    BUBBLE("BUBBLE", 3, "bubble", true), 
    SUSPEND("SUSPEND", 4, "suspend", true), 
    DEPTH_SUSPEND("DEPTH_SUSPEND", 5, "depthSuspend"), 
    TOWN_AURA("TOWN_AURA", 6, "townaura"), 
    CRIT("CRIT", 7, "crit"), 
    MAGIC_CRIT("MAGIC_CRIT", 8, "magicCrit"), 
    SMOKE("SMOKE", 9, "smoke"), 
    MOB_SPELL("MOB_SPELL", 10, "mobSpell"), 
    MOB_SPELL_AMBIENT("MOB_SPELL_AMBIENT", 11, "mobSpellAmbient"), 
    SPELL("SPELL", 12, "spell"), 
    INSTANT_SPELL("INSTANT_SPELL", 13, "instantSpell"), 
    WITCH_MAGIC("WITCH_MAGIC", 14, "witchMagic"), 
    NOTE("NOTE", 15, "note"), 
    PORTAL("PORTAL", 16, "portal"), 
    ENCHANTMENT_TABLE("ENCHANTMENT_TABLE", 17, "enchantmenttable"), 
    EXPLODE("EXPLODE", 18, "explode"), 
    FLAME("FLAME", 19, "flame"), 
    LAVA("LAVA", 20, "lava"), 
    FOOTSTEP("FOOTSTEP", 21, "footstep"), 
    SPLASH("SPLASH", 22, "splash"), 
    WAKE("WAKE", 23, "wake"), 
    LARGE_SMOKE("LARGE_SMOKE", 24, "largesmoke"), 
    CLOUD("CLOUD", 25, "cloud"), 
    RED_DUST("RED_DUST", 26, "reddust"), 
    SNOWBALL_POOF("SNOWBALL_POOF", 27, "snowballpoof"), 
    DRIP_WATER("DRIP_WATER", 28, "dripWater"), 
    DRIP_LAVA("DRIP_LAVA", 29, "dripLava"), 
    SNOW_SHOVEL("SNOW_SHOVEL", 30, "snowshovel"), 
    SLIME("SLIME", 31, "slime"), 
    HEART("HEART", 32, "heart"), 
    ANGRY_VILLAGER("ANGRY_VILLAGER", 33, "angryVillager"), 
    HAPPY_VILLAGER("HAPPY_VILLAGER", 34, "happyVillager");
    
    private static final Map<String, ParticleEffect> NAME_MAP;
    private final String name;
    private final boolean requiresWater;
    
    static {
        NAME_MAP = new HashMap<String, ParticleEffect>();
        ParticleEffect[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final ParticleEffect effect = values[i];
            ParticleEffect.NAME_MAP.put(effect.name, effect);
        }
    }
    
    private ParticleEffect(final String s, final int n, final String name, final boolean requiresWater) {
        this.name = name;
        this.requiresWater = requiresWater;
    }
    
    private ParticleEffect(final String s, final int n, final String name) {
        this(s, n, name, false);
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean getRequiresWater() {
        return this.requiresWater;
    }
    
    public static ParticleEffect fromName(final String name) {
        for (final Map.Entry<String, ParticleEffect> entry : ParticleEffect.NAME_MAP.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }
    
    private static boolean isWater(final Location location) {
        final Material material = location.getBlock().getType();
        return material == Material.WATER || material == Material.STATIONARY_WATER;
    }
    
    private static boolean isBlock(final int id) {
        final Material material = Material.getMaterial(id);
        return material != null && material.isBlock();
    }
    
    public void display(final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, final double range) throws IllegalArgumentException {
        if (this.requiresWater && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticleEffectPacket(this.name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
    }
    
    public void display(final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, final List<Player> players) throws IllegalArgumentException {
        if (this.requiresWater && !isWater(center)) {
            throw new IllegalArgumentException("There is no water at the center location");
        }
        new ParticleEffectPacket(this.name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
    }
    
    public static void displayIconCrack(final int id, final byte data, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, final double range) {
        new ParticleEffectPacket("iconcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
    }
    
    public static void displayIconCrack(final int id, final byte data, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, final List<Player> players) {
        new ParticleEffectPacket("iconcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
    }
    
    public static void displayBlockCrack(final int id, final byte data, final float offsetX, final float offsetY, final float offsetZ, final int amount, final Location center, final double range) throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, 0.0f, amount).sendTo(center, range);
    }
    
    public static void displayBlockCrack(final int id, final byte data, final float offsetX, final float offsetY, final float offsetZ, final int amount, final Location center, final List<Player> players) throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, 0.0f, amount).sendTo(center, players);
    }
    
    public static void displayBlockDust(final int id, final byte data, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, final double range) throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockdust_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
    }
    
    public static void displayBlockDust(final int id, final byte data, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, final List<Player> players) throws IllegalArgumentException {
        if (!isBlock(id)) {
            throw new IllegalArgumentException("Invalid block id");
        }
        new ParticleEffectPacket("blockdust_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
    }
    
    public static List<Player> getOnlinePlayers() {
        List<Player> onlinePlayers = new ArrayList<Player>();
        try {
            final Method onlinePlayersMethod = Bukkit.class.getMethod("getOnlinePlayers", (Class<?>[])new Class[0]);
            if (onlinePlayersMethod.getReturnType().equals(Collection.class)) {
                final Collection<Player> playerCollection = (Collection<Player>)onlinePlayersMethod.invoke(null, new Object[0]);
                if (playerCollection instanceof List) {
                    onlinePlayers = (List<Player>)(List)playerCollection;
                }
                else {
                    onlinePlayers = new ArrayList<Player>(playerCollection);
                }
            }
            else {
                onlinePlayers = Arrays.asList((Player[])onlinePlayersMethod.invoke(null, new Object[0]));
            }
        }
        catch (NoSuchMethodException) {}
        catch (InvocationTargetException) {}
        catch (IllegalAccessException ex) {}
        return onlinePlayers;
    }
    
    public static final class ParticleEffectPacket
    {
        private static Constructor<?> packetConstructor;
        private static Method getHandle;
        private static Field playerConnection;
        private static Method sendPacket;
        private static boolean initialized;
        private final String name;
        private final float offsetX;
        private final float offsetY;
        private final float offsetZ;
        private final float speed;
        private final int amount;
        private Object packet;
        
        public ParticleEffectPacket(final String name, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount) throws IllegalArgumentException {
            super();
            initialize();
            if (speed < 0.0f) {
                throw new IllegalArgumentException("The speed is lower than 0");
            }
            if (amount < 1) {
                throw new IllegalArgumentException("The amount is lower than 1");
            }
            this.name = name;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.speed = speed;
            this.amount = amount;
        }
        
        public static void initialize() throws VersionIncompatibleException {
            if (ParticleEffectPacket.initialized) {
                return;
            }
            try {
                final int version = Integer.parseInt(Character.toString(How2Reflect.PackageType.getServerVersion().charAt(3)));
                final Class<?> packetClass = How2Reflect.PackageType.MINECRAFT_SERVER.getClass((version < 7) ? "Packet63WorldParticles" : How2Reflect.PacketType.PLAY_OUT_WORLD_PARTICLES.getName());
                ParticleEffectPacket.packetConstructor = How2Reflect.getConstructor(packetClass, (Class<?>[])new Class[0]);
                ParticleEffectPacket.getHandle = How2Reflect.getMethod("CraftPlayer", How2Reflect.PackageType.CRAFTBUKKIT_ENTITY, "getHandle", (Class<?>[])new Class[0]);
                ParticleEffectPacket.playerConnection = How2Reflect.getField("EntityPlayer", How2Reflect.PackageType.MINECRAFT_SERVER, false, "playerConnection");
                ParticleEffectPacket.sendPacket = How2Reflect.getMethod(ParticleEffectPacket.playerConnection.getType(), "sendPacket", How2Reflect.PackageType.MINECRAFT_SERVER.getClass("Packet"));
            }
            catch (Exception exception) {
                throw new VersionIncompatibleException("Your current bukkit version seems to be incompatible with this library", exception);
            }
            ParticleEffectPacket.initialized = true;
        }
        
        public static boolean isInitialized() {
            return ParticleEffectPacket.initialized;
        }
        
        public void sendTo(final Location center, final Player player) throws PacketInstantiationException, PacketSendingException {
            if (this.packet == null) {
                try {
                    How2Reflect.setValue(this.packet = ParticleEffectPacket.packetConstructor.newInstance(new Object[0]), true, "a", this.name);
                    How2Reflect.setValue(this.packet, true, "b", (float)center.getX());
                    How2Reflect.setValue(this.packet, true, "c", (float)center.getY());
                    How2Reflect.setValue(this.packet, true, "d", (float)center.getZ());
                    How2Reflect.setValue(this.packet, true, "e", this.offsetX);
                    How2Reflect.setValue(this.packet, true, "f", this.offsetY);
                    How2Reflect.setValue(this.packet, true, "g", this.offsetZ);
                    How2Reflect.setValue(this.packet, true, "h", this.speed);
                    How2Reflect.setValue(this.packet, true, "i", this.amount);
                }
                catch (Exception exception) {
                    throw new PacketInstantiationException("Packet instantiation failed", exception);
                }
            }
            try {
                ParticleEffectPacket.sendPacket.invoke(ParticleEffectPacket.playerConnection.get(ParticleEffectPacket.getHandle.invoke(player, new Object[0])), this.packet);
            }
            catch (Exception exception) {
                throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
            }
        }
        
        public void sendTo(final Location center, final List<Player> players) throws IllegalArgumentException {
            if (players.isEmpty()) {
                throw new IllegalArgumentException("The player list is empty");
            }
            for (final Player player : players) {
                this.sendTo(center, player);
            }
        }
        
        public void sendTo(final Location center, final double range) throws IllegalArgumentException {
            if (range < 1.0) {
                throw new IllegalArgumentException("The range is lower than 1");
            }
            final String worldName = center.getWorld().getName();
            final double squared = range * range;
            for (final Player player : ParticleEffect.getOnlinePlayers()) {
                if (player.getWorld().getName().equals(worldName)) {
                    if (player.getLocation().distanceSquared(center) > squared) {
                        continue;
                    }
                    this.sendTo(center, player);
                }
            }
        }
        
        private static final class VersionIncompatibleException extends RuntimeException
        {
            private static final long serialVersionUID = 3203085387160737484L;
            
            public VersionIncompatibleException(final String message, final Throwable cause) {
                super(message, cause);
            }
        }
        
        private static final class PacketInstantiationException extends RuntimeException
        {
            private static final long serialVersionUID = 3203085387160737484L;
            
            public PacketInstantiationException(final String message, final Throwable cause) {
                super(message, cause);
            }
        }
        
        private static final class PacketSendingException extends RuntimeException
        {
            private static final long serialVersionUID = 3203085387160737484L;
            
            public PacketSendingException(final String message, final Throwable cause) {
                super(message, cause);
            }
        }
    }
}
