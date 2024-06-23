package org.schabi.newpipe

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.FragmentManager
import org.schabi.newpipe.database.stream.model.StreamEntity
import org.schabi.newpipe.download.DownloadDialog
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.local.dialog.PlaylistDialog
import org.schabi.newpipe.player.playqueue.PlayQueue
import org.schabi.newpipe.player.playqueue.PlayQueueItem
import org.schabi.newpipe.util.NavigationHelper
import org.schabi.newpipe.util.SparseItemUtil
import org.schabi.newpipe.util.external_communication.ShareUtils

fun openPopupMenu(
    playQueue: PlayQueue,
    item: PlayQueueItem,
    view: View,
    hideDetails: Boolean,
    fragmentManager: FragmentManager,
    context: Context,
) {
    val themeWrapper = ContextThemeWrapper(context, R.style.DarkPopupMenu)
    val popupMenu = PopupMenu(themeWrapper, view)
    popupMenu.inflate(R.menu.menu_play_queue_item)

    if (hideDetails) {
        popupMenu.menu.findItem(R.id.menu_item_details).isVisible = false
    }

    popupMenu.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_item_remove -> {
                val index = playQueue.indexOf(item)
                playQueue.remove(index)
                true
            }

            R.id.menu_item_details -> {
                NavigationHelper.openVideoDetail(
                    context,
                    item.serviceId,
                    item.url,
                    item.title,
                    null,
                    false,
                )
                true
            }

            R.id.menu_item_append_playlist -> {
                PlaylistDialog.createCorrespondingDialog(
                    context,
                    listOf(StreamEntity(item)),
                ) { dialog: PlaylistDialog ->
                    dialog.show(
                        fragmentManager,
                        "QueueItemMenuUtil@append_playlist"
                    )
                }
                true
            }

            R.id.menu_item_channel_details -> {
                SparseItemUtil.fetchUploaderUrlIfSparse(
                    context,
                    item.serviceId,
                    item.url,
                    item.uploaderUrl,
                ) { uploaderUrl: String? ->
                    // An intent must be used here.
                    // Opening with FragmentManager transactions is not working,
                    // as PlayQueueActivity doesn't use fragments.
                    NavigationHelper.openChannelFragmentUsingIntent(
                        context,
                        item.serviceId,
                        uploaderUrl,
                        item.uploader,
                    )
                }
                true
            }

            R.id.menu_item_share -> {
                ShareUtils.shareText(
                    context,
                    item.title,
                    item.url,
                    item.thumbnails,
                )
                true
            }

            R.id.menu_item_download -> {
                SparseItemUtil.fetchStreamInfoAndSaveToDatabase(
                    context,
                    item.serviceId,
                    item.url,
                ) { info: StreamInfo? ->
                    val downloadDialog = DownloadDialog(
                        context,
                        info!!
                    )
                    downloadDialog.show(fragmentManager, "downloadDialog")
                }
                true
            }

            else -> {
                false
            }
        }
    }
    popupMenu.show()
}
