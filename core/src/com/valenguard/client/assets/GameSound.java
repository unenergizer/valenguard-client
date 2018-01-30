package com.valenguard.client.assets;

/********************************************************
 * Valenguard MMO Client and Valenguard MMO Server Info
 *
 * Owned by Robert A Brown & Joseph Rugh
 * Created by Robert A Brown & Joseph Rugh
 *
 * Project Title: valenguard-client
 * Original File Date: 1/18/2018 @ 7:06 AM
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

public enum GameSound {

    EAT("17661_SFX_HumanEatingPotatoChips1.wav");

    private String filePath;

    GameSound(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return "sounds/" + filePath;
    }
}
