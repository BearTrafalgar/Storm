package com.github.Icyene.Storm.Blizzard.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Blizzard.Blizzard;

public class BlizzardTask {

    private int id;
    private World affectedWorld;
    private Storm storm;

    public BlizzardTask(Storm storm, World affectedWorld) {
	this.storm = storm;
	this.affectedWorld = affectedWorld;
    }

    public void run() {

	id = Bukkit.getScheduler()
		.scheduleSyncRepeatingTask(
			storm,
			new Runnable()
			{
			    @Override
			    public void run()
			    {

				for (Player damagee : affectedWorld
					.getPlayers())
				{
				    if (!damagee.getGameMode().equals(
					    GameMode.CREATIVE)
				    )
				    {

					if (!Blizzard.snowyBiomes
						.contains(damagee.getLocation()
							.getBlock().getBiome())) {
					    return;
					}

					damagee.addPotionEffect(
						new PotionEffect(
							PotionEffectType.BLINDNESS,
							Storm.config.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks + 60,
							Storm.config.Blizzard_Damager_Blindness__Amplitude),
						true);

					final Location loc = damagee
						.getLocation();

					final World world = damagee.getWorld();

					for (int y = 1; y > -2; y--)
					{
					    for (int x = 1; x > -2; x--)
					    {
						for (int z = 1; z > -2; z--)
						{
						    Block scan = world.getBlockAt(
							    (int) loc.getX()
								    + x,
							    (int) loc.getY()
								    + y,
							    (int) loc.getZ()
								    + z);
						    if (Storm.config.Blizzard_Damager_Heating__Blocks
							    .contains(scan
								    .getTypeId())) {
							return; // Don't damage
								// if they are
								// near hot
								// blocks
						    }
						}
					    }
					}

					damagee.damage(Storm.config.Blizzard_Player_Damage__From__Exposure * 2);
					Storm.util
						.message(
							damagee,
							Storm.config.Blizzard_Damager_Message__On__Player__Damaged__Cold);

				    }
				}
			    }

			},
			Storm.config.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks,
			Storm.config.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks);

    }

    public void stop() {
	Bukkit.getScheduler().cancelTask(id);
    }

}
