package com.github.StormTeam.Storm;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Fireball;

import com.github.StormTeam.Storm.Acid_Rain.AcidRain;
import static com.github.StormTeam.Storm.Acid_Rain.AcidRain.acidicWorlds;
;
import com.github.StormTeam.Storm.Acid_Rain.Events.AcidRainEvent;
import com.github.StormTeam.Storm.Acid_Rain.Listeners.AcidListener;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DissolverTask;
import static com.github.StormTeam.Storm.Blizzard.Blizzard.blizzardingWorlds;
import com.github.StormTeam.Storm.Blizzard.Events.BlizzardEvent;
import com.github.StormTeam.Storm.Blizzard.Listeners.BlizzardListeners;
import com.github.StormTeam.Storm.Blizzard.Tasks.PlayerTask;
import com.github.StormTeam.Storm.Meteors.Entities.EntityMeteor;
import com.github.StormTeam.Storm.Wildfire.Wildfire;



public class CommandExecutors {

    private Storm storm;

    public CommandExecutors(Storm storm) {
        this.storm = storm;
    }

    public void meteor(Location targetLoc, Location spawnLoc) {
        net.minecraft.server.WorldServer mcWorld = ((CraftWorld) (spawnLoc
                .getWorld())).getHandle();

        EntityMeteor mm = new EntityMeteor(
                mcWorld,
                15,
                15,
                15,
                60,
                100,
                Storm.wConfigs.get(mcWorld.getWorld().getName()).Natural__Disasters_Meteor_Message__On__Meteor__Crash,
                9,
                80,
                Storm.wConfigs.get(mcWorld.getWorld().getName()).Natural__Disasters_Meteor_Shockwave_Damage__Message,
                0,
                false,
                0);
        mm.spawn();

        Fireball meteor = (Fireball) mm.getBukkitEntity();
        meteor.teleport(spawnLoc);

        meteor.setDirection(targetLoc.toVector().subtract(spawnLoc.toVector()));
        meteor.setBounce(false);
        meteor.setIsIncendiary(true);
    }

    public void wildfire(Location targetLoc) {

        Block fire = targetLoc.getBlock().getRelative(BlockFace.UP);
        fire.setType(Material.FIRE);
        Wildfire.wildfireBlocks.get(targetLoc.getWorld()).add(fire);

    }

    public void acidRain(World world) {

        String name = world.getName();

        if (acidicWorlds.containsKey(name)
                && acidicWorlds.get(name)) {

            acidicWorlds.put(name, false);
            AcidListener.damagerMap.get(name).stop();
            AcidListener.dissolverMap.get(name).stop();

            world.setStorm(false);

            Storm.pm.callEvent(new AcidRainEvent(world, false));

        } else {

            DamagerTask dam = new DamagerTask(storm, world);
            AcidListener.damagerMap.put(name, dam);
            dam.run();

            DissolverTask dis = new DissolverTask(storm, world);
            AcidListener.dissolverMap.put(name,
                    new DissolverTask(storm, world));
            dis.run();

            AcidRain.acidicWorlds.put(name, true);
            Storm.util
                    .broadcast(Storm.wConfigs.get(name).Acid__Rain_Message__On__Acid__Rain__Start);

            world.setStorm(true);

            Storm.pm.callEvent(new AcidRainEvent(world, true));

        }

    }

    public void blizzard(World world) {

        String name = world.getName();

        if (blizzardingWorlds.containsKey(name)
                && blizzardingWorlds.get(name)) {

            BlizzardListeners.damagerMap.get(name).stop();

            blizzardingWorlds.put(name, false);

            world.setStorm(false);

            Storm.pm.callEvent(new BlizzardEvent(world, false));

        } else {

            PlayerTask dam = new PlayerTask(storm, world);
            BlizzardListeners.damagerMap.put(name, dam);
            dam.run();

            blizzardingWorlds.put(name, true);
            Storm.util
                    .broadcast(Storm.wConfigs.get(name).Blizzard_Message__On__Blizzard__Start);
            world.setStorm(true);

            Storm.pm.callEvent(new BlizzardEvent(world, false));
        }

    }
}