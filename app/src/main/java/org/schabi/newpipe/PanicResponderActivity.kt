package org.schabi.newpipe

import android.app.Activity
import android.os.Bundle

class PanicResponderActivity : Activity() {
    companion object {
        const val PANIC_TRIGGER_ACTION = "info.guardianproject.panic.action.TRIGGER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null && PANIC_TRIGGER_ACTION == intent.action) {
            // TODO: Explicitly clear the search results
            //  once they are restored when the app restarts
            //  or if the app reloads the current video after being killed,
            //  that should be cleared also
            ExitActivity.exitAndRemoveFromRecentApps(this)
        }

        finishAndRemoveTask()
    }
}
