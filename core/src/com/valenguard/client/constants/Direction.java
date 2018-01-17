package com.valenguard.client.constants;

import lombok.Getter;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/8/2018 @ 10:17 PM
 * ______________________________________________________
 *
 * Copyright © 2017 Valenguard.com. All Rights Reserved.
 *
 * No part of this project and/or code and/or source code 
 * and/or source may be reproduced, distributed, or 
 * transmitted in any form or by any means, including 
 * photocopying, recording, or other electronic or 
 * mechanical methods, without the prior written 
 * permission of the owner.
 *******************************************************/

public enum Direction {

    NONE((byte) 0x00),
    UP((byte) 0x01),
    DOWN((byte) 0x02),
    LEFT((byte) 0x03),
    RIGHT((byte) 0x04);

    @Getter
    private byte direction;

    Direction(byte direction) {
        this.direction = direction;
    }
}
