package weather2.block;

import weather2.ClientTickHandler;
import weather2.Weather;
import weather2.config.ConfigMisc;
import weather2.util.WeatherUtilSound;
import weather2.weathersystem.storm.StormObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityTSiren extends TileEntity
{
    public long lastPlayTime = 0L;
    public long lastVolUpdate = 0L;
    //public int soundID = -1;
    public int lineBeingEdited = -1;
    protected AxisAlignedBB aabb;

    @Override
    public void validate() {
    	super.validate();
    	aabb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
    	return aabb;
    }

    public void updateEntity()
    {
    	if (worldObj.isRemote) {
    		tickClient();
    	}
    }
    
    @SideOnly(Side.CLIENT)
    public void tickClient() {
    	
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	
    	if (this.lastPlayTime < System.currentTimeMillis())
        {
    		StormObject so = ClientTickHandler.weatherManager.getClosestStorm(Vec3.createVectorHelper(xCoord, yCoord, zCoord), ConfigMisc.sirenActivateDistance, StormObject.STATE_FORMING);

            if (so != null)
            {
            	//if (so.attrib_tornado_severity > 0) {
            		//Weather.dbg("soooooouuuunnnnddddddd");
	                this.lastPlayTime = System.currentTimeMillis() + 13000L;
	                /*this.soundID = */WeatherUtilSound.playNonMovingSound(Vec3.createVectorHelper(xCoord, yCoord, zCoord), Weather.modID + ":streaming.siren", 1.0F, 1.0F, 120);
            	//}
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
