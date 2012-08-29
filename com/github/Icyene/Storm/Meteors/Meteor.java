package com.github.Icyene.Storm.Meteors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Fireball;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;
import com.github.Icyene.Storm.Meteors.Tasks.MeteorSpawnerTask;

public class Meteor {

    public static Storm storm;
    public static List<Fireball> activeMeteors = new ArrayList<Fireball>();
    
    public static void load(Storm sStorm) {
	storm = sStorm;

	if (Storm.config.Features_Meteor) {

	    try {
		Method a = net.minecraft.server.EntityTypes.class
			.getDeclaredMethod("a", Class.class, String.class,
				int.class);
		a.setAccessible(true);

		a.invoke(a, EntityMeteor.class, "Fireball", 12);

	    } catch (Exception e) {
		Storm.util.log(Level.SEVERE, "Failed to create meteor entity!");
	    }

	  new MeteorSpawnerTask(storm).run();
	    
	}
    }

}
