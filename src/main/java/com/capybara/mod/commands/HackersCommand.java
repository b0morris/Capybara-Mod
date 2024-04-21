package com.capybara.mod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class HackersCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "hackers";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/hackers";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0){
            List<EntityPlayer> players = Minecraft.getMinecraft().thePlayer.getEntityWorld().playerEntities;
            StringBuilder names = new StringBuilder();
            for(EntityPlayer p : players){
                names.append(p.getName() +", ");
            }
            ChatComponentText msg = new ChatComponentText("Players in lobby: "+ players.size() +" :: "+ names.toString());
            msg.getChatStyle().setColor(EnumChatFormatting.GREEN);
            Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
        }
    }
}
