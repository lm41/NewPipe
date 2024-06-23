package org.schabi.newpipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import org.schabi.newpipe.util.NavigationHelper

class ExitActivity : Activity() {

    companion object {
        fun exitAndRemoveFromRecentApps(activity: Activity) {
            val intent = Intent(activity, ExitActivity::class.java)

            intent.flags = (
                Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION
                )

            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        finishAndRemoveTask()

        NavigationHelper.restartApp(this)
    }
}
