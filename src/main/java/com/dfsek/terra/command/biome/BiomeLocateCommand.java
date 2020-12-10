package com.dfsek.terra.command.biome;

import com.dfsek.terra.Terra;
import com.dfsek.terra.api.gaea.command.WorldCommand;
import com.dfsek.terra.async.AsyncBiomeFinder;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeLocateCommand extends WorldCommand {
    private final boolean tp;

    public BiomeLocateCommand(com.dfsek.terra.api.gaea.command.Command parent, boolean teleport) {
        super(parent);
        this.tp = teleport;
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World world) {
        String id = args[0];
        int maxRadius;
        try {
            maxRadius = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.biome.invalid-radius", sender, args[1]);
            return true;
        }
        UserDefinedBiome b;
        try {
            b = ((Terra) getMain()).getWorld(world).getConfig().getBiome(id);
        } catch(IllegalArgumentException | NullPointerException e) {
            LangUtil.send("command.biome.invalid", sender, id);
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(getMain(), new AsyncBiomeFinder(((Terra) getMain()).getWorld(world).getGrid(), b, sender.getLocation().clone().multiply((1D / ((Terra) getMain()).getTerraConfig().getBiomeSearchResolution())), 0, maxRadius, location -> {
            if(location != null) {
                LangUtil.send("command.biome.biome-found", sender, String.valueOf(location.getBlockX()), String.valueOf(location.getBlockZ()));
                if(tp) {
                    Location l = new Location(sender.getWorld(), location.getX(), sender.getLocation().getY(), location.getZ());
                    Bukkit.getScheduler().runTask(getMain(), () -> sender.teleport(l));
                }
            } else LangUtil.send("command.biome.unable-to-locate", sender);
        }, (Terra) getMain()));
        return true;
    }

    @Override
    public String getName() {
        return tp ? "teleport" : "locate";
    }

    @Override
    public List<com.dfsek.terra.api.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(!(sender instanceof Player) || !(((Player) sender).getWorld().getGenerator() instanceof TerraChunkGenerator))
            return Collections.emptyList();
        List<String> ids = ((Terra) getMain()).getWorld(((Player) sender).getWorld()).getConfig().getBiomeIDs();
        if(args.length == 1)
            return ids.stream().filter(string -> string.toUpperCase().startsWith(args[0].toUpperCase())).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
