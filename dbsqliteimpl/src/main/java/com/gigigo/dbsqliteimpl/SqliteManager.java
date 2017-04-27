package com.gigigo.dbsqliteimpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SqliteManager extends SQLiteOpenHelper {

  private static int version = 1;
  private static SQLiteDatabase.CursorFactory factory = null;
  private String databaseName;
  private String tableName;
  private String sqlCreate = "CREATE TABLE ";
  private boolean createTableWithColumns;
  private Context context;
  private XmlBuilder xmlBuilder = null;

  public SqliteManager(Context context, String name) {
    super(context, name, factory, version);
    this.databaseName = name;
    this.context = context;
  }

  @Override public void onCreate(SQLiteDatabase db) {

  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }


  public void createTable(SQLiteDatabase db) {
    if (createTableWithColumns) {
      sqlCreate = sqlCreate.substring(0, sqlCreate.length() - 1) + ");";
    } else {
      sqlCreate = sqlCreate + "id INTEGER PRIMARY KEY AUTOINCREMENT);";
    }
    Log.v("-----------", "sql" + sqlCreate);
    db.execSQL("DROP TABLE IF EXISTS " + tableName);
    db.execSQL(sqlCreate);
  }

  public void insertColumn(String name, String type) {
    createTableWithColumns = true;
    sqlCreate += name + " " + type + ",";
  }

  public void getClassNames() {
    Class<ModelObj> clazz = ModelObj.class;
    for (final java.lang.reflect.Field field : clazz.getDeclaredFields()) {
      System.out.println("----------" + field.getName());
    }
  }

  public LinkedHashMap<Integer, ModelObj> loadObjectList(SQLiteDatabase db, String tableName) {

    LinkedHashMap<Integer, ModelObj> rowUser = new LinkedHashMap<>();
    int id = 1;
    ArrayList<String> userList;
    int columns = getTableColumnCount(db, tableName);

    ModelObj obj;

    Cursor cursorDB = db.rawQuery("SELECT * from " + tableName + "", null);
    if (cursorDB.moveToFirst()) {
      userList = new ArrayList<String>();
      for (int i = 0; i < columns; i++) {
        userList.add(cursorDB.getString(i));
      }
      obj = new ModelObj();
      obj.setList(userList);
      rowUser.put(id, obj);
      id++;

      while (cursorDB.moveToNext()) {
        userList = new ArrayList<String>();
        for (int i = 0; i < columns; i++) {
          userList.add(cursorDB.getString(i));
        }
        obj = new ModelObj();
        obj.setList(userList);
        rowUser.put(id, obj);
        id++;
      }
    }
    return rowUser;
  }

  public File loadDatabaseAsJson(String tableName, SQLiteDatabase db){
    File file = null;
    String[] parts = tableName.split("-");
    tableName = parts[0];
    try {
      xmlBuilder = new XmlBuilder();
    } catch (IOException e) {
      e.printStackTrace();
    }
    xmlBuilder.start(databaseName);

    // get the tables
    String sql = "select * from '"+ tableName +"'";
    Cursor c = db.rawQuery(sql, new String[0]);

    if (c.moveToFirst()) {

      //while (c.moveToNext()) {
     // String tableName ="dept";//c.getString(c.getColumnIndex("dep_id"));

      // skip metadata, sequence, and uidx (unique indexes)
      if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")
          && !tableName.startsWith("uidx")) {
        try {
          exportTable(tableName, db);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      // }
    }
    String xmlString = null;
    try {
      xmlString = xmlBuilder.end();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      String exportFileNamePrefix="dataXML";
      file = writeToFile(xmlString, exportFileNamePrefix + ".xml");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  return file;
  }



  private void exportTable(final String tableName, SQLiteDatabase db) throws IOException {
    xmlBuilder.openTable(tableName);
    String sql = "select * from " + tableName;
    Cursor c = db.rawQuery(sql, new String[0]);
    if (c.moveToFirst()) {
      int cols = c.getColumnCount();
      do {
        xmlBuilder.openRow();
        for (int i = 0; i < cols; i++) {
                /*if(i==6)
                {
                    //String id = c.getString( c.getColumnIndex("photo"));
                    String str = new String(image);
                    xmlBuilder.addColumn(c.getColumnName(i), str);
                }*/
          xmlBuilder.addColumn(c.getColumnName(i), c.getString(i));
        }
        xmlBuilder.closeRow();
      } while (c.moveToNext());
    }
    c.close();
    xmlBuilder.closeTable();
  }

  private File writeToFile(final String xmlString, final String exportFileName) throws IOException {
   Log.v("FILEPATH",""+ context.getFilesDir());
    File dir = new File(context.getFilesDir(), "DatabaseToXml");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir, exportFileName);
    file.createNewFile();

    ByteBuffer buff = ByteBuffer.wrap(xmlString.getBytes());
    FileChannel channel = new FileOutputStream(file).getChannel();
    try {
      channel.write(buff);
    } finally {
      if (channel != null) {
        channel.close();
      }
    }
    return file;
  }

  public void readFromFile(File file){

    //Read text from file
    StringBuilder text = new StringBuilder();

    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line;

      while ((line = br.readLine()) != null) {
        text.append(line);
        text.append('\n');
      }
      br.close();
    }
    catch (IOException e) {
      //You'll need to add proper error handling here
    }

    Log.v("FILE",""+ text);
  }
  /**
   * XmlBuilder is used to write XML tags (open and close, and a few attributes)
   * to a StringBuilder. Here we have nothing to do with IO or SQL, just a fancy StringBuilder.
   *
   * @author ccollins
   *
   */
  public static class XmlBuilder {
    private static final String OPEN_XML_STANZA = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final String CLOSE_WITH_TICK = "'>";
    private static final String DB_OPEN = "<database name='";
    private static final String DB_CLOSE = "</database>";
    private static final String TABLE_OPEN = "<table name='";
    private static final String TABLE_CLOSE = "</table>";
    private static final String ROW_OPEN = "<row>";
    private static final String ROW_CLOSE = "</row>";
    private static final String COL_OPEN = "<col name='";
    private static final String COL_CLOSE = "</col>";

    private final StringBuilder sb;

    public XmlBuilder() throws IOException {
      sb = new StringBuilder();
    }

    void start(final String dbName) {
      sb.append(XmlBuilder.OPEN_XML_STANZA);
      sb.append(
          XmlBuilder.DB_OPEN + dbName + XmlBuilder.CLOSE_WITH_TICK);
    }

    String end() throws IOException {
      sb.append(XmlBuilder.DB_CLOSE);
      return sb.toString();
    }

    void openTable(final String tableName) {
      sb.append(
         XmlBuilder.TABLE_OPEN + tableName + XmlBuilder.CLOSE_WITH_TICK);
    }

    void closeTable() {
      sb.append(XmlBuilder.TABLE_CLOSE);
    }

    void openRow() {
      sb.append(XmlBuilder.ROW_OPEN);
    }

    void closeRow() {
      sb.append(XmlBuilder.ROW_CLOSE);
    }

    void addColumn(final String name, final String val) throws IOException {
      sb.append(XmlBuilder.COL_OPEN + name + XmlBuilder.CLOSE_WITH_TICK + val + XmlBuilder.COL_CLOSE);
    }
  }

  public <T extends Serializable> T loadObjectListDBTableMaster(SQLiteDatabase db, String tableName) {
    Log.v("LOADOBJECTSERIALIZABLE",""+ tableName);
    String[] parts = tableName.split("-");
    tableName = parts[0];
    T objectToReturn = null;

    ArrayList<ModelObj> rowUser = new ArrayList<>();
    int id = 1;
    ArrayList<String> userList;

    int columns = getTableColumnCount(db, tableName);

    ModelObj obj;

    Cursor cursorDB = db.rawQuery("SELECT * from " + tableName + "", null);
    if (cursorDB.moveToFirst()) {
      userList = new ArrayList<String>();
      for (int i = 0; i < columns; i++) {
        userList.add(cursorDB.getString(i));
      }
      obj = new ModelObj();
      obj.setList(userList);
      rowUser.add(obj);
      id++;

      while (cursorDB.moveToNext()) {
        userList = new ArrayList<String>();
        for (int i = 0; i < columns; i++) {
          userList.add(cursorDB.getString(i));
        }
        obj = new ModelObj();
        obj.setList(userList);
        rowUser.add(obj);
        id++;
      }
    }

    objectToReturn = (T) rowUser;
    return objectToReturn;
  }

  public ArrayList<String> getTableList(SQLiteDatabase db) {
    ArrayList<String> tableNameList = new ArrayList<String>();
    String tableListStr = "SELECT name FROM sqlite_master;";
    Cursor cursor = db.rawQuery(tableListStr, null);
    cursor.moveToFirst(); // android_metadata
    while (cursor.moveToNext()) {
      String tableName = cursor.getString(0);
      if (!tableName.equals("sqlite_sequence")) {
        tableNameList.add(tableName);
      }
    }
    return tableNameList;
  }

  public boolean checkIfTableExist(SQLiteDatabase db, String tablename) {

    boolean tableExists = false;
    String tableQuery =
        "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tablename + "';";
    Cursor cursor = db.rawQuery(tableQuery,null);
    if (cursor != null){
      if (cursor.moveToFirst()) {
        tableExists = true;
        Log.v("TABLEEXIST", "TRUE" + tablename);
      }
    }

    return tableExists;
  }

  public LinkedHashMap<String, String> getTableColumnNames(SQLiteDatabase db, String tablename) {

    LinkedHashMap<String, String> columnNamesList = new LinkedHashMap<String, String>();
    String tableListStr = "PRAGMA table_info('" + tablename + "');";
    Cursor cursor = db.rawQuery(tableListStr, null);
    if (cursor.moveToFirst()) {
      columnNamesList.put(cursor.getString(1), cursor.getString(2));
      //Log.v("Columns",""+ cursor.getString(0)+cursor.getString(1)+ cursor.getString(2));
      while (cursor.moveToNext()) {
        columnNamesList.put(cursor.getString(1), cursor.getString(2));
        Log.v("Columns", "" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2));
      }
    }
    return columnNamesList;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
    sqlCreate += tableName + "(";
  }

  public void dropTable(SQLiteDatabase db, String tableName) {
    String dropTableStr = "DROP TABLE IF EXISTS " + tableName + " ";
    db.execSQL(dropTableStr);
  }

  public void clearTableContent(SQLiteDatabase db, String tableName) {
    db.delete(tableName, null, null);
    db.execSQL("DELETE FROM " + tableName);
  }

  public boolean checkIfTableHasContent(SQLiteDatabase db, String tableName) {
    boolean hasContent = false;
    String count = "SELECT count(*) FROM '" + tableName + "'";
    Cursor mcursor = db.rawQuery(count, null);
    mcursor.moveToFirst();
    int icount = mcursor.getInt(0);
    if (icount > 0) {
      hasContent = true;
    }
    return hasContent;
  }

  public void dropDatabase(Context context, String dbname) {
    context.deleteDatabase(dbname);
  }



  public boolean checkIfDatabaseExists(Context context, String dbName) {
    File dbFile = context.getDatabasePath(dbName);
    return dbFile.exists();
  }

  public void setCreateTableWithColumns(boolean createTableWithColumns) {
    this.createTableWithColumns = createTableWithColumns;
  }

  public ArrayList<String> getDatabaseNameList(SQLiteDatabase dbAux) {
    ArrayList<String> dbList = new ArrayList<>();

    String count = ".databases";
    Cursor mcursor = dbAux.rawQuery(count, null);
    if (mcursor.moveToFirst()) {
      Log.v("DATABASE", "" + mcursor.getString(0));
      dbList.add(mcursor.getString(0));
      while (mcursor.moveToNext()) {
        Log.v("DATABASE", "" + mcursor.getString(0));
        dbList.add(mcursor.getString(0));
      }
    }

    return dbList;
  }

  public int getTableColumnCount(SQLiteDatabase db, String tableName) {
    int columns = 0;
    String tableListStr = "PRAGMA table_info('" + tableName + "');";
    Cursor cursor = db.rawQuery(tableListStr, null);
    if (cursor.moveToFirst()) {
      columns++;
      while (cursor.moveToNext()) {
        columns++;
      }
    }
    return columns;
  }

  public void insertColumnType(SQLiteDatabase db, String tableName, String columnName,
      String columnType) {
    String query =
        "ALTER TABLE '" + tableName + "' ADD COLUMN '" + columnName + "' '" + columnType + "'";
    db.execSQL(query);
  }

  public void insertRowHm(SQLiteDatabase db, LinkedHashMap<EditText, String> hmEditTextColumn,
      String tableName) {

    String query = "INSERT INTO '" + tableName + "' VALUES (";

    EditText editTextAux;
    Iterator iterator = hmEditTextColumn.entrySet().iterator();
    boolean id = true;
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      String type = (String) entry.getValue();
      editTextAux = (EditText) entry.getKey();

      if (type.equals("INTEGER") && id && editTextAux.getText().toString().trim().equals("")) {
        query = query + "NULL" + ",";
        id = false;
      } else if (type.equals("INTEGER")) {
        query = query + editTextAux.getText().toString().trim() + ",";
      } else if (type.equals("TEXT")) {
        query = query + "'" + editTextAux.getText().toString().trim() + "',";
      } else if (type.equals("DATETIME")) {
        query = query + "CURRENT_TIMESTAMP ,";
      }
    }
    query = query.substring(0, query.length() - 1) + ");";

    try {
      db.execSQL(query);
    } catch (SQLiteConstraintException e) {
      Toast.makeText(context, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }
  }

  public void deleteRowFromTable(SQLiteDatabase db, String tableName, Object tag) {
    String deleteStr = "DELETE FROM " + tableName + " WHERE id=" + tag + " ";
    db.execSQL(deleteStr);
  }

  public void updateRowObj(SQLiteDatabase db, String tableName, ModelObj oldModelObj,
      ModelObj newModelObj) {

    ArrayList<String> alNewModel = newModelObj.getList();
    ArrayList<String> alOldModel = oldModelObj.getList();

    String updateStr = "UPDATE " + tableName + " set ";

    LinkedHashMap<String, String> columnNameType = getTableColumnNames(db, tableName);
    Iterator iterator = columnNameType.entrySet().iterator();
    int i = 1;
    iterator.next();
    while (iterator.hasNext()) {
      Map.Entry entry = (Map.Entry) iterator.next();
      String name = (String) entry.getKey();
      String type = (String) entry.getValue();

      updateStr = updateStr + name + "=";
      if (type.equals("TEXT")) {
        updateStr = updateStr + "'" + alNewModel.get(i) + "' ,";
      } else if (type.equals("DATETIME")) {
        updateStr = updateStr + " CURRENT_TIMESTAMP ,";
      } else {
        updateStr = updateStr + alNewModel.get(i) + " ,";
      }

      i++;
    }
    updateStr = updateStr.substring(0, updateStr.length() - 1);
    updateStr = updateStr + "WHERE id=" + alOldModel.get(0);
    Log.v("QUERY", "" + updateStr);
    db.execSQL(updateStr);
  }
}