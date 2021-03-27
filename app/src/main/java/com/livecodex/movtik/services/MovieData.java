package com.livecodex.movtik.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_ACTORS;
import static com.livecodex.movtik.services.Constants.MOVIE_DIRECTOR;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_RATING;
import static com.livecodex.movtik.services.Constants.MOVIE_REVIEW;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.MOVIE_YEAR;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class MovieData extends SQLiteOpenHelper {

    private static final String DB_NAME = "movieRecords.db";
    private static final int DB_VERSION = 3;

    public MovieData(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + _ID
                + "  INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_TITLE + " TEXT NOT NULL,"
                + MOVIE_YEAR + " TEXT,"
                + MOVIE_DIRECTOR + " TEXT,"
                + MOVIE_ACTORS + " TEXT,"
                + MOVIE_RATING + " INTEGER,"
                + MOVIE_REVIEW + " TEXT,"
                + MOVIE_FAVOURITES + " BOOLEAN);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
