package com.github.gaoxusheng.post;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.lang.String;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONValue;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

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
                    String X = String.valueOf(player.getLocation().getX());
                    String Y = String.valueOf(player.getLocation().getY());
                    String Z = String.valueOf(player.getLocation().getZ());
                    Map var18 = (Map) JSONValue.parse(doPost(args[0],player.getName(), player.getWorld().getName(),args[1], player.getUniqueId(),player.getLevel(),X,Y,Z));
                    if (var18.containsKey("CMD")) {
                        String var30 = ((String) var18.get("CMD"));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), var30);
                    }
                    System.out.println("----POST插件异步任务完成-----");
                }
            }.runTaskAsynchronously(this);
        }
        return true;
    }

    public String doPost(String URL, String player, String world, String value, UUID UUID, int Level, String X, String Y, String Z) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn;
        try {
            URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            String jsonStr = "{\"player\":\""+player+"\", \"world\":\""+world+"\", \"value\":\""+value+"\",\"UUID\":\""+UUID+"\",\"Level\":\""+Level+"\",\"X\":\""+X+"\",\"Y\":\""+Y+"\",\"Z\":\""+Z+"\"}";
            out.write(jsonStr);
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                System.out.println("在运行中发生错误:" + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }
}