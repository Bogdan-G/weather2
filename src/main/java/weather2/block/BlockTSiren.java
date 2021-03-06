package weather2.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTSiren extends BlockContainer
{
    public BlockTSiren(int var1)
    {
        super(Material.clay);
    }

    public int tickRate()
    {
        return 90;
    }
    
    public int tickRate(World world)
    {
        return 90;
    }

    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {}

    @Override
    public TileEntity createNewTileEntity(World var1, int meta)
    {
        return new TileEntityTSiren();
    }
}
