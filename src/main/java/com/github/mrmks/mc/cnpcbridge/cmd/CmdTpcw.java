package com.github.mrmks.mc.cnpcbridge.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.mrmks.mc.cnpcbridge.StaticUtils.*;

public class CmdTpcw implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(command.getPermission())) {
            // player x y z rotate pitch world [0|1|2]
            if (args.length > 6) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player == null || !player.isOnline()) {
                    sender.sendMessage("§cCan't find player named " + args[0]);
                    return true;
                }
                double x, y, z;
                float rotate, pitch;
                try {
                    Location location = player.getLocation();
                    x = parseCoordinate(args[1], location.getX());
                    y = parseCoordinate(args[2], location.getY());
                    z = parseCoordinate(args[3], location.getZ());
                    rotate = parseAngle(args[4], location.getYaw());
                    pitch = parsePitch(args[5], location.getPitch());
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cCan't parse coordinate or angle to number");
                    return true;
                }
                World world = Bukkit.getWorld(args[6]);
                // 0 do nothing, 1 setSpawnPoint, 2 resetSpawnPoint to now world's spawn point;
                int spawn;
                try {
                    spawn = args.length > 7 ? Integer.parseInt(args[7]) : 0;
                } catch (NumberFormatException e) {
                    sender.sendMessage("The arg \"" + args[7] + "\" can't be parse to int");
                    return false;
                }
                if (world != null) {
                    Location location = new Location(world, x, y, z, rotate, pitch);
                    if (spawn == 2) player.setBedSpawnLocation(player.getWorld().getSpawnLocation(), true);
                    boolean suc = player.teleport(location);
                    if (!suc) sender.sendMessage("§cCan't teleport target player to the position");
                    if (spawn == 1 && suc) player.setBedSpawnLocation(location, true);
                } else {
                    sender.sendMessage("§cCan't find the world named " + args[6]);
                }
            } else {
                return false;
            }
        } else {
            sender.sendMessage(command.getPermissionMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(command.getPermission())) {
            if (args.length == 1) return null;
            else if (args.length == 7) {
                String s_wn = args[6];
                Stream<String> list = Bukkit.getWorlds().stream().map(World::getName);
                if (!s_wn.isEmpty()) list = list.filter(w->w.startsWith(s_wn));
                return list.sorted().collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

}
