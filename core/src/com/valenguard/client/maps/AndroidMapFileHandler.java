package com.valenguard.client.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.valenguard.client.Valenguard;
import com.valenguard.client.constants.ClientConstants;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

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

public class AndroidMapFileHandler implements InternalFileManager {

    @Override
    public File[] loadFiles() {
        FileHandle fileHandle = Gdx.files.internal(ClientConstants.MAP_DIRECTORY);

        List<File> files = new ArrayList<File>();

        for (FileHandle entry : fileHandle.list()) {
            String path = entry.path();

            // make sure were only adding tmx files
            if (path.endsWith(".tmx")) files.add(new File(path));
        }

        return files.toArray(new File[files.size()]);
    }
}
