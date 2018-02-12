package com.valenguard.client.maps;

import com.valenguard.client.constants.ClientConstants;

import java.io.File;
import java.io.FilenameFilter;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 2/10/2018 @ 6:04 PM
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

public class PCMapFileHandler implements InternalFileManager {

    @Override
    public File[] loadFiles() {
        File dir = new File(ClientConstants.MAP_DIRECTORY);

        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".tmx");
            }
        });
    }
}
