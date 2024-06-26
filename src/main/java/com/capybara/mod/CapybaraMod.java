package com.capybara.mod;

import com.capybara.mod.commands.HackersCommand;
import com.capybara.mod.hax.Base;
import com.capybara.mod.hax.NoSlow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod(modid="CapybaraMod", version="0.0.1")
public class CapybaraMod {
    private ArrayList<Base> hacks = new ArrayList<>();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
        this.hacks = new ArrayList<Base>();
        this.hacks.add(new NoSlow());

        ClientCommandHandler.instance.registerCommand(new HackersCommand());
    }

    private boolean worldLoaded = false;
    private World world = null;

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event){
        if(event.entity != null && Minecraft.getMinecraft().thePlayer != null){
            if(event.entity == Minecraft.getMinecraft().thePlayer) {
                FMLLog.info("ENTITY JOINED WORLD: " + event.entity);
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[[[ YOU JOINED THE WORLD ]]]"));
                this.world = event.world;
                this.worldLoaded = true;
                hacks.forEach(hack -> {
                    hack.worldLoaded(event.world);
                });
            }
        }
    }

    /*
    @SubscribeEvent
    public void worldLoaded(WorldEvent.Load event){
        if(event.world != null) {
            WorldInfo worldInfo = event.world.getWorldInfo();
            if(worldInfo != null) {
                FMLLog.info("LOADING WORLD: " + event.world.getWorldInfo().getGameType());
                if (event.world.getWorldInfo().getWorldName().equals("New World")) {
                    this.world = event.world;
                    this.worldLoaded = true;
                    hacks.forEach(hack -> {
                        hack.worldLoaded(event.world);
                    });
                }
            }
        }
    }
     */

    private Entity cow;
    private int ticks;

    /*
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event){
        EntityPlayer player = event.player;
        if(event.phase == TickEvent.Phase.END) return;
        if(this.worldLoaded){
            this.worldLoaded = false;
            for(Entity entity : world.loadedEntityList) {
                FMLLog.info("Loaded entity: "+ entity.getName());
                if(entity.getName().equals("Cow")){
                    if(cow == null) {
                        cow = entity;
                    }
                    else {
                        if(player.getDistanceToEntity(entity) < player.getDistanceToEntity(cow))
                            cow = entity;
                    }
                }
            }
            FMLLog.info("Closest cow: "+ cow +" :: "+ player.getDistanceToEntity(cow));
            //List<EntityPlayer> players = world.playerEntities;
            //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[[[ PLAYERS: "+ players.size() +" ]]]"));
        }

        ticks += 1;

        if(ticks % 20 == 0){
            if(cow != null){
                float distance1 = event.player.getDistanceToEntity(cow);
                double distance2 = event.player.getDistanceSqToEntity(cow);

                //lookAt(player, cow);
            }
        }
    }
    */

    @SubscribeEvent
    public void livingHurtEvent(LivingHurtEvent event){
        FMLLog.info("Hurt Event to "+ event.entity +" amount "+ event.ammount +" damage type "+ event.source.damageType +" from "+ event.source.getEntity());
    }
}
