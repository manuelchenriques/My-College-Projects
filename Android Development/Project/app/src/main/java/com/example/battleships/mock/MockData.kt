package com.example.battleships.mock

import com.example.battleships.model.Stat
import kotlin.random.Random


val mockRanking = arrayListOf(
    Stat(Random.nextInt(1,999),"Oofus1",10,9),
    Stat(Random.nextInt(1,999),"Oofus2",10,8),
    Stat(Random.nextInt(1,999),"Oofus3",10,7),
    Stat(Random.nextInt(1,999),"Oofus4",10,6),
    Stat(Random.nextInt(1,999),"Oofus5",10,5),
    Stat(Random.nextInt(1,999),"Oofus6",10,4),
    Stat(Random.nextInt(1,999),"Oofus7",10,3),
    Stat(Random.nextInt(1,999),"Oofus8",10,2),
    Stat(Random.nextInt(1,999),"Oofus9",10,1),
    Stat(Random.nextInt(1,999),"Oofus0",10,0))
