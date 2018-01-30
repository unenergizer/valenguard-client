package com.valenguard.client.network.shared;

/********************************************************
 * Valenguard MMO ClientConnection and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 12/20/2017 @ 12:10 AM
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

public class Opcodes {

    /**
     * DONT FORGET TO ADD YOUR NETWORK LISTENERS!!!!!!!!!!
     */

    public static final byte INIT_PLAYER_CLIENT = 0x00;
    public static final byte MOVE_REQUEST = 0x01;
    public static final byte MOVE_REPLY = 0x02;
    public static final byte ENTITY_MOVE_UPDATE = 0x03;
    public static final byte ENTITY_JOINED_MAP = 0x04;
    public static final byte ENTITY_EXIT_MAP = 0x05;
    public static final byte PLAYER_MAP_CHANGE = 0x06;
}
