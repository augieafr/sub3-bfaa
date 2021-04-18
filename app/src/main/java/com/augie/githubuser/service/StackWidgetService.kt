package com.augie.githubuser.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.augie.githubuser.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}