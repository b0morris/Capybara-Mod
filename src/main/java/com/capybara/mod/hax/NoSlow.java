package com.capybara.mod.hax;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;

public class NoSlow extends Base {
    private World world;
    private HashMap<String, User> entities;

    public NoSlow(){
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void worldLoaded(World world){
        this.world = world;
        entities = new HashMap<String, User>();
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.END || this.world == null) return;
        World world = event.player.worldObj;
        List<EntityPlayer> players = world.playerEntities;
        for(EntityPlayer player : players){
            if(!entities.containsKey(player.getName())){
                entities.put(player.getName(), new User(player));
            }
            else {
                User user = entities.get(player.getName());
                user.updateSpeed(player);
                if(user.is_eating){
                    // Max sprint speed 0.337
                    FMLLog.info("PLAYER IS EATING "+ user.player +" SPEED "+ user.speed +" PERCENT "+ user.percent_cheating());
                    user.eating_ticks += 1;
                    if(user.speed > 0.1)
                        user.fast_ticks += 1;
                }
                if(user.is_cheating() && !user.marked_cheater){
                    user.marked_cheater = true;
                    FMLLog.info("CHEATING!!! "+ user.player.getName());
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[[[ CHEATER: "+ user.player.getName() +" ]]]"));
                }
            }
        }
    }

    @SubscribeEvent
    public void eatingFoodStart(PlayerUseItemEvent.Start event){
        FMLLog.info("STARTED EATING FOOD "+ event.entity +" "+ event.item.getItem().getRegistryName() +" duration "+ event.duration);
        if(event.item.getItem().getRegistryName().equals("minecraft:golden_apple")){
            User user = entities.get(event.entity.getName());
            if(user != null){
                user.is_eating = true;
                entities.put(event.entity.getName(), user);
            }
        }
    }

    @SubscribeEvent
    public void eatingFoodTick(PlayerUseItemEvent.Tick event){
        //FMLLog.info("TICK EATING FOOD "+ event.entity +" "+ event.item.getItem().getRegistryName() +" duration "+ event.duration);
    }

    @SubscribeEvent
    public void eatingFoodStop(PlayerUseItemEvent.Stop event){
        FMLLog.info("STOPPED EATING FOOD "+ event.entity +" "+ event.item.getItem().getRegistryName());
        User user = entities.get(event.entity.getName());
        if(user != null){
            user.is_eating = false;
            entities.put(event.entity.getName(), user);
        }
    }

    @SubscribeEvent
    public void eatingFoodFinish(PlayerUseItemEvent.Finish event){
        FMLLog.info("FINISHED EATING FOOD "+ event.entity +" "+ event.item.getItem().getRegistryName());
        User user = entities.get(event.entity.getName());
        if(user != null){
            user.is_eating = false;
            entities.put(event.entity.getName(), user);
        }
    }

    private static class User {
        public EntityPlayer player;
        public double speed;
        public boolean is_eating = false;
        public int eating_ticks = 0;
        public int fast_ticks = 0;
        public boolean marked_cheater = false;

        User(EntityPlayer p){
            this.player = p;
            this.updateSpeed(p);
        }

        public void updateSpeed(EntityPlayer p){
            this.speed = Math.sqrt(Math.pow(p.posX - p.prevPosX, 2) + Math.pow(p.posZ - p.prevPosZ, 2));
            //FMLLog.info("Player "+ this.player +" SPEED "+ this.speed);
        }

        public double percent_cheating(){
            return (double)this.fast_ticks / this.eating_ticks;
        }

        public boolean is_cheating(){
            return this.percent_cheating() > 0.2;
        }
    }
}
