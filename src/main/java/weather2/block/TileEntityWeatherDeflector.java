package weather2.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import weather2.ClientTickHandler;
import weather2.ServerTickHandler;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;

public class TileEntityWeatherDeflector extends TileEntity
{
	public int deflectorRadius = 150;

    protected AxisAlignedBB aabb;

    @Override
    public void validate() {
    	super.validate();
    	aabb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 2, yCoord + 2, zCoord + 2);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
    	return aabb;
    }

    public void updateEntity()
    {
    	
    	if (!worldObj.isRemote) {
    		
    		if (worldObj.getTotalWorldTime() % 100 == 0) {
    			WeatherManagerServer wm = ServerTickHandler.lookupDimToWeatherMan.get(worldObj.provider.dimensionId);
    			if (wm != null) {
		    		//StormObject lastTickStormObject = wm.getClosestStorm(Vec3.createVectorHelper(xCoord, StormObject.layers.get(0), zCoord), deflectorRadius, StormObject.STATE_NORMAL, true);
		    		
		    		List<StormObject> storms = wm.getStormsAround(Vec3.createVectorHelper(xCoord, StormObject.layers.get(0), zCoord), deflectorRadius);
		    		
		    		for (int i = 0; i < storms.size(); i++) {
		    			StormObject storm = storms.get(i);
		    			
		    			if (storm != null) {
		    				wm.removeStormObject(storm.ID);
			    			wm.syncStormRemove(storm);
		    			}
		    		}
		    		
		    		
		    	}
    		}
    	}
    }

    public void writeToNBT(NBTTagCompound var1)
    {
        super.writeToNBT(var1);
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        super.readFromNBT(var1);

    }
}
