package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.state.BlockState;

public class BufferedStateManipulator implements BufferedItem {
    private final TerraPlugin main;
    private final String data;

    public BufferedStateManipulator(TerraPlugin main, String state) {
        this.main = main;
        this.data = state;
    }

    @Override
    public void paste(Location origin) {
        BlockState state = origin.getBlock().getState();
        try {
            state.applyState(data);
            state.update(false);
        } catch(Exception e) {
            main.getLogger().warning("Could not apply BlockState at " + origin + ": " + e.getMessage());
        }
    }
}
