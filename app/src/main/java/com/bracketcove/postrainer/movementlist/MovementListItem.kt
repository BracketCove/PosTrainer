package com.bracketcove.postrainer.movementlist


//Since Resource Ids are generated dynamically at run time, I've created this Model class to make it more convenient
//for the View and Adapter to get those Image Ids.
class MovementListItem(
    val name: String,
    val targets:List<Int>,
    val thumbnail: Int,
    val difficulty: Int
)