package com.fraz.dartlog.db;

import android.provider.BaseColumns;

public final class DartLogContract {

    public DartLogContract() {}

    /* Inner class that defines the table contents */
    public static abstract class PlayerEntry implements BaseColumns {
        public static final String TABLE_NAME = "player";
        public static final String COLUMN_NAME_PLAYER_NAME = "name";
    }
}
