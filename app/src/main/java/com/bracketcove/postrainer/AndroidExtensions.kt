package com.bracketcove.postrainer

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Activity.addFragmentToActivity(
    fragmentManager: FragmentManager,
    fragment: Fragment,
    frameId: Int,
    tag: String
) = fragmentManager.beginTransaction()
    .replace(frameId, fragment, tag)
    .commit()


fun Fragment.showToast(msg: String) = Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()

fun Fragment.getTargetsByResource(targets: List<String>): List<Int>{
    val newList = mutableListOf<Int>()
    targets.forEach {
        newList.add(
            resources.getIdentifier(
                it,
                "drawable",
                activity?.packageName
            )
        )
    }

    return newList
}

fun Fragment.getThumbnailByResourceName(s: String): Int {
    return resources.getIdentifier(
        s,
        "drawable",
        activity?.packageName
    )
}
