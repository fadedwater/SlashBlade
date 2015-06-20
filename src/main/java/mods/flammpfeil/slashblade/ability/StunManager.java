package mods.flammpfeil.slashblade.ability;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mods.flammpfeil.slashblade.entity.ai.EntityAIStun;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Furia on 15/06/20.
 */
public class StunManager {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event){
        if(!(event.entity instanceof EntityLiving)) return;
        EntityLiving entity = (EntityLiving)event.entity;

        entity.tasks.addTask(-1,new EntityAIStun(entity));
    }

    @SubscribeEvent
    public void onEntityLivingUpdate(LivingEvent.LivingUpdateEvent event){
        EntityLivingBase target = event.entityLiving;
        if(target == null) return;
        if(target.worldObj == null) return;

        long timeout = target.getEntityData().getLong(EntityAIStun.StunTimeout);
        if(timeout == 0) return;
        timeout = timeout - target.worldObj.getTotalWorldTime();
        if(timeout <= 0 || EntityAIStun.timeoutLimit < timeout){
            target.getEntityData().removeTag(EntityAIStun.StunTimeout);
            return;
        }

        if(target.motionY < 0)
            target.motionY *= 0.5f;
    }

    public static void setStun(EntityLivingBase target, long duration){
        if(target.worldObj == null) return;
        if(!(target instanceof EntityLiving)) return;
        if(duration <= 0) return;

        duration = Math.min(duration, EntityAIStun.timeoutLimit);
        target.getEntityData().setLong(EntityAIStun.StunTimeout,target.worldObj.getTotalWorldTime() + duration);
    }
}
