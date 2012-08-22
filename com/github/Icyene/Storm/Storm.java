/*
 * Storm
 * Copyright (C) 2012 Icyene
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.Icyene.Storm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Configuration.ReflectConfiguration;
import com.github.Icyene.Storm.Lightning.Lightning;
import com.github.Icyene.Storm.Meteors.Meteor;
import com.github.Icyene.Storm.Rain.Acid.AcidRain;
import com.github.Icyene.Storm.Snow.Snow;
import com.github.Icyene.Storm.Wildfire.Wildfire;

public class Storm extends JavaPlugin implements Listener
{

    /**
     * @author Icyene, hammale
     * 
     *         WORKING:
     * 
     *         - Acid Rain - Meteors - Multiworld - Configuration
     * 
     *         NOT WORKING:
     * 
     *         - Lightning - Snow Under Trees: Fixed as of dev build #2292
     * 
     *         TODO:
     * 
     *         - Puddles - Piling Snow - Hail -Damage players in range when
     *         meteor falls
     * 
     * @specification
     * 
     *                Puddles will be formed with a modified acid-rain based
     *                code, and their locations stored in a sqlite database, to
     *                be dried (deleted) when the world stops raining
     * 
     * 
     * @see That is all that will be included in V0.1, with perhaps Earthquakes.
     * 
     * @SNOWRULES Do not pile if any block in any direction is air, or has a
     *            data difference of greater than 1,.
     * 
     */

    // Dear future me. I cannot even begin to tell you how bad I feel for you.

    public final Logger log = Logger.getLogger("Minecraft");
    static final String prefix = "[Storm] ";
    public static boolean debug = true;

    public static List<String> stats = new ArrayList<String>();

    @Override
    public void onEnable()
    {
	ReflectConfiguration.load(this, GlobalVariables.class, "Storm.");

	// Stats

	try {
	    Metrics metrics = new Metrics(this);
	    for (int i = 0; i < stats.size(); i++) {
		final String name = stats.get(i);
		metrics.addCustomData(new Metrics.Plotter() {
		    @Override
		    public String getColumnName() {
			return name;
		    }

		    @Override
		    public int getValue() {
			return 1;
		    }
		});
	    }
	    metrics.start();
	} catch (IOException e) {

	}

	try {
	    if (!StormUtil.verifyVersion(this)) {
		StormUtil.log(Level.WARNING,
			"--------------------------------------------");
		StormUtil.log(Level.WARNING,
			"Your configuration is outdated, so some  ");
		StormUtil.log(Level.WARNING,
			"of Storm's features may not work correctly! ");
		StormUtil.log(Level.WARNING,
			"Delete it, or risk the consequences!      ");
		StormUtil.log(Level.WARNING, " ");
		StormUtil.log(Level.WARNING, "Thanks for using Storm!");
		StormUtil.log(Level.WARNING,
			"--------------------------------------------");
	    }

	    Snow.load(this);
	    AcidRain.load(this);
	    Lightning.load(this);
	    Wildfire.load(this);
	    Blizzard.load(this);
	    Meteor.load(this);
	    // this.getServer().getPluginManager().registerEvents(this, this);

	} catch (Exception e) {

	    e.printStackTrace();
	    crashDisable("Failed to initialize subplugins.");

	}
    }

    @Override
    public void onDisable() {
	Blizzard.unload();
    }

    public void crashDisable(String crash)
    {
	StormUtil.log(Level.SEVERE, prefix + crash + " Storm disabled.");
	this.setEnabled(false);
    }

    @EventHandler
    public void hitThatPlayer(PlayerInteractEvent e) {

    }

}