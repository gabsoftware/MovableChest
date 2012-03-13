package com.gabsoftware.MoveableChest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class PistonListener implements Listener {
	
	Server server;
	
	public PistonListener( Server this_server )
	{
		this.server = this_server;
	}

	/*
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
	*/
	
	@EventHandler( priority = EventPriority.NORMAL )
	public void onPistonRetracted( BlockPistonRetractEvent event )
	{
		Block piston = null;
		BlockFace face = null;
		Block currentRelative = null;
		Block oldBlock = null;
		Block newBlock = null;
		boolean foundSomething = false;
		int currentRelativeIndex = 1;
		Location oldLoc = null;
		Location newLoc = null;
		Material oldMat = null;
		double x_offset = 0;
		double y_offset = 0;
		double z_offset = 0;
		BlockState oldState = null;
		BlockState newState = null;
		Chest oldChestState = null;
		Chest newChestState = null;
		Furnace oldFurnaceState = null;
		Furnace newFurnaceState = null;
		BrewingStand oldBTState = null;
		BrewingStand newBTState = null;
		Dispenser oldDispenserState = null;
		Dispenser newDispenserState = null;
		Inventory oldInv = null;
		ItemStack[] oldItems = null;
		Inventory newInv = null;
		MaterialData oldMatData;
		
		//this.server.broadcastMessage( "A piston was retracted!" );
		if( event.isSticky() )
		{
			piston = event.getBlock();
			face = getPistonFace( piston.getData() );
			foundSomething = false;
			currentRelativeIndex = 2;
			
			currentRelative = piston.getRelative(face, currentRelativeIndex);
			if( currentRelative != null && ! this.isNotAcceptedType( currentRelative.getType() ) )
			{
				//this.server.broadcastMessage( currentRelative.getType().toString() );
				if( currentRelative.getType().equals( Material.CHEST ) 
				||  currentRelative.getType().equals( Material.FURNACE )
				||  currentRelative.getType().equals( Material.DISPENSER )
				||  currentRelative.getType().equals( Material.BREWING_STAND ) )
				{
					foundSomething = true;
				}
			}
			
			if( foundSomething )
			{
				//this.server.broadcastMessage( "[retract] Chest detected in front of piston!" );
				
				
				
							
				
				
			
				
				//get the block
				oldBlock = piston.getRelative( face, currentRelativeIndex );
				
				//get the state of the old block
				oldState =  oldBlock.getState();
				
				//get the location of the old block
				oldLoc = oldState.getLocation();
				
				//get the material of the old block
				oldMat = oldState.getType();
				
				//this.server.broadcastMessage( "Old block Material: " + oldMat.toString() );
				
				x_offset = 0;
				y_offset = 0;
				z_offset = 0;
				
				switch( face )
				{
					case NORTH:
						x_offset = 1;
						break;
					case SOUTH:
						x_offset = -1;
						break;
					case EAST:
						z_offset = 1;
						break;
					case WEST:
						z_offset = -1;
						break;
					case UP:
						y_offset = -1;
						break;
					case DOWN:
						y_offset = 1;
						break;
				}
				
				//get the new block location
				newLoc = oldLoc.add(x_offset, y_offset, z_offset);
				
				//get the new block
				newBlock = newLoc.getBlock();
				
				//get the state of the new block
				newState = newBlock.getState();
				
				//if the old block was a chest, copy the inventory to the new chest
				if( oldMat.equals( Material.CHEST ) )
				{
					//cast the old block state to a Chest 
					oldChestState = (Chest) oldState.getBlock().getState();
					
					//get the inventory of the old chest
					oldInv = oldChestState.getInventory();
					
					//store a copy of the items
					oldItems = oldInv.getContents().clone();
					
					//get a copy of the data of the old chest
					oldMatData = oldChestState.getData();

					//clear the inventory of the old chest
					oldInv.clear();
					
					//replace the old chest block by AIR
					oldChestState.setType( Material.AIR );
					
					//update the old block
					if( ! oldChestState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update old Chest block!" );
					}
					
					//set the new block material
					newState.setType( Material.CHEST );
					newState.update( true );
					newChestState = (Chest) newState.getBlock().getState();
					
					//set the data of the new chest
					newChestState.setData( oldMatData );
					
					//get the inventory to the new chest
					newInv = newChestState.getInventory();
					
					//set the items of the new chest
					newInv.setContents( oldItems );
					
					//update the new block
					if( ! newChestState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update new Chest block!" );
					}
				}
				else if( oldMat.equals( Material.FURNACE ) )
				{
				
					//cast the old block state to a Chest 
					oldFurnaceState = (Furnace) oldState.getBlock().getState();
					
					//get the inventory of the old chest
					oldInv = oldFurnaceState.getInventory();
					
					//store a copy of the items
					oldItems = oldInv.getContents().clone();
					
					//get a copy of the data of the old chest
					oldMatData = oldFurnaceState.getData();

					//clear the inventory of the old chest
					oldInv.clear();
					
					//replace the old chest block by AIR
					oldFurnaceState.setType( Material.AIR );
					
					//update the old block
					if( ! oldFurnaceState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update old Furnace block!" );
					}
					
					//set the new block material
					newState.setType( Material.FURNACE );
					newState.update( true );
					newFurnaceState = (Furnace) newState.getBlock().getState();
					
					//set the data of the new chest
					newFurnaceState.setData( oldMatData );
					
					//get the inventory to the new chest
					newInv = newFurnaceState.getInventory();
					
					//set the items of the new chest
					newInv.setContents( oldItems );
					
					//update the new block
					if( ! newFurnaceState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update new Furnace block!" );
					}				
				}
				else if( oldMat.equals( Material.DISPENSER ) )
				{
				
					//cast the old block state to a Chest 
					oldDispenserState = (Dispenser) oldState.getBlock().getState();
					
					//get the inventory of the old chest
					oldInv = oldDispenserState.getInventory();
					
					//store a copy of the items
					oldItems = oldInv.getContents().clone();
					
					//get a copy of the data of the old chest
					oldMatData = oldDispenserState.getData();

					//clear the inventory of the old chest
					oldInv.clear();
					
					//replace the old chest block by AIR
					oldDispenserState.setType( Material.AIR );
					
					//update the old block
					if( ! oldDispenserState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update old Dispenser block!" );
					}
					
					//set the new block material
					newState.setType( Material.DISPENSER );
					newState.update( true );
					newDispenserState = (Dispenser) newState.getBlock().getState();
					
					//set the data of the new chest
					newDispenserState.setData( oldMatData );
					
					//get the inventory to the new chest
					newInv = newDispenserState.getInventory();
					
					//set the items of the new chest
					newInv.setContents( oldItems );
					
					//update the new block
					if( ! newDispenserState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update new Dispenser block!" );
					}				
				}
				else if( oldMat.equals( Material.BREWING_STAND ) )
				{
				
					//cast the old block state to a Chest 
					oldBTState = (BrewingStand) oldState.getBlock().getState();
					
					//get the inventory of the old chest
					oldInv = oldBTState.getInventory();
					
					//store a copy of the items
					oldItems = oldInv.getContents().clone();
					
					//get a copy of the data of the old chest
					oldMatData = oldBTState.getData();

					//clear the inventory of the old chest
					oldInv.clear();
					
					//replace the old chest block by AIR
					oldBTState.setType( Material.AIR );
					
					//update the old block
					if( ! oldBTState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update old Brewing_Stand block!" );
					}
					
					//set the new block material
					newState.setType( Material.BREWING_STAND );
					newState.update( true );
					newBTState = (BrewingStand) newState.getBlock().getState();
					
					//set the data of the new chest
					newBTState.setData( oldMatData );
					
					//get the inventory to the new chest
					newInv = newBTState.getInventory();
					
					//set the items of the new chest
					newInv.setContents( oldItems );
					
					//update the new block
					if( ! newBTState.update( true ) )
					{
						this.server.broadcastMessage( "MovableChest: Could not update new Brewing_Stand block!" );
					}				
				}
				
				
				
				
				
				
				
				
				
				
			}
			
		}
	}
	
	
	@EventHandler( priority = EventPriority.NORMAL )
	public void onBlockRedstone( BlockRedstoneEvent event )
	{		
		List<Block> pistons = this.getTriggeredPistons( event );
		
		BlockFace face = null;
		Block currentRelative = null;
		Block oldBlock = null;
		Block newBlock = null;
		boolean foundSomething = false;
		int currentRelativeIndex = 1;
		int i = 1;
		Location oldLoc = null;
		Location newLoc = null;
		Material oldMat = null;
		double x_offset = 0;
		double y_offset = 0;
		double z_offset = 0;
		BlockState oldState = null;
		BlockState newState = null;
		Chest oldChestState = null;
		Chest newChestState = null;
		Furnace oldFurnaceState = null;
		Furnace newFurnaceState = null;
		BrewingStand oldBTState = null;
		BrewingStand newBTState = null;
		Dispenser oldDispenserState = null;
		Dispenser newDispenserState = null;
		Inventory oldInv = null;
		ItemStack[] oldItems = null;
		Inventory newInv = null;
		MaterialData oldMatData;
		
		for( Block piston : pistons )
		{
			//this.server.broadcastMessage( "Detected that a piston power state changed" );
			
			face = getPistonFace( piston.getData() );

			foundSomething = false;
			currentRelativeIndex = 1;
				
			while( ! foundSomething && currentRelativeIndex <= 12 )
			{
				currentRelative = piston.getRelative(face, currentRelativeIndex);
				if( currentRelative != null && ! this.isNotAcceptedType( currentRelative.getType() ) )
				{
					//this.server.broadcastMessage( currentRelative.getType().toString() );
					if( currentRelative.getType().equals( Material.CHEST )
					||  currentRelative.getType().equals( Material.FURNACE )
					||  currentRelative.getType().equals( Material.DISPENSER )
					||  currentRelative.getType().equals( Material.BREWING_STAND ) )
					{
						foundSomething = true;
						break;
					}
					currentRelativeIndex ++;	
				}
				else
				{
					//forces exit
					break;
				}
			}
			
			if( foundSomething )
			{
				//this.server.broadcastMessage( "Chest detected in front of piston!" );
				

				//we have to move the chest 1 block away or towards the piston
				if( event.getNewCurrent() > 0 )
				{
					//move the chest 1 block away (and all the blocks in between)
								
					
					//loop through the block list behind the piston
					for( i = currentRelativeIndex; i >= 1; i-- )
					{
						//this.server.broadcastMessage( "New loop iteration | i = " + i );
						//get the block
						oldBlock = piston.getRelative(face, i);
						
						//get the state of the old block
						oldState =  oldBlock.getState();
						
						//get the location of the old block
						oldLoc = oldState.getLocation();
						
						//get the material of the old block
						oldMat = oldState.getType();
						
						//this.server.broadcastMessage( "Old block Material: " + oldMat.toString() );
						
						x_offset = 0;
						y_offset = 0;
						z_offset = 0;
						
						switch( face )
						{
							case NORTH:
								x_offset = -1;
								break;
							case SOUTH:
								x_offset = 1;
								break;
							case EAST:
								z_offset = -1;
								break;
							case WEST:
								z_offset = 1;
								break;
							case UP:
								y_offset = 1;
								break;
							case DOWN:
								y_offset = -1;
								break;
						}
						
						//get the new block location
						newLoc = oldLoc.add(x_offset, y_offset, z_offset);
						
						//get the new block
						newBlock = newLoc.getBlock();
						
						//get the state of the new block
						newState = newBlock.getState();
						
						//if the old block was a chest, copy the inventory to the new chest
						if( oldMat.equals( Material.CHEST ) )
						{
							//cast the old block state to a Chest 
							oldChestState = (Chest) oldState.getBlock().getState();
							
							//get the inventory of the old chest
							oldInv = oldChestState.getInventory();
							
							//store a copy of the items
							oldItems = oldInv.getContents().clone();
							
							//get a copy of the data of the old chest
							oldMatData = oldChestState.getData();

							//clear the inventory of the old chest
							oldInv.clear();
							
							//replace the old chest block by AIR
							oldChestState.setType( Material.AIR );
							
							//update the old block
							if( ! oldChestState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update old block!" );
							}
							
							//set the new block material
							newState.setType( Material.CHEST );
							newState.update( true );
							newChestState = (Chest) newState.getBlock().getState();
							
							//set the data of the new chest
							newChestState.setData( oldMatData );
							
							//get the inventory to the new chest
							newInv = newChestState.getInventory();
							
							//set the items of the new chest
							newInv.setContents( oldItems );
							
							//update the new block
							if( ! newChestState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update new block!" );
							}
						}
						else if( oldMat.equals( Material.FURNACE ) )
						{
						
							//cast the old block state to a Chest 
							oldFurnaceState = (Furnace) oldState.getBlock().getState();
							
							//get the inventory of the old chest
							oldInv = oldFurnaceState.getInventory();
							
							//store a copy of the items
							oldItems = oldInv.getContents().clone();
							
							//get a copy of the data of the old chest
							oldMatData = oldFurnaceState.getData();

							//clear the inventory of the old chest
							oldInv.clear();
							
							//replace the old chest block by AIR
							oldFurnaceState.setType( Material.AIR );
							
							//update the old block
							if( ! oldFurnaceState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update old Furnace block!" );
							}
							
							//set the new block material
							newState.setType( Material.FURNACE );
							newState.update( true );
							newFurnaceState = (Furnace) newState.getBlock().getState();
							
							//set the data of the new chest
							newFurnaceState.setData( oldMatData );
							
							//get the inventory to the new chest
							newInv = newFurnaceState.getInventory();
							
							//set the items of the new chest
							newInv.setContents( oldItems );
							
							//update the new block
							if( ! newFurnaceState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update new Furnace block!" );
							}						
						}
						else if( oldMat.equals( Material.DISPENSER ) )
						{
						
							//cast the old block state to a Chest 
							oldDispenserState = (Dispenser) oldState.getBlock().getState();
							
							//get the inventory of the old chest
							oldInv = oldDispenserState.getInventory();
							
							//store a copy of the items
							oldItems = oldInv.getContents().clone();
							
							//get a copy of the data of the old chest
							oldMatData = oldDispenserState.getData();

							//clear the inventory of the old chest
							oldInv.clear();
							
							//replace the old chest block by AIR
							oldDispenserState.setType( Material.AIR );
							
							//update the old block
							if( ! oldDispenserState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update old Dispenser block!" );
							}
							
							//set the new block material
							newState.setType( Material.DISPENSER );
							newState.update( true );
							newDispenserState = (Dispenser) newState.getBlock().getState();
							
							//set the data of the new chest
							newDispenserState.setData( oldMatData );
							
							//get the inventory to the new chest
							newInv = newDispenserState.getInventory();
							
							//set the items of the new chest
							newInv.setContents( oldItems );
							
							//update the new block
							if( ! newDispenserState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update new Dispenser block!" );
							}				
						}
						else if( oldMat.equals( Material.BREWING_STAND ) )
						{
						
							//cast the old block state to a Chest 
							oldBTState = (BrewingStand) oldState.getBlock().getState();
							
							//get the inventory of the old chest
							oldInv = oldBTState.getInventory();
							
							//store a copy of the items
							oldItems = oldInv.getContents().clone();
							
							//get a copy of the data of the old chest
							oldMatData = oldBTState.getData();

							//clear the inventory of the old chest
							oldInv.clear();
							
							//replace the old chest block by AIR
							oldBTState.setType( Material.AIR );
							
							//update the old block
							if( ! oldBTState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update old BrewingStand block!" );
							}
							
							//set the new block material
							newState.setType( Material.BREWING_STAND );
							newState.update( true );
							newBTState = (BrewingStand) newState.getBlock().getState();
							
							//set the data of the new chest
							newBTState.setData( oldMatData );
							
							//get the inventory to the new chest
							newInv = newBTState.getInventory();
							
							//set the items of the new chest
							newInv.setContents( oldItems );
							
							//update the new block
							if( ! newBTState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update new BrewingStand block!" );
							}				
						}
						else
						{
							//not a chest: we just set the old block to AIR
							oldState.setType( Material.AIR );
							
							//update the old block
							if( ! oldState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update old block!" );
							}
							
							//set the new block to the old block material
							newState.setType( oldMat );
							
							//update the old block
							if( ! newState.update( true ) )
							{
								this.server.broadcastMessage( "MovableChest: Could not update new block!" );
							}
						}
					}
				}
				/*else if( event.getNewCurrent() == 0 && piston.getType().equals( Material.PISTON_STICKY_BASE ) )
				{
					//move the chest 1 block towards the piston
					
					
					

				}*/
				
				
			}
			/*else
			{
				//this.server.broadcastMessage( "No chest were detected in front of piston." );
			}*/
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
