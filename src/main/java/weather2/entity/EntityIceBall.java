package weather2.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import CoroUtil.api.weather.IWindHandler;
import CoroUtil.componentAI.ICoroAI;
import CoroUtil.entity.EntityThrowableUsefull;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityIceBall extends EntityThrowableUsefull implements IWindHandler
{
	public int ticksInAir;
	
	@SideOnly(Side.CLIENT)
	public boolean hasDeathTicked;

	public EntityIceBall(World world)
	{
		super(world);
	}

	public EntityIceBall(World world, EntityLivingBase entityliving)
	{
		super(world, entityliving);
		
		float speed = 0.7F;
		float f = 0.4F;
        this.motionX = (-Math.sin(-this.rotationYaw / 180.0F * (float)Math.PI) * Math.cos(-this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (Math.cos(-this.rotationYaw / 180.0F * (float)Math.PI) * Math.cos(-this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (-Math.sin((-this.rotationPitch + this.func_70183_g()) / 180.0F * (float)Math.PI) * f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed, 1.0F);
	}

	public EntityIceBall(World world, double d, double d1, double d2)
	{
		super(world, d, d1, d2);
	}
	
	@Override
	public void onUpdate()
    {
		super.onUpdate();
		
		//gravity
		this.motionY -= 0.1F;
		
		if (this.motionY <= -3) {
			this.motionY = -3;
		}
		
		if (!this.worldObj.isRemote)
        {
			
			ticksInAir++;
			
			if (this.isCollided) {
				setDead();
			}
			
			if (ticksInAir > 120) {
				setDead();
			}
			
			if (this.worldObj.getClosestPlayer(this.posX, 50, this.posZ, 80) == null) {
				setDead();
			}
        } else {
        	tickAnimate();
        }
    }
	
	@Override
	protected float getGravityVelocity() {
		return 0F;
	}
	
	@Override
	public MovingObjectPosition tickEntityCollision(Vec3 vec3, Vec3 vec31) {
		MovingObjectPosition movingobjectposition = null;
		
        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(0.5D, 1D, 0.5D));
        double d0 = 0.0D;
        EntityLivingBase entityliving = this.getThrower();

        for (int j = 0; j < list.size(); ++j)
        {
            Entity entity1 = (Entity)list.get(j);

            if (entity1.canBeCollidedWith() && (entity1 != entityliving && this.ticksInAir >= 4))
            {
                entity = entity1;
                break;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new MovingObjectPosition(entity);
            /*if (movingobjectposition != null) {
            	this.onImpact(movingobjectposition);
            	setDead();
            }*/
        }
        return movingobjectposition;
	}

	@Override
	protected void onImpact(MovingObjectPosition movingobjectposition)
	{
		
		if (movingobjectposition.entityHit != null)
		{
			if (!worldObj.isRemote)
			{
				
				byte damage = 5;
				
				if (movingobjectposition.entityHit instanceof ICoroAI && getThrower() instanceof ICoroAI) {
					if (((ICoroAI) getThrower()).getAIAgent().dipl_info != ((ICoroAI) movingobjectposition.entityHit).getAIAgent().dipl_info) {
						movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), damage);
					} else {

					}
				} else {
					movingobjectposition.entityHit.attackEntityFrom(DamageSource.fallingBlock, damage);
				}
				
				/*if (movingobjectposition.entityHit instanceof EntityLiving) {
					((EntityLiving)movingobjectposition.entityHit).knockBack(par1Entity, par2, par3, par5)
				}*/
				//movingobjectposition.entityHit.setFire(10);

				/*if (movingobjectposition.entityHit instanceof EntityBlaze)
            {
                byte0 = 3;
            }*/
            /*     if (movingobjectposition.entityHit instanceof EntityKoaMember && thrower instanceof EntityKoaMember) {
    			if (((EntityKoaMember) thrower).dipl_team != ((EntityKoaMember) movingobjectposition.entityHit).dipl_team) {
    				movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), byte0);
    			} else {

    			}
    		}
            else if (!(movingobjectposition.entityHit instanceof EntityKoaMemberNew)) { 
            	if (!movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), byte0));
            	if (thrower instanceof EntityPlayer) {
            		int what = 0;
            	}
            } else if (movingobjectposition.entityHit instanceof EntityKoaMemberNew && thrower instanceof EntityKoaMemberNew) {
    			if (((EntityKoaMemberNew) thrower).dipl_team != ((EntityKoaMemberNew) movingobjectposition.entityHit).dipl_team) {
    				movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), byte0);
    			} else {

    			}
    		}
        } 
        for (int i = 0; i < 30; i++)
        {
            //worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        	double speed = 0.01D;
        	EntityTexFX var31 = new EntityTexFX(worldObj, posX, posY, posZ, rand.nextGaussian()*rand.nextGaussian()*speed, rand.nextGaussian()*speed, rand.nextGaussian()*rand.nextGaussian()*speed, (rand.nextInt(80)/10), 0, mod_EntMover.effLeafID);
            var31.setGravity(0.3F);
            Random rand = new org.bogdang.modifications.random.XSTR();
            var31.rotationYaw = rand.nextInt(360);
            mod_ExtendedRenderer.rotEffRenderer.addEffect(var31);
        }
             */
				if (!worldObj.isRemote) {
					setDead();
				}
				
			}
		}
		
		
		
		if (!worldObj.isRemote) {
			worldObj.playSoundEffect(posX, posY, posZ, "step.stone", 3F, 5F);//0.2F + worldObj.rand.nextFloat() * 0.1F);
			setDead();
			//cpw.mods.fml.common.FMLLog.info("server: " + posX);
		} else {
			tickDeath();
		}
		
	}
	
	@Override
	public void setDead() {
		if (worldObj.isRemote) tickDeath();
		super.setDead();
	}
	
	@SideOnly(Side.CLIENT)
	public void tickAnimate() {
		
	}
	
	@SideOnly(Side.CLIENT)
	public void tickDeath() {
		if (!hasDeathTicked) {
			//cpw.mods.fml.common.FMLLog.info("client: " + posX);
			hasDeathTicked = true;
		}
	}

	@Override
	public float getWindWeight() {
		return 4;
	}

	@Override
	public int getParticleDecayExtra() {
		return 0;
	}
}
