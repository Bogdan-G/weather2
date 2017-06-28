package weather2;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import weather2.client.SceneEnhancer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.*;

public class EventHandlerFML {

	public static World lastWorld = null;
	
	@SubscribeEvent
	public void tickWorld(WorldTickEvent event) {
		if (event.phase == Phase.START) {
			
		}
	}
	
	@SubscribeEvent
	public void tickServer(ServerTickEvent event) {
		
		if (event.phase == Phase.START) {
			//System.out.println("tick weather2");
			ServerTickHandler.onTickInGame();
		}
		
	}
	
	@SubscribeEvent
	public void tickClient(ClientTickEvent event) {
		if (event.phase == Phase.START) {
			ClientProxy.clientTickHandler.onTickInGame();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tickRenderScreen(RenderTickEvent event) {
		if (event.phase == Phase.END) {
			ClientProxy.clientTickHandler.onRenderScreenTick();
		} else if (event.phase == Phase.START) {
			//fix for sky flicker with global overcast on and transitioning between vanilla weather states
			Minecraft mc = Minecraft.getMinecraft();
			if (mc != null && mc.thePlayer != null && mc.theWorld != null) {
				EntityPlayer entP = mc.thePlayer;
				float curRainStr = SceneEnhancer.getRainStrengthAndControlVisuals(entP, true);
				curRainStr = Math.abs(curRainStr);
				mc.theWorld.setRainStrength(curRainStr);
			}
		}
	}
	
	@SubscribeEvent
	public void playerLoggedIn(PlayerLoggedInEvent event) {
		ServerTickHandler.playerJoinedServerSyncFull((EntityPlayerMP) event.player);
	}
}
