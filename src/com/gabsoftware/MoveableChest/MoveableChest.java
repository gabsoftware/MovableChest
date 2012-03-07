package com.gabsoftware.MoveableChest;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MoveableChest extends JavaPlugin
{
	Logger log;

	public void onEnable()
	{
		log = this.getLogger();
		log.info( "MoveableChest has been enabled!" );
		getServer().getPluginManager().registerEvents( new PistonListener( getServer() ), this);
		log.info( "PistonListener registered." );
	}
 
	public void onDisable()
	{
		log.info( "MoveableChest has been disabled." );
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		Player player = null;
		if( sender instanceof Player)
		{
			player = (Player) sender;
		}
		
		if( cmd.getName().equalsIgnoreCase( "mc" ) )
		{ // If the player typed /mc then do the following...
			if (player == null)
			{
				sender.sendMessage( "This command can only be run by a player!" );
			}
			else
			{
				log.info( "MoveableChest is active!" );
			}
			return true;
		}
		//If this has happened the function will break and return true.
		//If this hasn't happened the a value of false will be returned.
		return false; 
	}

}
