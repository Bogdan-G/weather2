package weather2.client.entity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import weather2.entity.EntityMovingBlock;
import weather2.util.WeatherUtilParticle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFlyingBlock extends Render
{
	Block renderBlock;
	
    public RenderFlyingBlock(Block parBlock)
    {
    	renderBlock = parBlock;
    }
    
    @Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TextureMap.locationBlocksTexture;
	}

    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_FOG);
        
        int age = var1.ticksExisted * 5;
        
        float size = 0.3F;// - (age * 0.03F);
        
        if (var1 instanceof EntityMovingBlock) {
        	size = 1;
        }
        
        if (size < 0) size = 0;//move check, since below it there is one more that changes the meaning
        
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        this.bindEntityTexture(var1);
        //this.loadTexture("/terrain.png");
        World var11 = var1.worldObj;
        //GL11.glDisable(GL11.GL_LIGHTING);
        
        RenderBlocks rb = new RenderBlocks(var1.worldObj);
        GL11.glScalef(size, size, size);
        //Tessellator tess = Tessellator.instance;
        //tess.setBrightness(255);
        //tess.setColorOpaque_F(255, 255, 255);
        //renderBlock = Block.netherrack;
        if (var1 instanceof EntityMovingBlock) {
        	try {
        	Block dynamicRenderBlock = ((EntityMovingBlock) var1).tile;
        	GL11.glRotatef(age * 0.1F * 180.0F / 12.566370964050293F - 0.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(age * 0.1F * 180.0F / ((float)Math.PI * 2F) - 0.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(age * 0.1F * 180.0F / ((float)Math.PI * 2F) - 0.0F, 0.0F, 0.0F, 1.0F);
        	rb.setRenderBoundsFromBlock(dynamicRenderBlock);
	        rb.renderBlockAsItem(dynamicRenderBlock, 0, 0.8F);
	        } catch (Exception e) {cpw.mods.fml.common.FMLLog.log(org.apache.logging.log4j.Level.WARN, (Throwable)e, "Weather2 stacktrace: %s", (Throwable)e);}
        } else {
        	//the real one
	        rb.setRenderBoundsFromBlock(renderBlock);
	        rb.renderBlockAsItem(renderBlock, 0, 0.8F);
	        
	        //the fake ones
        	for (int i = 0; i < Math.min(4, WeatherUtilParticle.maxRainDrops); i++) {
        		GL11.glPushMatrix();
        		GL11.glTranslatef((float)WeatherUtilParticle.rainPositions[i].xCoord * 3F, (float)WeatherUtilParticle.rainPositions[i].yCoord * 3F, (float)WeatherUtilParticle.rainPositions[i].zCoord * 3F);
        		GL11.glRotatef(age * 0.1F * 180.0F / 12.566370964050293F - 0.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(age * 0.1F * 180.0F / ((float)Math.PI * 2F) - 0.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(age * 0.1F * 180.0F / ((float)Math.PI * 2F) - 0.0F, 0.0F, 0.0F, 1.0F);
        		rb.setRenderBoundsFromBlock(renderBlock);
    	        rb.renderBlockAsItem(renderBlock, 0, 0.8F);
        		//GL11.glTranslatef((float)-WeatherUtilParticle.rainPositions[i].xCoord, (float)-WeatherUtilParticle.rainPositions[i].yCoord, (float)-WeatherUtilParticle.rainPositions[i].zCoord);
        		GL11.glPopMatrix();
        	}
        	
        }
        
        GL11.glEnable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
