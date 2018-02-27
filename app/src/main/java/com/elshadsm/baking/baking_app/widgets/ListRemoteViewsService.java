package com.elshadsm.baking.baking_app.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Elshad Seyidmammadov on 27.02.2018.
 */

public class ListRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
