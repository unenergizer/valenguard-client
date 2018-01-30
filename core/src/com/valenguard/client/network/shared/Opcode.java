package com.valenguard.client.network.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Opcode {
    byte getOpcode();
}

