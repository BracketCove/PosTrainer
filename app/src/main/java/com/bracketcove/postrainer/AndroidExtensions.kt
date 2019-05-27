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


fun Fragment.showToast(msg:String) = Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()

/** Wakelock **/
