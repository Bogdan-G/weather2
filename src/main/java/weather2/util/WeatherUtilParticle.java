package weather2.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import CoroUtil.OldUtil;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.Vec3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import extendedrenderer.particle.entity.EntityRotFX;
import extendedrenderer.particle.entity.EntityTexFX;

public class WeatherUtilParticle {
    public static List fxLayers[];
    
    public static int effLeafID = 0;
    public static int effRainID = 1;
    public static int effWindID = 2;
    public static int effSnowID = 3;
    /*public static int effSandID = 4;
    public static int effWind2ID = 2;*/
    
    public static Random rand = new org.bogdang.modifications.random.XSTR();
    
    public static Vec3[] rainPositions;
    public static int maxRainDrops = 2000;//80
    //public static int rainDrops = 20;
    
    static {
    	rainPositions = new Vec3[maxRainDrops];
        
        float range = 60F;//20F
        
        for (int i = 0; i < maxRainDrops; i++) {
        	rainPositions[i] = Vec3.createVectorHelper((rand.nextFloat() * range) - (range/2), (rand.nextFloat() * range/16) - (range/32), (rand.nextFloat() * range) - (range/2));
        }
    }
    
    //weather2: not sure what will happen to this in 1.7, copied over for convinience
    public static int getParticleAge(EntityFX ent)
    {
    	if (OldUtil.getPrivateValueBoth(EntityFX.class, ent, "field_70546_d", "particleAge") != null) {
    		return (Integer) OldUtil.getPrivateValueBoth(EntityFX.class, ent, "field_70546_d", "particleAge");
    	} else {
    		return 0;
    	}
    }

    //weather2: not sure what will happen to this in 1.7, copied over for convinience
    public static void setParticleAge(EntityFX ent, int val)
    {
        OldUtil.setPrivateValueBoth(EntityFX.class, ent, "field_70546_d", "particleAge", val);
    }

    @SideOnly(Side.CLIENT)
    public static void getFXLayers()
    {
        //fxLayers
        Field field = null;

        try
        {
            field = (EffectRenderer.class).getDeclaredField("field_78876_b");//ObfuscationReflectionHelper.remapFieldNames("net.minecraft.client.particle.EffectRenderer", new String[] { "fxLayers" })[0]);
            field.setAccessible(true);
            fxLayers = (List[])field.get(FMLClientHandler.instance().getClient().effectRenderer);
        }
        catch (Exception ex)
        {
        	//cpw.mods.fml.common.FMLLog.info("temp message: obf reflection fail!");
        	//cpw.mods.fml.common.FMLLog.log(org.apache.logging.log4j.Level.WARN, (Throwable)ex, "Weather2 stacktrace: %s", (Throwable)ex);
            try
            {
                field = (EffectRenderer.class).getDeclaredField("fxLayers");
                field.setAccessible(true);
                fxLayers = (List[])field.get(FMLClientHandler.instance().getClient().effectRenderer);
            }
            catch (Exception ex2)
            {
                ex2.printStackTrace();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static float getParticleWeight(EntityRotFX entity1)
    {
    	//commented out for weather2 copy
        /*if (entity1 instanceof EntityFallingRainFX)
        {
            return 1.1F;
        }*/

        if (entity1 instanceof EntityTexFX)
        {
            return 5.0F + ((float)entity1.getAge() / 200);
        }

        //commented out for weather2 copy
        /*if (entity1 instanceof EntityWindFX)
        {
            return 1.4F + ((float)entity1.getAge() / 200);
        }*/

        if (entity1 instanceof EntityFX)
        {
            return 5.0F + ((float)entity1.getAge() / 200);
        }

        return -1;
    }
    
    /*@SideOnly(Side.CLIENT)
    public static void shakeTrees(int range)
    {
        int size = range;
        int hsize = size / 2;
        int curX = (int)player.posX;
        int curY = (int)player.posY - 1;
        int curZ = (int)player.posZ;
        //if (true) return;
        float windStr = 1F;

        for (int xx = curX - hsize; xx < curX + hsize; xx++)
        {
            for (int yy = curY - hsize; yy < curY + hsize + 10; yy++)
            {
                for (int zz = curZ - hsize; zz < curZ + hsize; zz++)
                {
                    //REMOVE VINES!!!!!
                    int uh = (int)(40 / (windStr + 0.001));

                    //System.out.println(uh);
                    if (uh < 1)
                    {
                        uh = 1;
                    }

                    if (worldRef.rand.nextInt(uh) == 0)
                    {
                        for (int i = 0; i < p_blocks_leaf.size(); i++)
                        {
                            int id = getBlockId(xx, yy, zz);

                            if (id == ((Block)p_blocks_leaf.get(i)).blockID)
                            {
                                if (id == Block.leaves.blockID)
                                {
                                    if (getBlockId(xx, yy - 1, zz) == 0)
                                    {
                                        EntityRotFX var31 = new EntityTexBiomeColorFX(worldRef, (double)xx, (double)yy - 0.5, (double)zz, 0D, 0D, 0D, 10D, 0, effLeafID, id, getBlockMetadata(xx, yy, zz), xx, yy, zz);
                                        WeatherUtil.setParticleGravity((EntityFX)var31, 0.3F);

                                        for (int ii = 0; ii < 10; ii++)
                                        {
                                            applyWindForce(var31);
                                        }

                                        var31.rotationYaw = rand.nextInt(360);
                                        //var31.spawnAsWeatherEffect();
                                        spawnQueue.add(var31);
                                    }
                                }
                                else
                                {
                                    //This is non leaves, as in wildgrass or wahtever is in the p_blocks_leaf list (no special rules)
                                    EntityRotFX var31 = new EntityTexBiomeColorFX(worldRef, (double)xx, (double)yy + 0.5, (double)zz, 0D, 0D, 0D, 10D, 0, effLeafID, id, getBlockMetadata(xx, yy, zz), xx, yy, zz);
                                    WeatherUtil.setParticleGravity((EntityFX)var31, 0.3F);
                                    //var31.spawnAsWeatherEffect();
                                    spawnQueue.add(var31);
                                }
                            }
                            else if (id == 0)
                            {
                                if (weatherMan.wind.strength > 0.02)
                                {
                                    if (worldRef.rand.nextInt(400 - (int)(weatherMan.wind.strength * 100)) == 0)
                                    {
                                        //EntityFX var31 = new EntitySmokeFX(worldRef, (double)xx, (double)yy+0.5, (double)zz, 0D, 0D, 0D);
                                        EntityRotFX var31 = new EntityTexFX(worldRef, (double)xx, (double)yy + 0.5, (double)zz, 0D, 0D, 0D, 10D, 0, effWind2ID);
                                        //var31.particleGravity = 0.3F;
                                        //mod_ExtendedRenderer.rotEffRenderer.addEffect(var31);

                                        for (int ii = 0; ii < 20; ii++)
                                        {
                                            applyWindForce(var31);
                                        }

                                        WeatherUtil.setParticleGravity((EntityFX)var31, 0.0F);
                                        var31.noClip = true;
                                        WeatherUtil.setParticleScale((EntityFX)var31, 0.3F);
                                        var31.rotationYaw = rand.nextInt(360);
                                        //var31.spawnAsWeatherEffect();
                                        spawnQueue.add(var31);
                                    }
                                }
                            }
                        }

                        int id = getBlockId(xx, yy, zz);

                        if (id == ((Block)p_blocks_sand.get(0)).blockID)
                        {
                            if (id == Block.sand.blockID)
                            {
                                if (getBlockId(xx, yy + 1, zz) == 0)
                                {
                                    EntityTexFX var31 = new EntityTexFX(worldRef, (double)xx, (double)yy + 0.5, (double)zz, 0D, 0D, 0D, 10D, 0, effSandID);
                                    //var31 = new EntityWindFX(worldRef, (double)xx, (double)yy+1.2, (double)zz, 0D, 0.0D, 0D, 9.5D, 1);
                                    var31.rotationYaw = rand.nextInt(360) - 180F;
                                    var31.type = 1;
                                    WeatherUtil.setParticleGravity((EntityFX)var31, 0.6F);
                                    WeatherUtil.setParticleScale((EntityFX)var31, 0.3F);
                                    //var31.spawnAsWeatherEffect();
                                    spawnQueue.add(var31);
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
}
