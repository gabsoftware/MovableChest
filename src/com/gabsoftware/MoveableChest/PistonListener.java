package com.gabsoftware.MoveableChest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

public class PistonListener implements Listener {
	
	Server server;
	
	public PistonListener( Server this_server )
	{
		this.server = this_server;
	}

	@EventHandler( priority = EventPriority.NORMAL )
	public void onPistonExtended( BlockPistonExtendEvent event )
	{
		this.server.broadcastMessage( "A piston was extended!" );
	        
		List<Block> blocks = event.getBlocks();
		for( Block block : blocks )
		{
			this.server.broadcastMessage( "A block was pushed!" );    
	    }		
	}
	
	@EventHandler( priority = EventPriority.NORMAL )
	public void onPistonRetracted( BlockPistonRetractEvent event )
	{		
		this.server.broadcastMessage( "A piston was retracted!" );	
	}
	
	
	@EventHandler( priority = EventPriority.NORMAL )
	public void onBlockRedstone( BlockRedstoneEvent event )
	{		
		List<Block> pistons = this.getTriggeredPistons( event );
		
		for( Block piston : pistons )
		{			
			this.server.broadcastMessage( "Detected that a piston power state changed" );
			
		}
		
	}
	
	
	public List<Block> getTriggeredPistons( BlockRedstoneEvent event )
	{
		List<Block> result = new ArrayList<Block>();
		if( ( event.getNewCurrent() > 0 && event.getOldCurrent() > 0 )
		||
			( event.getNewCurrent() == 0 && event.getOldCurrent() == 0 )
		)
		{
			//When power state didn't really change, 
			return result;
		}
		//TODO: Implement all types of possible triggers
		BlockFace faces[] = {
				BlockFace.NORTH,
				BlockFace.EAST,
				BlockFace.SOUTH,
				BlockFace.WEST,
				BlockFace.UP,
				BlockFace.SELF,
				BlockFace.DOWN,
		};
		for( BlockFace face : faces )
		{
			Block piston = event.getBlock().getRelative(face);
			
			if( piston.getType().equals( Material.PISTON_BASE )
			||
				piston.getType().equals( Material.PISTON_STICKY_BASE ) )
			{
				result.add( piston );
			}
			if( piston.getType().toString().contains( "DIODE" )
			&&
				(
					piston.getRelative( face ).getType().equals( Material.PISTON_BASE )
						||
					piston.getRelative( face ).getType().equals( Material.PISTON_STICKY_BASE )
				)
			)
			{
				result.add( piston.getRelative( face ) );
			}
		}
		return result;
	}	
}
