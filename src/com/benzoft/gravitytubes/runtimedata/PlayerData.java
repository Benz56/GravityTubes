package com.benzoft.gravitytubes.runtimedata;


import com.benzoft.gravitytubes.GravityBar;
import com.benzoft.gravitytubes.GravityTube;

import java.util.UUID;

public class PlayerData {

    private final UUID uniqueId;
    private final GravityBar gravityBar;
    private GravityTube gravityTube;

    PlayerData(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        gravityBar = new GravityBar(this);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public GravityTube getGravityTube() {
        return gravityTube;
    }

    public void setGravityTube(final GravityTube gravityTube) {
        this.gravityTube = gravityTube;
    }

    public GravityBar getGravityBar() {
        return gravityBar;
    }
}
