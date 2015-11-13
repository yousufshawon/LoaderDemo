package com.yousuf.shawon.loaderdemo.app.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import com.yousuf.shawon.loaderdemo.app.db.DbHelper;

/**
 * Created by user on 11/13/2015.
 */
public class DemoLoader extends CursorLoader {

    String tableName = null;
    String[] columns = null;
    String selection = null;
    String[] selectionArgs = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;
    String limit = null;
    
    String TAG = getClass().getSimpleName();

    public DemoLoader(Context context, String tableName, String[] columns) {
        super(context);
        this.tableName = tableName;
        this.columns = columns;
    }

    public DemoLoader(Context context, String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        super(context);
        this.tableName = tableName;
        this.columns = columns;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    @Override
    public Cursor loadInBackground() {
        return DbHelper.getInstance(getContext()).getEntry(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }
}
