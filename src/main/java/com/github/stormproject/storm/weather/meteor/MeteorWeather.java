package com.github.stormproject.storm.weather.meteor;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.StormUtil;
import com.github.stormproject.storm.weather.StormWeather;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * A meteor weather object.
 */

public class MeteorWeather extends StormWeather {

    private final WorldVariables glob;

    /**
     * Creates a meteor weather object for given world.
     *
     * @param world The world this object will be handling
     */

    public MeteorWeather(String world) {
        super(world);
        glob = Storm.wConfigs.get(world);
        autoKillTicks = 1;
    }

    @Override
    public boolean canStart() {
        return glob.Weathers__Enabled_Natural__Disasters_Meteors;
    }

    /**
     * Called when a meteor is called in the handled world.
     */

    @Override
    public void start() {

        Chunk chunk = StormUtil.pickChunk(Bukkit.getWorld(world));

        if (chunk == null) {
            return;
        }
        Block b = chunk.getBlock(Storm.random.nextInt(16), 4, Storm.random.nextInt(16));
        spawnMeteorNaturallyAndRandomly(chunk.getWorld(), b.getX(), b.getZ());
    }

    @Override
    public void end() {

    }

    private void spawnMeteorNaturallyAndRandomly(World world, double x, double z) {
        net.minecraft.server.v1_6_R2.World meteoriteWorld = ((CraftWorld) bukkitWorld).getHandle();

        EntityMeteor meteor = new EntityMeteor(
                meteoriteWorld,
                Storm.random.nextInt(7) + 1,
                10,
                Storm.random.nextInt(5) + 5,
                Storm.random.nextInt(50) + 25,
                100,
                glob.Natural__Disasters_Meteor_Messages_On__Meteor__Crash,
                glob.Natural__Disasters_Meteor_Shockwave_Damage,
                80,
                glob.Natural__Disasters_Meteor_Messages_On__Damaged__By__Shockwave,
                glob.Natural__Disasters_Meteor_Meteor__Spawn,
                Storm.random.nextInt(6) + 3);

        meteor.spawn();

        meteor.setPosition(
                x,
                Storm.random.nextInt(100) + 156,
                z);
        meteor.yaw = (float) Storm.random.nextInt(360);
        meteor.pitch = -9;
        meteoriteWorld.addEntity(meteor, CreatureSpawnEvent.SpawnReason.DEFAULT);

        Fireball fireMeteor = (Fireball) meteor.getBukkitEntity();
        fireMeteor.setDirection(fireMeteor.getDirection().setY(-1));
    }
}