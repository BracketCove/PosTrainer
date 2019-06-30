package com.wiseassblog.domain.domainmodel

data class Movement(
    val name: String,
    //target muscle groups: ["Arms",
    val targets: List<String>,
    //frequency of training: "2 sets per day"
    val frequency: String,
    //Recommended repetitions or duration: "30-60 seconds
    val timeOrRepetitions: String,
    //convenience for front end
    val isTimeBased: Boolean,
    //Where to find movement images
    val imageResourceNames: List<String>,
    val difficulty: String,
    //where to find video to play
    val videoUrl:String?,
    //String resource names
    val description: String,
    val instructions: String


)