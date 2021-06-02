package com.github.gaoxusheng.post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONValue;

/**
 * @author Yun_Nan
 */
public final class Post extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("post").setExecutor(this);
        System.out.println("------------");
        System.out.println("POST插件已上线");
        System.out.println("------------");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length <= 1) {
            return false;
        } else if (!(sender instanceof Player)) {
            return false;
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player player = (Player) sender;
                    String[] var17 = new String[]{"curl", args[0] + "?player=" + player.getName() + "&world=" + player.getWorld().getName() + "&value=" + args[1], "-X", "POST", "-H", "\"Content-Type: application/x-www-form-urlencoded\"", "--data", args[1]};
                    if (var17 != null) {
                        Map var18 = (Map) JSONValue.parse(execCurl(var17));
                        if (var18.containsKey("CMD")) {
                            String var30 = ((String) var18.get("CMD"));
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), var30);
                        }
                        System.out.println("----POST插件异步任务完成-----");
                        return;
                    }

                    this.cancel();
                }
            }.runTaskAsynchronously(this);
        }
        return true;
    }

    public static String execCurl(String[] cmd) {
        ProcessBuilder process = new ProcessBuilder(cmd);

        try {
            Process p = process.start();
            BufferedReader e = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = e.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }

            return builder.toString();
        } catch (IOException var6) {
            var6.printStackTrace();
            return null;
        }
    }

}