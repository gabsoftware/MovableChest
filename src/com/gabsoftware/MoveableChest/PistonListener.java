package com.gabsoftware.MoveableChest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
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
		
		BlockFace face = null;
		Block currentRelative = null;
		boolean foundChest = false;
		int currentRelativeIndex = 1;
		
		for( Block piston : pistons )
		{
			this.server.broadcastMessage( "Detected that a piston power state changed" );
			
			face = getPistonFace( piston.getData() );

			foundChest = false;
			currentRelativeIndex = 1;
				
			while( ! foundChest && currentRelativeIndex <= 12 )
			{
				currentRelative = piston.getRelative(face, currentRelativeIndex);
				if( currentRelative != null && ! this.isNotAcceptedType( currentRelative.getType() ) )
				{
					this.server.broadcastMessage( currentRelative.getType().toString() );
					if( currentRelative.getType().equals( Material.CHEST ) )
					{
						foundChest = true;
					}
					currentRelativeIndex ++;	
				}
				else
				{
					//forces exit
					break;
				}
			}
			
			if( foundChest )
			{
				this.server.broadcastMessage( "Chest detected in front of piston!" );
					
				//we have to move the chest 1 block away or towards the piston
				if( event.getNewCurrent() > 0 )
				{
					//move the chest 1 block away
					Location loc = currentRelative.getLocation();
					//World world = loc.getWorld();
					
					double x_offset = 0;
					double y_offset = 0;
					double z_offset = 0;
					
					switch( face )
					{
						case NORTH:
							y_offset = -1;
						case EAST:
							x_offset = 1;
						case SOUTH:
							y_offset = 1;
						case WEST:
							x_offset = -1;
						case UP:
							z_offset = 1;
						case DOWN:
							z_offset = -1;					
					}
					
					loc.setX( x_offset );
					loc.setY( y_offset );
					loc.setZ( z_offset );
					
					//world.save();
					
				}
				else
				{
					//move the chest 1 block towards
				}
				
			}
			else
			{
				this.server.broadcastMessage( "No chest were detected in front of piston." );
			}
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
				BlockFace.DOWN
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
		}
		return result;
	}
	
	public BlockFace getPistonFace(byte data)
	{
		BlockFace face = null;
		data = (byte) ( data & 0x7);
		switch( data )
		{
			case 0:
				face = BlockFace.DOWN;
				break;
			case 1:
				face = BlockFace.UP;
				break;
			case 2:
				face = BlockFace.EAST;
				break;
			case 3:
				face = BlockFace.WEST;
				break;
			case 4:
				face = BlockFace.NORTH;
				break;
			case 5:
				face = BlockFace.SOUTH;
				break;
			default:
				face = null;
				break;
		}
		return face;
	}
	
	public boolean isNotAcceptedType( Material type )
	{
		if( type.equals( Material.AIR )
		|| type.equals( Material.BEDROCK )
		|| type.equals( Material.NOTE_BLOCK )
		|| type.equals( Material.OBSIDIAN )
		|| type.equals( Material.REDSTONE_WIRE )
		|| type.equals( Material.REDSTONE_TORCH_OFF )
		|| type.equals( Material.REDSTONE_TORCH_ON )
		|| type.equals( Material.DIODE_BLOCK_OFF )
		|| type.equals( Material.DIODE_BLOCK_ON )
		|| type.equals( Material.RED_ROSE )
		|| type.equals( Material.YELLOW_FLOWER )
		|| type.equals( Material.RED_MUSHROOM )
		|| type.equals( Material.BROWN_MUSHROOM )
		|| type.equals( Material.SAPLING )
		|| type.equals( Material.SIGN )
		|| type.equals( Material.STONE_BUTTON )
		|| type.equals( Material.LEVER )
		|| type.equals( Material.LADDER )
		|| type.equals( Material.WOODEN_DOOR )
		|| type.equals( Material.IRON_DOOR_BLOCK )
		|| type.equals( Material.TORCH )
		|| type.equals( Material.WATER )
		|| type.equals( Material.LAVA )
		|| type.equals( Material.STATIONARY_LAVA )
		|| type.equals( Material.STATIONARY_WATER )
		|| type.equals( Material.FIRE )
		|| type.equals( Material.PISTON_MOVING_PIECE )
		|| type.equals( Material.PISTON_EXTENSION ) )
		{
			return true;
		}
		return false;
	}
	
}
