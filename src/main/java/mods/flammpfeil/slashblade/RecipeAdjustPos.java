package mods.flammpfeil.slashblade;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.InventoryUtility;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class RecipeAdjustPos extends ShapedRecipe
{

    static private Ingredient dirIS(String name){
        return Ingredient.fromStacks(new ItemStack(Items.STICK, 0).setDisplayName(new TextComponentString(name)));
    }

    public RecipeAdjustPos()
    {
        super(SlashBlade.modid + ":adjust" ,3, 3, NonNullList.<Ingredient>from(Ingredient.EMPTY,
                Ingredient.EMPTY, dirIS("Up"), dirIS("Front"),
                dirIS("Left"), Ingredient.fromStacks(new ItemStack(SlashBlade.tool, 1)), dirIS("Right"),
                dirIS("Back"), dirIS("Down"), Ingredient.EMPTY)
        , new ItemStack(SlashBlade.tool, 1));
    }

    public boolean isFactor(ItemStack itemStack){
        return !itemStack.isEmpty() && itemStack.getItem() == Items.STICK;
    }

    
    @Override
    public boolean matches(InventoryCrafting cInv, World par2World)
    {
        {
            ItemStack itemstack = ItemStack.EMPTY;

            int x = 0;
            int y = 0;
            int z = 0;
            if(!InventoryUtility.getStackInRowAndColumn(cInv, 0, 0).isEmpty())
                return false;
            if(!InventoryUtility.getStackInRowAndColumn(cInv,2, 2).isEmpty())
                return false;

            ItemStack tmp;
            if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,0, 1)))
                y += tmp.getCount();
            if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,0, 2)))
                z += tmp.getCount();
            if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,1, 0)))
                x += tmp.getCount();

            if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,1, 2)))
                x -= tmp.getCount();
            if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,2, 0)))
                z -= tmp.getCount();
            if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,2, 1)))
                y -= tmp.getCount();


            ItemStack target = InventoryUtility.getStackInRowAndColumn(cInv,1, 1);
            if(!target.isEmpty() && target.getItem() instanceof ItemSlashBlade)
                itemstack = target;


            return !itemstack.isEmpty() && (x != 0 || y != 0 || z != 0);
        }
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting cInv)
    {
        ItemStack itemstack = ItemStack.EMPTY;

        int x = 0;
        int y = 0;
        int z = 0;

        ItemStack tmp;
        if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,1, 0)))
            y += tmp.getCount();
        if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,2, 0)))
            z += tmp.getCount();
        if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,0, 1)))
            x += tmp.getCount();

        if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,2, 1)))
            x -= tmp.getCount();
        if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,0, 2)))
            z -= tmp.getCount();
        if(isFactor(tmp = InventoryUtility.getStackInRowAndColumn(cInv,1, 2)))
            y -= tmp.getCount();


        ItemStack target = InventoryUtility.getStackInRowAndColumn(cInv,1, 1);
        if(!target.isEmpty() && target.getItem() instanceof ItemSlashBlade)
            itemstack = target;

        itemstack = itemstack.copy();

        NBTTagCompound tag;

        if(!itemstack.hasTag()){
            tag = new NBTTagCompound();
            itemstack.setTag(tag);
        }else{
            tag = itemstack.getTag();
        }

        float ax = tag.getFloat(ItemSlashBlade.adjustXStr);
        float ay = tag.getFloat(ItemSlashBlade.adjustYStr);
        float az = tag.getFloat(ItemSlashBlade.adjustZStr);

        ax = Math.round(ax * 10 + x) / 10.0f;
        ay = Math.round(ay * 10 + y) / 10.0f;
        az = Math.round(az * 10 + z) / 10.0f;

        tag.setFloat(ItemSlashBlade.adjustXStr, ax);
        tag.setFloat(ItemSlashBlade.adjustYStr, ay);
        tag.setFloat(ItemSlashBlade.adjustZStr, az);

        return itemstack;
    }
}

