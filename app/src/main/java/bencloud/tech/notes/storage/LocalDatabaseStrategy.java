package bencloud.tech.notes.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import bencloud.tech.notes.recycler.RecyclerPacket;

public class LocalDatabaseStrategy extends SQLiteOpenHelper implements StorageStrategy {

  private static final String DB_NAME = "notesDB";
  private static final String TABLE_NAME = "notes";
  private static final String COMPLETE = "complete";
  private static final String PRIORITY = "priority";
  private static final String CREATION_DATE = "creation_date";
  private static final String COMPLETION_DATE = "completion_date";
  private static final String DESCRIPTION = "description";

  public LocalDatabaseStrategy(Context context) {
    super(context, DB_NAME, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    String query = String.format("create table %s (primKey INTEGER PRIMARY KEY, %s text not null"
            + ", %s text not null, %s text not null, " + "%s text not null, %s text not null)",
        TABLE_NAME, COMPLETE, PRIORITY, CREATION_DATE, COMPLETION_DATE, DESCRIPTION);
    db.execSQL(query);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }

  /**** INTERFACE METHODS ****/
  @Override
  public List<RecyclerPacket> readCollection(String... info) {
    SQLiteDatabase db = this.getReadableDatabase();
    List<RecyclerPacket> arr = new ArrayList<>();
    String query = "select * FROM " + TABLE_NAME;
    Cursor c = db.rawQuery(query, null);

    if (!c.moveToFirst()) {
      return new ArrayList<>();
    }

    do {
      int i = 1; // id is index 0
      String complete = c.getString(i++);
      String priority = c.getString(i++);
      String creationDate = c.getString(i++);
      String completionDate = c.getString(i++);
      String description = c.getString(i);

      RecyclerPacket h = new RecyclerPacket(complete, priority, creationDate, completionDate,
          description);
      arr.add(h);
    } while (c.moveToNext());

    c.close();

    return arr;
  }

  @Override
  public void createItem(String... info) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    int i = 0;
    contentValues.put(COMPLETE, info[i++]);
    contentValues.put(PRIORITY, info[i++]);
    contentValues.put(CREATION_DATE, info[i++]);
    contentValues.put(COMPLETION_DATE, info[i++]);
    contentValues.put(DESCRIPTION, info[i]);
    db.insert(TABLE_NAME, null, contentValues);
  }

  @Override
  public void deleteItem(String... info) {
    SQLiteDatabase db = this.getWritableDatabase();
    int i = 0;
    String deleteString = String
        .format("%s='%s' AND %s='%s' AND %s='%s' AND %s='%s' AND %s='%s'",
            COMPLETE, info[i++],
            PRIORITY, info[i++],
            CREATION_DATE, info[i++],
            COMPLETION_DATE, info[i++],
            DESCRIPTION, info[i].replace("'", "''"));
    db.delete(TABLE_NAME, deleteString, null);
  }
}