package code.BreakMC.devmode;

import java.lang.reflect.*;
import java.util.*;
import org.bukkit.*;

public final class How2Reflect
{
    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... parameterTypes) throws NoSuchMethodException {
        final Class[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        Constructor<?>[] constructors;
        for (int length = (constructors = clazz.getConstructors()).length, i = 0; i < length; ++i) {
            final Constructor<?> constructor = constructors[i];
            if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
                return constructor;
            }
        }
        throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter types");
    }
    
    public static Constructor<?> getConstructor(final String className, final PackageType packageType, final Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getConstructor(packageType.getClass(className), parameterTypes);
    }
    
    public static Object instantiateObject(final Class<?> clazz, final Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
    }
    
    public static Object instantiateObject(final String className, final PackageType packageType, final Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return instantiateObject(packageType.getClass(className), arguments);
    }
    
    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) throws NoSuchMethodException {
        final Class[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        Method[] methods;
        for (int length = (methods = clazz.getMethods()).length, i = 0; i < length; ++i) {
            final Method method = methods[i];
            if (method.getName().equals(methodName) && DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes)) {
                return method;
            }
        }
        throw new NoSuchMethodException("There is no such method in this class with the specified name and parameter types");
    }
    
    public static Method getMethod(final String className, final PackageType packageType, final String methodName, final Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getMethod(packageType.getClass(className), methodName, parameterTypes);
    }
    
    public static Object invokeMethod(final Object instance, final String methodName, final Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }
    
    public static Object invokeMethod(final Object instance, final Class<?> clazz, final String methodName, final Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }
    
    public static Object invokeMethod(final Object instance, final String className, final PackageType packageType, final String methodName, final Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        return invokeMethod(instance, packageType.getClass(className), methodName, arguments);
    }
    
    public static Field getField(final Class<?> clazz, final boolean declared, final String fieldName) throws NoSuchFieldException, SecurityException {
        final Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        return field;
    }
    
    public static Field getField(final String className, final PackageType packageType, final boolean declared, final String fieldName) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
        return getField(packageType.getClass(className), declared, fieldName);
    }
    
    public static Object getValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return getField(clazz, declared, fieldName).get(instance);
    }
    
    public static Object getValue(final Object instance, final String className, final PackageType packageType, final boolean declared, final String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        return getValue(instance, packageType.getClass(className), declared, fieldName);
    }
    
    public static Object getValue(final Object instance, final boolean declared, final String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return getValue(instance, instance.getClass(), declared, fieldName);
    }
    
    public static void setValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName, final Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        getField(clazz, declared, fieldName).set(instance, value);
    }
    
    public static void setValue(final Object instance, final String className, final PackageType packageType, final boolean declared, final String fieldName, final Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassNotFoundException {
        setValue(instance, packageType.getClass(className), declared, fieldName, value);
    }
    
    public static void setValue(final Object instance, final boolean declared, final String fieldName, final Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        setValue(instance, instance.getClass(), declared, fieldName, value);
    }
    
    public enum DataType
    {
        BYTE((Class<?>)Byte.TYPE, (Class<?>)Byte.class), 
        SHORT((Class<?>)Short.TYPE, (Class<?>)Short.class), 
        INTEGER((Class<?>)Integer.TYPE, (Class<?>)Integer.class), 
        LONG((Class<?>)Long.TYPE, (Class<?>)Long.class), 
        CHARACTER((Class<?>)Character.TYPE, (Class<?>)Character.class), 
        FLOAT((Class<?>)Float.TYPE, (Class<?>)Float.class), 
        DOUBLE((Class<?>)Double.TYPE, (Class<?>)Double.class), 
        BOOLEAN((Class<?>)Boolean.TYPE, (Class<?>)Boolean.class);
        
        private static final Map<Class<?>, DataType> CLASS_MAP;
        private final Class<?> primitive;
        private final Class<?> reference;
        
        static {
            CLASS_MAP = new HashMap<Class<?>, DataType>();
            DataType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final DataType type = values[i];
                DataType.CLASS_MAP.put(type.primitive, type);
                DataType.CLASS_MAP.put(type.reference, type);
            }
        }
        
        private DataType(final Class<?> primitive, final Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }
        
        public Class<?> getPrimitive() {
            return this.primitive;
        }
        
        public Class<?> getReference() {
            return this.reference;
        }
        
        public static DataType fromClass(final Class<?> clazz) {
            return DataType.CLASS_MAP.get(clazz);
        }
        
        public static Class<?> getPrimitive(final Class<?> clazz) {
            final DataType type = fromClass(clazz);
            return (type == null) ? clazz : type.getPrimitive();
        }
        
        public static Class<?> getReference(final Class<?> clazz) {
            final DataType type = fromClass(clazz);
            return (type == null) ? clazz : type.getReference();
        }
        
        public static Class<?>[] getPrimitive(final Class<?>[] classes) {
            final int length = (classes == null) ? 0 : classes.length;
            final Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getPrimitive(classes[index]);
            }
            return (Class<?>[])types;
        }
        
        public static Class<?>[] getReference(final Class<?>[] classes) {
            final int length = (classes == null) ? 0 : classes.length;
            final Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getReference(classes[index]);
            }
            return (Class<?>[])types;
        }
        
        public static Class<?>[] getPrimitive(final Object[] objects) {
            final int length = (objects == null) ? 0 : objects.length;
            final Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getPrimitive(objects[index].getClass());
            }
            return (Class<?>[])types;
        }
        
        public static Class<?>[] getReference(final Object[] objects) {
            final int length = (objects == null) ? 0 : objects.length;
            final Class[] types = new Class[length];
            for (int index = 0; index < length; ++index) {
                types[index] = getReference(objects[index].getClass());
            }
            return (Class<?>[])types;
        }
        
        public static boolean compare(final Class<?>[] primary, final Class<?>[] secondary) {
            if (primary == null || secondary == null || primary.length != secondary.length) {
                return false;
            }
            for (int index = 0; index < primary.length; ++index) {
                final Class<?> primaryClass = primary[index];
                final Class<?> secondaryClass = secondary[index];
                if (!primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public enum PackageType
    {
        MINECRAFT_SERVER("MINECRAFT_SERVER", 0, "net.minecraft.server." + getServerVersion()), 
        CRAFTBUKKIT("CRAFTBUKKIT", 1, "org.bukkit.craftbukkit." + getServerVersion()), 
        CRAFTBUKKIT_BLOCK("CRAFTBUKKIT_BLOCK", 2, PackageType.CRAFTBUKKIT, "block"), 
        CRAFTBUKKIT_CHUNKIO("CRAFTBUKKIT_CHUNKIO", 3, PackageType.CRAFTBUKKIT, "chunkio"), 
        CRAFTBUKKIT_COMMAND("CRAFTBUKKIT_COMMAND", 4, PackageType.CRAFTBUKKIT, "command"), 
        CRAFTBUKKIT_CONVERSATIONS("CRAFTBUKKIT_CONVERSATIONS", 5, PackageType.CRAFTBUKKIT, "conversations"), 
        CRAFTBUKKIT_ENCHANTMENS("CRAFTBUKKIT_ENCHANTMENS", 6, PackageType.CRAFTBUKKIT, "enchantments"), 
        CRAFTBUKKIT_ENTITY("CRAFTBUKKIT_ENTITY", 7, PackageType.CRAFTBUKKIT, "entity"), 
        CRAFTBUKKIT_EVENT("CRAFTBUKKIT_EVENT", 8, PackageType.CRAFTBUKKIT, "event"), 
        CRAFTBUKKIT_GENERATOR("CRAFTBUKKIT_GENERATOR", 9, PackageType.CRAFTBUKKIT, "generator"), 
        CRAFTBUKKIT_HELP("CRAFTBUKKIT_HELP", 10, PackageType.CRAFTBUKKIT, "help"), 
        CRAFTBUKKIT_INVENTORY("CRAFTBUKKIT_INVENTORY", 11, PackageType.CRAFTBUKKIT, "inventory"), 
        CRAFTBUKKIT_MAP("CRAFTBUKKIT_MAP", 12, PackageType.CRAFTBUKKIT, "map"), 
        CRAFTBUKKIT_METADATA("CRAFTBUKKIT_METADATA", 13, PackageType.CRAFTBUKKIT, "metadata"), 
        CRAFTBUKKIT_POTION("CRAFTBUKKIT_POTION", 14, PackageType.CRAFTBUKKIT, "potion"), 
        CRAFTBUKKIT_PROJECTILES("CRAFTBUKKIT_PROJECTILES", 15, PackageType.CRAFTBUKKIT, "projectiles"), 
        CRAFTBUKKIT_SCHEDULER("CRAFTBUKKIT_SCHEDULER", 16, PackageType.CRAFTBUKKIT, "scheduler"), 
        CRAFTBUKKIT_SCOREBOARD("CRAFTBUKKIT_SCOREBOARD", 17, PackageType.CRAFTBUKKIT, "scoreboard"), 
        CRAFTBUKKIT_UPDATER("CRAFTBUKKIT_UPDATER", 18, PackageType.CRAFTBUKKIT, "updater"), 
        CRAFTBUKKIT_UTIL("CRAFTBUKKIT_UTIL", 19, PackageType.CRAFTBUKKIT, "util");
        
        private final String path;
        
        private PackageType(final String s, final int n, final String path) {
            this.path = path;
        }
        
        private PackageType(final String s, final int n, final PackageType parent, final String path) {
            this(s, n, parent + "." + path);
        }
        
        public String getPath() {
            return this.path;
        }
        
        public Class<?> getClass(final String className) throws ClassNotFoundException {
            return Class.forName(this + "." + className);
        }
        
        @Override
        public String toString() {
            return this.path;
        }
        
        public static String getServerVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }
    }
    
    public enum PacketType
    {
        HANDSHAKING_IN_SET_PROTOCOL("HANDSHAKING_IN_SET_PROTOCOL", 0, "PacketHandshakingInSetProtocol"), 
        LOGIN_IN_ENCRYPTION_BEGIN("LOGIN_IN_ENCRYPTION_BEGIN", 1, "PacketLoginInEncryptionBegin"), 
        LOGIN_IN_START("LOGIN_IN_START", 2, "PacketLoginInStart"), 
        LOGIN_OUT_DISCONNECT("LOGIN_OUT_DISCONNECT", 3, "PacketLoginOutDisconnect"), 
        LOGIN_OUT_ENCRYPTION_BEGIN("LOGIN_OUT_ENCRYPTION_BEGIN", 4, "PacketLoginOutEncryptionBegin"), 
        LOGIN_OUT_SUCCESS("LOGIN_OUT_SUCCESS", 5, "PacketLoginOutSuccess"), 
        PLAY_IN_ABILITIES("PLAY_IN_ABILITIES", 6, "PacketPlayInAbilities"), 
        PLAY_IN_ARM_ANIMATION("PLAY_IN_ARM_ANIMATION", 7, "PacketPlayInArmAnimation"), 
        PLAY_IN_BLOCK_DIG("PLAY_IN_BLOCK_DIG", 8, "PacketPlayInBlockDig"), 
        PLAY_IN_BLOCK_PLACE("PLAY_IN_BLOCK_PLACE", 9, "PacketPlayInBlockPlace"), 
        PLAY_IN_CHAT("PLAY_IN_CHAT", 10, "PacketPlayInChat"), 
        PLAY_IN_CLIENT_COMMAND("PLAY_IN_CLIENT_COMMAND", 11, "PacketPlayInClientCommand"), 
        PLAY_IN_CLOSE_WINDOW("PLAY_IN_CLOSE_WINDOW", 12, "PacketPlayInCloseWindow"), 
        PLAY_IN_CUSTOM_PAYLOAD("PLAY_IN_CUSTOM_PAYLOAD", 13, "PacketPlayInCustomPayload"), 
        PLAY_IN_ENCHANT_ITEM("PLAY_IN_ENCHANT_ITEM", 14, "PacketPlayInEnchantItem"), 
        PLAY_IN_ENTITY_ACTION("PLAY_IN_ENTITY_ACTION", 15, "PacketPlayInEntityAction"), 
        PLAY_IN_FLYING("PLAY_IN_FLYING", 16, "PacketPlayInFlying"), 
        PLAY_IN_HELD_ITEM_SLOT("PLAY_IN_HELD_ITEM_SLOT", 17, "PacketPlayInHeldItemSlot"), 
        PLAY_IN_KEEP_ALIVE("PLAY_IN_KEEP_ALIVE", 18, "PacketPlayInKeepAlive"), 
        PLAY_IN_LOOK("PLAY_IN_LOOK", 19, "PacketPlayInLook"), 
        PLAY_IN_POSITION("PLAY_IN_POSITION", 20, "PacketPlayInPosition"), 
        PLAY_IN_POSITION_LOOK("PLAY_IN_POSITION_LOOK", 21, "PacketPlayInPositionLook"), 
        PLAY_IN_SET_CREATIVE_SLOT("PLAY_IN_SET_CREATIVE_SLOT", 22, "PacketPlayInSetCreativeSlot "), 
        PLAY_IN_SETTINGS("PLAY_IN_SETTINGS", 23, "PacketPlayInSettings"), 
        PLAY_IN_STEER_VEHICLE("PLAY_IN_STEER_VEHICLE", 24, "PacketPlayInSteerVehicle"), 
        PLAY_IN_TAB_COMPLETE("PLAY_IN_TAB_COMPLETE", 25, "PacketPlayInTabComplete"), 
        PLAY_IN_TRANSACTION("PLAY_IN_TRANSACTION", 26, "PacketPlayInTransaction"), 
        PLAY_IN_UPDATE_SIGN("PLAY_IN_UPDATE_SIGN", 27, "PacketPlayInUpdateSign"), 
        PLAY_IN_USE_ENTITY("PLAY_IN_USE_ENTITY", 28, "PacketPlayInUseEntity"), 
        PLAY_IN_WINDOW_CLICK("PLAY_IN_WINDOW_CLICK", 29, "PacketPlayInWindowClick"), 
        PLAY_OUT_ABILITIES("PLAY_OUT_ABILITIES", 30, "PacketPlayOutAbilities"), 
        PLAY_OUT_ANIMATION("PLAY_OUT_ANIMATION", 31, "PacketPlayOutAnimation"), 
        PLAY_OUT_ATTACH_ENTITY("PLAY_OUT_ATTACH_ENTITY", 32, "PacketPlayOutAttachEntity"), 
        PLAY_OUT_BED("PLAY_OUT_BED", 33, "PacketPlayOutBed"), 
        PLAY_OUT_BLOCK_ACTION("PLAY_OUT_BLOCK_ACTION", 34, "PacketPlayOutBlockAction"), 
        PLAY_OUT_BLOCK_BREAK_ANIMATION("PLAY_OUT_BLOCK_BREAK_ANIMATION", 35, "PacketPlayOutBlockBreakAnimation"), 
        PLAY_OUT_BLOCK_CHANGE("PLAY_OUT_BLOCK_CHANGE", 36, "PacketPlayOutBlockChange"), 
        PLAY_OUT_CHAT("PLAY_OUT_CHAT", 37, "PacketPlayOutChat"), 
        PLAY_OUT_CLOSE_WINDOW("PLAY_OUT_CLOSE_WINDOW", 38, "PacketPlayOutCloseWindow"), 
        PLAY_OUT_COLLECT("PLAY_OUT_COLLECT", 39, "PacketPlayOutCollect"), 
        PLAY_OUT_CRAFT_PROGRESS_BAR("PLAY_OUT_CRAFT_PROGRESS_BAR", 40, "PacketPlayOutCraftProgressBar"), 
        PLAY_OUT_CUSTOM_PAYLOAD("PLAY_OUT_CUSTOM_PAYLOAD", 41, "PacketPlayOutCustomPayload"), 
        PLAY_OUT_ENTITY("PLAY_OUT_ENTITY", 42, "PacketPlayOutEntity"), 
        PLAY_OUT_ENTITY_DESTROY("PLAY_OUT_ENTITY_DESTROY", 43, "PacketPlayOutEntityDestroy"), 
        PLAY_OUT_ENTITY_EFFECT("PLAY_OUT_ENTITY_EFFECT", 44, "PacketPlayOutEntityEffect"), 
        PLAY_OUT_ENTITY_EQUIPMENT("PLAY_OUT_ENTITY_EQUIPMENT", 45, "PacketPlayOutEntityEquipment"), 
        PLAY_OUT_ENTITY_HEAD_ROTATION("PLAY_OUT_ENTITY_HEAD_ROTATION", 46, "PacketPlayOutEntityHeadRotation"), 
        PLAY_OUT_ENTITY_LOOK("PLAY_OUT_ENTITY_LOOK", 47, "PacketPlayOutEntityLook"), 
        PLAY_OUT_ENTITY_METADATA("PLAY_OUT_ENTITY_METADATA", 48, "PacketPlayOutEntityMetadata"), 
        PLAY_OUT_ENTITY_STATUS("PLAY_OUT_ENTITY_STATUS", 49, "PacketPlayOutEntityStatus"), 
        PLAY_OUT_ENTITY_TELEPORT("PLAY_OUT_ENTITY_TELEPORT", 50, "PacketPlayOutEntityTeleport"), 
        PLAY_OUT_ENTITY_VELOCITY("PLAY_OUT_ENTITY_VELOCITY", 51, "PacketPlayOutEntityVelocity"), 
        PLAY_OUT_EXPERIENCE("PLAY_OUT_EXPERIENCE", 52, "PacketPlayOutExperience"), 
        PLAY_OUT_EXPLOSION("PLAY_OUT_EXPLOSION", 53, "PacketPlayOutExplosion"), 
        PLAY_OUT_GAME_STATE_CHANGE("PLAY_OUT_GAME_STATE_CHANGE", 54, "PacketPlayOutGameStateChange"), 
        PLAY_OUT_HELD_ITEM_SLOT("PLAY_OUT_HELD_ITEM_SLOT", 55, "PacketPlayOutHeldItemSlot"), 
        PLAY_OUT_KEEP_ALIVE("PLAY_OUT_KEEP_ALIVE", 56, "PacketPlayOutKeepAlive"), 
        PLAY_OUT_KICK_DISCONNECT("PLAY_OUT_KICK_DISCONNECT", 57, "PacketPlayOutKickDisconnect"), 
        PLAY_OUT_LOGIN("PLAY_OUT_LOGIN", 58, "PacketPlayOutLogin"), 
        PLAY_OUT_MAP("PLAY_OUT_MAP", 59, "PacketPlayOutMap"), 
        PLAY_OUT_MAP_CHUNK("PLAY_OUT_MAP_CHUNK", 60, "PacketPlayOutMapChunk"), 
        PLAY_OUT_MAP_CHUNK_BULK("PLAY_OUT_MAP_CHUNK_BULK", 61, "PacketPlayOutMapChunkBulk"), 
        PLAY_OUT_MULTI_BLOCK_CHANGE("PLAY_OUT_MULTI_BLOCK_CHANGE", 62, "PacketPlayOutMultiBlockChange"), 
        PLAY_OUT_NAMED_ENTITY_SPAWN("PLAY_OUT_NAMED_ENTITY_SPAWN", 63, "PacketPlayOutNamedEntitySpawn"), 
        PLAY_OUT_NAMED_SOUND_EFFECT("PLAY_OUT_NAMED_SOUND_EFFECT", 64, "PacketPlayOutNamedSoundEffect"), 
        PLAY_OUT_OPEN_SIGN_EDITOR("PLAY_OUT_OPEN_SIGN_EDITOR", 65, "PacketPlayOutOpenSignEditor"), 
        PLAY_OUT_OPEN_WINDOW("PLAY_OUT_OPEN_WINDOW", 66, "PacketPlayOutOpenWindow"), 
        PLAY_OUT_PLAYER_INFO("PLAY_OUT_PLAYER_INFO", 67, "PacketPlayOutPlayerInfo"), 
        PLAY_OUT_POSITION("PLAY_OUT_POSITION", 68, "PacketPlayOutPosition"), 
        PLAY_OUT_REL_ENTITY_MOVE("PLAY_OUT_REL_ENTITY_MOVE", 69, "PacketPlayOutRelEntityMove"), 
        PLAY_OUT_REL_ENTITY_MOVE_LOOK("PLAY_OUT_REL_ENTITY_MOVE_LOOK", 70, "PacketPlayOutRelEntityMoveLook"), 
        PLAY_OUT_REMOVE_ENTITY_EFFECT("PLAY_OUT_REMOVE_ENTITY_EFFECT", 71, "PacketPlayOutRemoveEntityEffect"), 
        PLAY_OUT_RESPAWN("PLAY_OUT_RESPAWN", 72, "PacketPlayOutRespawn"), 
        PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE("PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE", 73, "PacketPlayOutScoreboardDisplayObjective"), 
        PLAY_OUT_SCOREBOARD_OBJECTIVE("PLAY_OUT_SCOREBOARD_OBJECTIVE", 74, "PacketPlayOutScoreboardObjective"), 
        PLAY_OUT_SCOREBOARD_SCORE("PLAY_OUT_SCOREBOARD_SCORE", 75, "PacketPlayOutScoreboardScore"), 
        PLAY_OUT_SCOREBOARD_TEAM("PLAY_OUT_SCOREBOARD_TEAM", 76, "PacketPlayOutScoreboardTeam"), 
        PLAY_OUT_SET_SLOT("PLAY_OUT_SET_SLOT", 77, "PacketPlayOutSetSlot"), 
        PLAY_OUT_SPAWN_ENTITY("PLAY_OUT_SPAWN_ENTITY", 78, "PacketPlayOutSpawnEntity"), 
        PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB("PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB", 79, "PacketPlayOutSpawnEntityExperienceOrb"), 
        PLAY_OUT_SPAWN_ENTITY_LIVING("PLAY_OUT_SPAWN_ENTITY_LIVING", 80, "PacketPlayOutSpawnEntityLiving"), 
        PLAY_OUT_SPAWN_ENTITY_PAINTING("PLAY_OUT_SPAWN_ENTITY_PAINTING", 81, "PacketPlayOutSpawnEntityPainting"), 
        PLAY_OUT_SPAWN_ENTITY_WEATHER("PLAY_OUT_SPAWN_ENTITY_WEATHER", 82, "PacketPlayOutSpawnEntityWeather"), 
        PLAY_OUT_SPAWN_POSITION("PLAY_OUT_SPAWN_POSITION", 83, "PacketPlayOutSpawnPosition"), 
        PLAY_OUT_STATISTIC("PLAY_OUT_STATISTIC", 84, "PacketPlayOutStatistic"), 
        PLAY_OUT_TAB_COMPLETE("PLAY_OUT_TAB_COMPLETE", 85, "PacketPlayOutTabComplete"), 
        PLAY_OUT_TILE_ENTITY_DATA("PLAY_OUT_TILE_ENTITY_DATA", 86, "PacketPlayOutTileEntityData"), 
        PLAY_OUT_TRANSACTION("PLAY_OUT_TRANSACTION", 87, "PacketPlayOutTransaction"), 
        PLAY_OUT_UPDATE_ATTRIBUTES("PLAY_OUT_UPDATE_ATTRIBUTES", 88, "PacketPlayOutUpdateAttributes"), 
        PLAY_OUT_UPDATE_HEALTH("PLAY_OUT_UPDATE_HEALTH", 89, "PacketPlayOutUpdateHealth"), 
        PLAY_OUT_UPDATE_SIGN("PLAY_OUT_UPDATE_SIGN", 90, "PacketPlayOutUpdateSign"), 
        PLAY_OUT_UPDATE_TIME("PLAY_OUT_UPDATE_TIME", 91, "PacketPlayOutUpdateTime"), 
        PLAY_OUT_WINDOW_ITEMS("PLAY_OUT_WINDOW_ITEMS", 92, "PacketPlayOutWindowItems"), 
        PLAY_OUT_WORLD_EVENT("PLAY_OUT_WORLD_EVENT", 93, "PacketPlayOutWorldEvent"), 
        PLAY_OUT_WORLD_PARTICLES("PLAY_OUT_WORLD_PARTICLES", 94, "PacketPlayOutWorldParticles"), 
        STATUS_IN_PING("STATUS_IN_PING", 95, "PacketStatusInPing"), 
        STATUS_IN_START("STATUS_IN_START", 96, "PacketStatusInStart"), 
        STATUS_OUT_PONG("STATUS_OUT_PONG", 97, "PacketStatusOutPong"), 
        STATUS_OUT_SERVER_INFO("STATUS_OUT_SERVER_INFO", 98, "PacketStatusOutServerInfo");
        
        private static final Map<String, PacketType> NAME_MAP;
        private final String name;
        private Class<?> packet;
        
        static {
            NAME_MAP = new HashMap<String, PacketType>();
            PacketType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final PacketType type = values[i];
                PacketType.NAME_MAP.put(type.name, type);
            }
        }
        
        private PacketType(final String s, final int n, final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
        public Class<?> getPacket() throws ClassNotFoundException {
            return (this.packet == null) ? (this.packet = PackageType.MINECRAFT_SERVER.getClass(this.name)) : this.packet;
        }
    }
}
