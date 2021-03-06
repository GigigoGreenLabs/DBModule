package com.gigigo.dbsqliteimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import db.gigigo.com.dbmaster.schema.DBScheme;
import db.gigigo.com.dbmaster.schema.DBSchemeItem;
import db.gigigo.com.dbmaster.schema.DBTableFieldScheme;
import db.gigigo.com.dbmaster.schema.DBTableScheme;
import java.io.File;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Created by nubor on 03/03/2017.se
 */
public class DBEngineSQLLite extends DBEngineMaster {

  SqliteManager mSqliteManager;
  SQLiteDatabase sqLiteDatabase;

  public DBEngineSQLLite(Context context) {
    super(context);
    mSqliteManager = new SqliteManager(mContext, null);
  }

  @Override public void createDB(DBScheme dbMasterScheme) {
    System.out.println("+++++++++++++++create DB" + dbMasterScheme.getDbName());
    //aqui se crearia la bd de realm sql lite o lo q fuera y no se añadiria ningun scheme Item,
    //de añadir o tal se ocuparia el save del wrapper llamando a engineDb saveTableSchema
    //json nothing to do
    Log.v("DBNAME", "" + dbMasterScheme.getDbName());
    mSqliteManager = new SqliteManager(mContext, dbMasterScheme.getDbName());
    sqLiteDatabase = mSqliteManager.getWritableDatabase();
    Log.v("DBNAMEEXISTS",
        "" + mSqliteManager.checkIfDatabaseExists(this.mContext, dbMasterScheme.getDbName()));
    createDBScheme(dbMasterScheme);
  }

  public DBScheme loadDBScheme2() {
   /* System.out.println("***************** loadDBScheme" );
    String strFileName =  SCHEME_DB_FILE_NAME;

   return DataUtils.readSerializable(this.mContext,  strFileName);*/

    ArrayList<String> tableNameList;
    ArrayList<DBSchemeItem> columnList = new ArrayList<>();

    DBScheme db = new DBScheme();
    db.setDbName(SCHEME_DB_FILE_NAME);

    tableNameList = mSqliteManager.getTableList(sqLiteDatabase);
    DBSchemeItem dbSchemeItemAux;
    for (String tableName : tableNameList) {
      dbSchemeItemAux = new DBSchemeItem(tableName, "", "", System.currentTimeMillis());
      columnList.add(dbSchemeItemAux);
    }
    db.setLstSchemaItems(columnList);

    return db;
  }

  @Override public boolean isDBCreated(DBScheme dbMasterScheme) {
    Log.v("DATABASENAME", "" + dbMasterScheme.getDbName());

    if (mSqliteManager.checkIfDatabaseExists(this.mContext, dbMasterScheme.getDbName())){
      mSqliteManager = new SqliteManager(mContext, dbMasterScheme.getDbName());
      sqLiteDatabase = mSqliteManager.getWritableDatabase();
      return true;
    }else{
      return false;
    }
  }

  private String normalizetableName(String tableName) {

    if(tableName.indexOf("|")>-1)
    return tableName.substring(0, tableName.indexOf("|"));
    else
      return tableName;
  }

  @Override public boolean isTableCreated(String tableName) {
    //return DataUtils.isFileExists(tableName + SCHEME_TABLE_SUFFIX);
    tableName = normalizetableName(tableName);
    return mSqliteManager.checkIfTableExist(sqLiteDatabase, tableName);
  }

  @Override public void createDBScheme(DBScheme dbScheme) {
    System.out.println("***************** createDBScheme" + dbScheme.toString());
    String strFileName = SCHEME_DB_FILE_NAME;
    DataUtils.removeSerializable(this.mContext, strFileName);
    DataUtils.saveSerializable(this.mContext, dbScheme, strFileName);
  }

  @Override public DBScheme loadDBScheme() {
    System.out.println("***************** loadDBScheme");
    String strFileName = SCHEME_DB_FILE_NAME;
    DBScheme dbScheme = DataUtils.readSerializable(this.mContext, strFileName);
    if (dbScheme != null) {
      return dbScheme;
    } else {
      dbScheme = new DBScheme();
      dbScheme.setLstSchemaItems(new ArrayList<DBSchemeItem>());
      return dbScheme;
    }
  }

  @Override public void createTable(DBTableScheme table) {
    //System.out.println("*****************create DB Table" + table.getLstTableFields().size());
    //json nothing to do

    //1º read dbtable scheme
    //2º for con los rows de dicha tabla(especificacion de los campos, las columnas)
    //3º por cada loop, traducir el tipo java al tipo analogo de sqlite
    //lstFields_Testeo.add(new DBTableFieldScheme("java.lang.String", "Title"));
    //y utilizando el name del field para nombrar la columna

    if (!mSqliteManager.checkIfTableExist(sqLiteDatabase, table.getTableAlias())) {
      ArrayList<DBTableFieldScheme> arrayFields = new ArrayList<>();
      DBTableScheme dbTableScheme = this.loadTableSchema(table.getTableAlias());
      if (dbTableScheme != null && dbTableScheme.getLstTableFields() != null) {
        arrayFields = dbTableScheme.getLstTableFields();
      }

      String createTableStr = "CREATE TABLE " + table.getTableAlias() + "( ";

      for (int i = 0; i < arrayFields.size(); i++) {
        if (i != arrayFields.size() - 1) {
          createTableStr += arrayFields.get(i).getNameField() + " " + convertJavaType2SqliteType(
              arrayFields.get(i).getTypeDBField()) + ",";
        } else {
          createTableStr += arrayFields.get(i).getNameField() + " " + convertJavaType2SqliteType(
              arrayFields.get(i).getTypeDBField()) + ")";
        }
      }

      Log.v("STRING", "" + createTableStr);
      sqLiteDatabase.execSQL(createTableStr);
    }
  }

  @Override public void createTableScheme(DBTableScheme dbScheme) {
   /* System.out.println("*****************  createTableScheme" + dbScheme.toString());
    //esto crea un file llamado aliastable+Scheme.json que tiene la definicion de los campos que
    //esto guarda el file con el scheme y haría el createtable, si existiese deberia hacer droptable
    //create table con el array de campos q contiene el table*/

    String strFileName = dbScheme.getTableAlias() + SCHEME_TABLE_SUFFIX;
    DataUtils.removeSerializable(this.mContext, strFileName);
    DataUtils.saveSerializable(this.mContext, dbScheme, strFileName);
    //create table y tal*/
  }

  private String convertJavaType2SqliteType(String typeDBField) {
    if (typeDBField.equals("java.lang.String")) {
      return "TEXT";
    } else if (typeDBField.equals("java.lang.Integer")) {
      return "INTEGER";
    } else if (typeDBField.equals("java.lang.Float") || typeDBField.equals("java.lang.Double")) {
      return "REAL";
    } else if (typeDBField.equals("java.lang.Boolean")
        || typeDBField.equals("java.lang.Datetime")
        || typeDBField.equals("java.lang.Date")
        || typeDBField.equals("java.lang.Decimal")) {
      return "NUMERIC";
    } else if (typeDBField.equals("java.lang.Blob")) {
      return "BLOB";
    } else {
      return null;
    }
  }

  @Override public void clearTable(String tableAlias) {
    tableAlias = normalizetableName(tableAlias);
    mSqliteManager.clearTableContent(sqLiteDatabase, tableAlias);
  }

  public void saveTable_OLD(DBTableWrapperMaster tableWrapper, String tableAlias) {

    tableAlias = tableAlias.substring(0, tableAlias.indexOf("-"));
    ArrayList<DBTableMaster> itemsForInsertInSQL = new ArrayList<>();
    ArrayList<DBTableMaster> itemsForDeleteinSQL = new ArrayList<>();
    ArrayList<DBTableMaster> itemsForUpdateInSQL = new ArrayList<>();

    final ArrayList<? extends DBTableMaster> items = tableWrapper.getItems();
    String query = "Select * from " + tableAlias;

    Cursor mcursor = sqLiteDatabase.rawQuery(query, null);
    if (mcursor.getCount() > tableWrapper.getItems().size()) { //todo           insert
      if (mcursor.getCount() > 0) {
        int icount = mcursor.getInt(0);
        if (icount > 0) {
          for (int i = 1; i < tableWrapper.getItems().size(); i++)
            if (tableWrapper.getItems().get(i).equals(mcursor.getInt(i))) {

            } else {
              itemsForInsertInSQL.add(tableWrapper.getItems().get(i));
            }
        }
      } else {
        itemsForInsertInSQL.addAll(tableWrapper.getItems());
      }
      for (DBTableMaster dbTableMaster : itemsForInsertInSQL) {
        insertSQL(dbTableMaster, tableAlias);
      }
    } else if (mcursor.getCount() < tableWrapper.getItems().size()) {  //todo         delete
      if (mcursor.getCount() > 0) {
        int icount = mcursor.getInt(0);
        if (icount > 0) {
          for (int i = 1; i < tableWrapper.getItems().size(); i++)
            if (tableWrapper.getItems().get(i).equals(mcursor.getInt(i))) {

            } else {
              itemsForDeleteinSQL.add(tableWrapper.getItems().get(i));
            }
        }
      }
      for (DBTableMaster dbTableMaster : itemsForDeleteinSQL) {
        deleteSql(dbTableMaster, tableAlias);
      }
    } else if (mcursor.getCount() == tableWrapper.getItems().size()) { //todo          update
      if (mcursor.getCount() > 0) {
        int icount = mcursor.getInt(0);
        if (icount > 0) {
          for (int i = 1; i < tableWrapper.getItems().size(); i++)
            if (tableWrapper.getItems().get(i).equals(mcursor.getInt(i))) {

            } else {
              itemsForUpdateInSQL.add(tableWrapper.getItems().get(i));
            }
        }
      }
      for (DBTableMaster dbTableMaster : itemsForUpdateInSQL) {
        updateSql(dbTableMaster, tableAlias);
      }
    }






/*
    //region migration code
    DBScheme dbScheme = DataUtils.readSerializable(this.mContext, SCHEME_DB_FILE_NAME);
    ArrayList<DBSchemeItem> newSchemaItems = new ArrayList<>();
    boolean isTableSchemeAlreadyPresentInSchemeFile = false;
    DBMapperMaster myMapper = null;

    String inputClass = "";
    String outputClass = "";
    String strFileNameOldItems="";
    String Alias=tableAlias.replace(tableWrapper.HashCodeTableFields(),"");
    for (DBSchemeItem schemeItem : dbScheme.getLstSchemaItems()) {
      if ((schemeItem.getTableAlias()).equals(Alias)) {
        isTableSchemeAlreadyPresentInSchemeFile = true;
        //region migration code
        if (!schemeItem.getHashCodeTableFields().equals( tableWrapper.HashCodeTableFields())) {
          if (this.getMigrationMappers() != null) {
            //1º cargar los items del ese table alias con su hashcode(old)

             strFileNameOldItems=schemeItem.getTableAlias() + schemeItem.getHashCodeTableFields();
            ArrayList<? extends DBTableMaster> lstOldItems =
                loadItemsTable(strFileNameOldItems);

            inputClass = schemeItem.getModelClass();
            outputClass = tableWrapper.ModelClass();
            //2º buscar el mapper de la inputClla= item.modelClass y outputclass == a tableWrapper.getModelClass
            for (DBMapperMaster mapper : this.getMigrationMappers()) {
              if (mapper.getInputClass().equals(schemeItem.getModelClass())
                  && mapper.getOutputClass().equals(tableWrapper.ModelClass())) {
                myMapper = mapper;
                break;
              }
            }
            if (myMapper != null) {
              //3º recorrer los items del fichero oldç
              ArrayList arrayList = new ArrayList<>();
              for (DBTableMaster oldItem : lstOldItems) {
                System.out.println("+++++++++++++++MIGRATION+++++++++++++++"
                    + " \n\n\nFROM:" + myMapper.getInputClass()
                    + " \n\n\nTO:" + myMapper.getOutputClass()
                );
                //4º hacer el convert del item old y hacer el add en el tablewrapper.items
                arrayList.add(myMapper.convert(oldItem));
                //itemConverted.cast(tableWrapper.getItems().get(0).getClass()));
              }
              //asv esto no se si va funcionar
              tableWrapper.getItems().addAll(arrayList);
              //elimiamos el file con los registros antiguos
              DataUtils.removeSerializable(this.mContext,strFileNameOldItems);
              //5º eliminar el item de DBSchemeItem(creando una coleccion aux) ya que la migracion está completada
              //6º guardar el nuevo tablewrapper.items en su sitio y seguir con el bucle
            } else {
              throw new RuntimeException(
                  "NO mapper for convert " + inputClass + " into " + outputClass);
            }
          }
        }//endregion
        else {
          //update datetime in item //se hace al final, este else sobraria
          newSchemaItems.add(new DBSchemeItem(Alias, tableWrapper.ModelClass(),
              tableWrapper.HashCodeTableFields(), System.currentTimeMillis()));
        }
      } else {
        newSchemaItems.add(schemeItem); //this row is from another table, keep it
      }
    }
    //if is first time we save the table, create the row for table in scheme
    if (!isTableSchemeAlreadyPresentInSchemeFile) {
      newSchemaItems.add(new DBSchemeItem(Alias, tableWrapper.ModelClass(),
          tableWrapper.HashCodeTableFields(), System.currentTimeMillis()));
    }
    //save scheme, with all updates
    dbScheme.setLstSchemaItems(newSchemaItems);
    DataUtils.saveSerializable(this.mContext, dbScheme, SCHEME_DB_FILE_NAME);
    //endregion

    //save the items of tablewrapper
    DataUtils.saveSerializable(this.mContext, tableWrapper.getItems(), tableAlias);
    System.out.println("*****************saveTable" + tableAlias);*/
  }

  @Override public void saveTable(DBTableWrapperMaster tableWrapper, String tableAlias) {

    //tableAlias = tableAlias.substring(0, tableAlias.indexOf("-"));
    tableAlias = normalizetableName(tableAlias);
    ArrayList<Object> itemsForInsertInSQL = tableWrapper.itemsForInsert;
    ArrayList<Object> itemsForDeleteinSQL = tableWrapper.itemsForDelete;
    ArrayList<Object> itemsForUpdateInSQL = tableWrapper.itemsForUpdate;

    for (Object item : itemsForInsertInSQL) {
      // if(item instanceof DBTableMaster)
      Log.v("INSERTITEM", "" + item.toString());
      insertSQL(item, tableAlias);
    }

    for (Object item : itemsForUpdateInSQL) {
      // if(item instanceof DBTableMaster)
      updateSql(item, tableAlias);
    }

    for (Object item : itemsForDeleteinSQL) {
      // if(item instanceof DBTableMaster)
      deleteSql(item, tableAlias);
    }

    //fixme asv, queda hacer la migracion, pero para ello debe funcionar

  }
/*
    //region migration code
    DBScheme dbScheme = DataUtils.readSerializable(this.mContext, SCHEME_DB_FILE_NAME);
    ArrayList<DBSchemeItem> newSchemaItems = new ArrayList<>();
    boolean isTableSchemeAlreadyPresentInSchemeFile = false;
    DBMapperMaster myMapper = null;

    String inputClass = "";
    String outputClass = "";
    String strFileNameOldItems="";
    String Alias=tableAlias.replace(tableWrapper.HashCodeTableFields(),"");
    for (DBSchemeItem schemeItem : dbScheme.getLstSchemaItems()) {
      if ((schemeItem.getTableAlias()).equals(Alias)) {
        isTableSchemeAlreadyPresentInSchemeFile = true;
        //region migration code
        if (!schemeItem.getHashCodeTableFields().equals( tableWrapper.HashCodeTableFields())) {
          if (this.getMigrationMappers() != null) {
            //1º cargar los items del ese table alias con su hashcode(old)

             strFileNameOldItems=schemeItem.getTableAlias() + schemeItem.getHashCodeTableFields();
            ArrayList<? extends DBTableMaster> lstOldItems =
                loadItemsTable(strFileNameOldItems);

            inputClass = schemeItem.getModelClass();
            outputClass = tableWrapper.ModelClass();
            //2º buscar el mapper de la inputClla= item.modelClass y outputclass == a tableWrapper.getModelClass
            for (DBMapperMaster mapper : this.getMigrationMappers()) {
              if (mapper.getInputClass().equals(schemeItem.getModelClass())
                  && mapper.getOutputClass().equals(tableWrapper.ModelClass())) {
                myMapper = mapper;
                break;
              }
            }
            if (myMapper != null) {
              //3º recorrer los items del fichero oldç
              ArrayList arrayList = new ArrayList<>();
              for (DBTableMaster oldItem : lstOldItems) {
                System.out.println("+++++++++++++++MIGRATION+++++++++++++++"
                    + " \n\n\nFROM:" + myMapper.getInputClass()
                    + " \n\n\nTO:" + myMapper.getOutputClass()
                );
                //4º hacer el convert del item old y hacer el add en el tablewrapper.items
                arrayList.add(myMapper.convert(oldItem));
                //itemConverted.cast(tableWrapper.getItems().get(0).getClass()));
              }
              //asv esto no se si va funcionar
              tableWrapper.getItems().addAll(arrayList);
              //elimiamos el file con los registros antiguos
              DataUtils.removeSerializable(this.mContext,strFileNameOldItems);
              //5º eliminar el item de DBSchemeItem(creando una coleccion aux) ya que la migracion está completada
              //6º guardar el nuevo tablewrapper.items en su sitio y seguir con el bucle
            } else {
              throw new RuntimeException(
                  "NO mapper for convert " + inputClass + " into " + outputClass);
            }
          }
        }//endregion
        else {
          //update datetime in item //se hace al final, este else sobraria
          newSchemaItems.add(new DBSchemeItem(Alias, tableWrapper.ModelClass(),
              tableWrapper.HashCodeTableFields(), System.currentTimeMillis()));
        }
      } else {
        newSchemaItems.add(schemeItem); //this row is from another table, keep it
      }
    }
    //if is first time we save the table, create the row for table in scheme
    if (!isTableSchemeAlreadyPresentInSchemeFile) {
      newSchemaItems.add(new DBSchemeItem(Alias, tableWrapper.ModelClass(),
          tableWrapper.HashCodeTableFields(), System.currentTimeMillis()));
    }
    //save scheme, with all updates
    dbScheme.setLstSchemaItems(newSchemaItems);
    DataUtils.saveSerializable(this.mContext, dbScheme, SCHEME_DB_FILE_NAME);
    //endregion

    //save the items of tablewrapper
    DataUtils.saveSerializable(this.mContext, tableWrapper.getItems(), tableAlias);
    System.out.println("*****************saveTable" + tableAlias);
}*/

  private void updateSql(Object dbTableMaster, String tableAlias) {
    tableAlias = normalizetableName(tableAlias);
    final DBTableScheme dbTableScheme = loadTableSchema(tableAlias);
    ContentValues contentValues = new ContentValues();

    Class<?> clazz = dbTableMaster.getClass();

    for (DBTableFieldScheme dbTableFieldScheme : dbTableScheme.getLstTableFields()) {
      try {
        Log.v("NAMEFIELD", "" + dbTableFieldScheme.getNameField());
        Field field1 = clazz.getDeclaredField(dbTableFieldScheme.getNameField());

        Object strValue = (Object) field1.get(dbTableMaster);

        if (field1.getType().isAssignableFrom(String.class)) {
          //contentValues.remove(String.valueOf(dbTableFieldScheme));
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  private void deleteSql(Object dbTableMaster, String tableAlias) {
    tableAlias = normalizetableName(tableAlias);
    final DBTableScheme dbTableScheme = loadTableSchema(tableAlias);
    ContentValues contentValues = new ContentValues();

    Class<?> clazz = dbTableMaster.getClass();

    for (DBTableFieldScheme dbTableFieldScheme : dbTableScheme.getLstTableFields()) {
      try {
        Log.v("NAMEFIELD", "" + dbTableFieldScheme.getNameField());
        Field field1 = clazz.getDeclaredField(dbTableFieldScheme.getNameField());

        if (field1.getType().isAssignableFrom(String.class)) {
          contentValues.remove(tableAlias);
        }
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    // sqLiteDatabase.delete(tableAlias, dbTableFieldScheme.getNameField());
  }

  private void insertSQL(Object dbTableMaster, String tableAlias) {
    tableAlias = normalizetableName(tableAlias);
    final DBTableScheme dbTableScheme = loadTableSchema(tableAlias);
    ContentValues contentValues = new ContentValues();
    Class<?> clazz = dbTableMaster.getClass();
    ArrayList<DBTableFieldScheme> dbTableFieldScheme = dbTableScheme.getLstTableFields();

    for (DBTableFieldScheme tableFieldScheme : dbTableFieldScheme) {
      try {
        Log.v("NAMEFIELD", "" + tableFieldScheme.getNameField());
        Field field1 = clazz.getDeclaredField(tableFieldScheme.getNameField());

        Object strValue = (Object) field1.get(dbTableMaster);

        if (field1.getType().isAssignableFrom(String.class)) {
          contentValues.put(tableFieldScheme.getNameField(), strValue.toString());
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    sqLiteDatabase.insert(tableAlias, null, contentValues);
  }

  @Override public ArrayList<? extends DBTableMaster> loadItemsTable(String tableAlias) {
    System.out.println("*****************loadItemsTable" + tableAlias);
    tableAlias = normalizetableName(tableAlias);
    final File file1 = mSqliteManager.loadDatabaseAsXml(tableAlias, sqLiteDatabase);
    String xmlStrign = mSqliteManager.readFromFile(file1);
    ArrayList<? extends DBTableMaster> arrayList = new ArrayList<>();
    // todo serializar xml
    try {
      JSONObject jsonObject = XML.toJSONObject(xmlStrign);

      JSONObject arrayUsers = jsonObject;//jsonObject.getJSONObject("col");
      String jsonPrettyPrintString = jsonObject.toString();
      System.out.println(jsonPrettyPrintString);
      Reader reader = new StringReader(arrayUsers.toString());
      JsonElement elem = new JsonParser().parse(reader);
      Gson gson = new GsonBuilder().create();

     // data d = gson.fromJson(jsonObject.toString(), data.class);

    /*  data database = new data();

      String databaseName = String.valueOf(jsonObject.getJSONObject("database").get("dataname"));
      String tableName = String.valueOf(jsonObject.getJSONObject("database").getJSONObject("table").get("tablename"));*/

      ArrayList<UsersModelproxy> arrayListUsersModelproxy = new ArrayList<>();

      arrayListUsersModelproxy = gson.fromJson(
          String.valueOf(jsonObject.getJSONObject("database").getJSONObject("table").getJSONArray("row")),arrayListUsersModelproxy.getClass());



      UsersModelproxy[] ususarios = new UsersModelproxy[]{};



      arrayList = gson.fromJson(
          String.valueOf(jsonObject.getJSONObject("database").getJSONObject("table").getJSONArray("row")),arrayList.getClass());

      Object[] objAux = new Object[]{};

      objAux = gson.fromJson(
          String.valueOf(jsonObject.getJSONObject("database").getJSONObject("table").getJSONArray("row")),objAux.getClass());


 ususarios = gson.fromJson(
    String.valueOf(jsonObject.getJSONObject("database").getJSONObject("table").getJSONArray("row")),ususarios.getClass());



    } catch (JSONException je) {
      System.out.println(je.toString());
    }

    return  arrayList;

    /*
    ArrayList<? extends DBTableMaster> arrayList =
        mSqliteManager.loadObjectListDBTableMaster(sqLiteDatabase, tableAlias);
    if (arrayList == null) arrayList = new ArrayList<>();
    return arrayList;
    */

    //mSqliteManager.readFromFile(file);

    //return new ArrayList<>();
  }

  //region Schema fields Table

  @Override public DBTableScheme loadTableSchema(String tableAlias) {
    tableAlias = normalizetableName(tableAlias);
    String strFileName = tableAlias + SCHEME_TABLE_SUFFIX;
    //System.out.println("*****************loadTableSchema" + tableAlias + HashCodeDBFields);
    return DataUtils.readSerializable(mContext, strFileName);
  }

  //endregion
}



class data implements Serializable{
  @SerializedName("dataname")
  private String dataname;

  @SerializedName("tablename")
  private String tablename;

  @SerializedName("row")
  private ArrayList <Object> row;

  public String getDataname() {
    return dataname;
  }

  public void setDataname(String dataname) {
    this.dataname = dataname;
  }

  public String getTablename() {
    return tablename;
  }

  public void setTablename(String tablename) {
    this.tablename = tablename;
  }

  public ArrayList<Object> getRow() {
    return row;
  }

  public void setRow(ArrayList<Object> row) {
    this.row = row;
  }
}
