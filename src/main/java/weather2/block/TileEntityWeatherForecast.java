package weather2.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import weather2.ClientTickHandler;
import weather2.weathersystem.storm.StormObject;

public class TileEntityWeatherForecast extends TileEntity
{
	
	//since client receives data every couple seconds, we need to smooth out everything for best visual
	
	public float smoothAngle = 0;
	public float smoothSpeed = 0;
	
	public float smoothAngleRotationalVel = 0;
	public float smoothAngleRotationalVelAccel = 0;
	
	public float smoothAngleAdj = 0.1F;
	public float smoothSpeedAdj = 0.1F;
	
	public StormObject lastTickStormObject = null;
	
	public List<StormObject> storms = new ArrayList<StormObject>();
	
	//public MapHandler mapHandler;

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
    	if (worldObj.isRemote) {
    		if (worldObj.getTotalWorldTime() % 200 == 0) {
    			lastTickStormObject = ClientTickHandler.weatherManager.getClosestStorm(Vec3.createVectorHelper(xCoord, StormObject.layers.get(0), zCoord), 1024, StormObject.STATE_THUNDER, true);
    			
    			storms = ClientTickHandler.weatherManager.getStormsAround(Vec3.createVectorHelper(xCoord, StormObject.layers.get(0), zCoord), 1024);
    		}
    	} else {
    		/*if (mapHandler == null) {
    			mapHandler = new MapHandler(this);
    		}
    		mapHandler.tick();*/
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
