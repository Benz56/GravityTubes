package com.benzoft.gravitytubes.runtimedata;


import com.benzoft.gravitytubes.GravityBar;
import com.benzoft.gravitytubes.GravityTube;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private final UUID uniqueId;
    private final GravityBar gravityBar;
    private GravityTube gravityTube;
    private boolean flying;

    PlayerData(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        gravityBar = new GravityBar(this);
    }
}
