package com.valenguard.client.maps;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tile {

    private boolean isTraversable;
    private Warp warp;
}
