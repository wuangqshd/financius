package com.code44.finance.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.code44.finance.App;

public class DBHelper extends SQLiteOpenHelper {
    private static final String NAME = "financius.db";
    private static final int VERSION = 1;

    private static DBHelper singleton;

    private final Context context;

    public DBHelper(Context context) {
        this(context, NAME);
    }

    public DBHelper(Context context, String name) {
        super(context, name, null, VERSION);
        this.context = context;
    }

    public static synchronized DBHelper get() {
        if (singleton == null) {
            singleton = new DBHelper(App.getContext());
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(Tables.Currencies.createScript());
        db.execSQL(Tables.Accounts.createScript());
        db.execSQL(Tables.Categories.createScript());
        db.execSQL(Tables.Tags.createScript());
        db.execSQL(Tables.Transactions.createScript());
        db.execSQL(Tables.TransactionTags.createScript());

        // Create indexes
        createIndex(db, Tables.Currencies.ID);
        createIndex(db, Tables.Accounts.ID);
        createIndex(db, Tables.Categories.ID);
        createIndex(db, Tables.Tags.ID);
        createIndex(db, Tables.Transactions.ID);

        // Add defaults
        addDefaults(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void clear() {
        final SQLiteDatabase database = getWritableDatabase();
        database.delete(Tables.Currencies.TABLE_NAME, null, null);
        // TODO Clear database for the user
    }

    public void addDefaults() {
        addDefaults(getWritableDatabase());
    }

    private void addDefaults(SQLiteDatabase db) {
        DBDefaults.addDefaults(context, db);
    }

    private void createIndex(SQLiteDatabase db, Column serverIdColumn) {
        db.execSQL("create index " + serverIdColumn.getName() + "_idx ON " + serverIdColumn.getTableName() + "(" + serverIdColumn.getName() + ");");
    }
}
