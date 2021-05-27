package com.github.mrmks.mc.cnpcbridge.cmd;

import com.github.mrmks.mc.cnpcbridge.StaticUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.Collections;
import java.util.List;

public class CmdMulti implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(command.getPermission())) {
            String[] cmds = StaticUtils.splitAndConcat(args, "|");
            for (String cmd : cmds) {
                try {
                    if(!cmd.isEmpty()) Bukkit.dispatchCommand(sender, cmd);
                } catch (CommandException e) {
                    sender.sendMessage("§cAn unhandled exception occurred while executing command \"" + cmd + "\"");
                }
            }
        } else {
            sender.sendMessage(command.getPermissionMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //sender.sendMessage("Tab complete is broken since you are using multi-cmd");
        if (sender.hasPermission(command.getPermission())) {
            StaticUtils.LastCommand ary = StaticUtils.findLastCommand(args, "|");
            if (ary != null) {
                PluginCommand cmd = Bukkit.getPluginCommand(ary.cmd);
                if (cmd != null) {
                    return cmd.tabComplete(sender, ary.cmd, ary.args);
                }
            }
        }
        return Collections.emptyList();
    }
}
