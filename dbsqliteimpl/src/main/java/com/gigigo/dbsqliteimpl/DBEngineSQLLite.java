package com.gigigo.dbsqliteimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBMapperMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import db.gigigo.com.dbmaster.schema.DBScheme;
import db.gigigo.com.dbmaster.schema.DBSchemeItem;
import db.gigigo.com.dbmaster.schema.DBTableFieldScheme;
import db.gigigo.com.dbmaster.schema.DBTableScheme;
import java.util.ArrayList;

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
  }

  public DBScheme loadDBScheme2() {
   /* System.out.println("***************** loadDBScheme" );
    String strFileName =  SCHEME_DB_FILE_NAME;

   return DataUtils.readSerializable(this.mContext,  strFileName);*/

    ArrayList<String> tableNameList = new ArrayList<String>();
    ArrayList<DBSchemeItem> columnList = new ArrayList<DBSchemeItem>();

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
    mSqliteManager = new SqliteManager(mContext, dbMasterScheme.getDbName());
    sqLiteDatabase = mSqliteManager.getWritableDatabase();
    return mSqliteManager.checkIfDatabaseExists(this.mContext, dbMasterScheme.getDbName());
  }

  @Override public boolean isDBTableCreated(String tableName) {
    //return DataUtils.isFileExists(tableName + SCHEME_TABLE_SUFFIX);
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

    return DataUtils.readSerializable(this.mContext, strFileName);
  }

  @Override public void createDBTable(DBTableScheme table) {
    //System.out.println("*****************create DB Table" + table.getLstTableFields().size());
    //json nothing to do

    //1º read dbtable scheme
    //2º for con los rows de dicha tabla(especificacion de los campos, las columnas)
    //3º por cada loop, traducir el tipo java al tipo analogo de sqlite
    //lstFields_Testeo.add(new DBTableFieldScheme("java.lang.String", "Title"));
    //y utilizando el name del field para nombrar la columna

    if (!mSqliteManager.checkIfTableExist(sqLiteDatabase,table.getTableAlias())){
      ArrayList<DBTableFieldScheme> arrayFields = new ArrayList<>();
      DBTableScheme dbTableScheme = this.loadTableSchema(table.getTableAlias(), "");
      if (dbTableScheme != null && dbTableScheme.getLstTableFields() != null) {
        arrayFields = dbTableScheme.getLstTableFields();
      }

      String createTableStr = "CREATE TABLE " + table.getTableAlias()+ "( ";

      for (int i = 0; i < arrayFields.size(); i++) {

        if (i != arrayFields.size()-1){
          createTableStr +=
              arrayFields.get(i).getNameField() + " " + convertJavaType2SqliteType(arrayFields.get(i).getTypeDBField())+ ",";
        }else{
          createTableStr +=
              arrayFields.get(i).getNameField() + " " + convertJavaType2SqliteType(arrayFields.get(i).getTypeDBField())+ "," + ")";

        }
      }

      Log.v("STRING",""+ createTableStr);
      sqLiteDatabase.execSQL(createTableStr);

    }


    //todo ejecutar la senctencia SQL de create table en la   SQLiteDatabase sqLiteDatabase;

  }

  private String convertJavaType2SqliteType(String typeDBField) {

    if (typeDBField.equals("java.lang.String")) {
      return "TEXT";
    } else {
      return null;
    }
  }

  @Override public void clearDBTable(String tableAlias) {
    /*System.out.println("*****************clearDBTable" + tableAlias);
    //this must be delete all items  DBTable  with the name alias, and all the hashcodeDBFields
    DBScheme dbScheme = DataUtils.readSerializable(this.mContext, SCHEME_DB_FILE_NAME);
    ArrayList<DBSchemeItem> newSchemaItems = new ArrayList<>();

    for (DBSchemeItem item : dbScheme.getLstSchemaItems()) {
      if (item.getTableAlias().equals(tableAlias)) {//delete all versions of this tablealias
        DataUtils.removeSerializable(this.mContext,
            item.getTableAlias() + item.getHashCodeTableFields());
      } else {
        newSchemaItems.add(item);
      }
    }
    dbScheme.setLstSchemaItems(newSchemaItems);
    DataUtils.saveSerializable(this.mContext, dbScheme, SCHEME_DB_FILE_NAME);*/
  }

  @Override public void saveTable(DBTableWrapperMaster tableWrapper, String tableAlias) {
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

  @Override public ArrayList<? extends DBTableMaster> loadItemsTable(String tableAlias) {
    /*System.out.println("*****************loadItemsTable" + tableAlias);
    ArrayList<? extends DBTableMaster> arrayList =
        DataUtils.readSerializable(this.mContext, tableAlias);
    if (arrayList == null) arrayList = new ArrayList<>();
    return arrayList;*/
    return new ArrayList<>();
  }

  @Override public void clearTable(String tableAlias) {
    //System.out.println("*****************clearTable" + tableAlias);
  }
  //region Schema fields Table

  @Override public DBTableScheme getDBTableSchema(String tableAlias) {
   /* System.out.println("*****************clearDBTable" + tableAlias);
    //this method maybe used by engines sql, or not need it because the creation will be
    //by createDBTable, but maybe for create the inserts/update

    String strFileName = tableAlias + SCHEME_TABLE_SUFFIX;
    DBTableScheme myTableScheme = DataUtils.readSerializable(this.mContext, strFileName);

    return myTableScheme;*/
    return new DBTableScheme("tabla");
  }

  @Override public void createDBTableScheme(DBTableScheme dbScheme) {
   /* System.out.println("*****************  createDBTableScheme" + dbScheme.toString());
    //esto crea un file llamado aliastable+Scheme.json que tiene la definicion de los campos que
    //esto guarda el file con el scheme y haría el createtable, si existiese deberia hacer droptable
    //create table con el array de campos q contiene el table*/
    String strFileName = dbScheme.getTableAlias() + SCHEME_TABLE_SUFFIX;
    DataUtils.removeSerializable(this.mContext, strFileName);
    DataUtils.saveSerializable(this.mContext, dbScheme, strFileName);
    //create table y tal*/
  }

  /*ESTOS DOS METODOS DE MOMENTO NO SE UTILIZAN, el load no se si el esl get o q es*/
  @Override public void saveTableSchema(String tableAlias, String HashCodeDBFields) {
    //System.out.println("*****************saveTableSchema" + tableAlias + HashCodeDBFields);
    //  String strFileName = dbScheme.getTableAlias() + SCHEME_TABLE_SUFFIX;
  }

  @Override public DBTableScheme loadTableSchema(String tableAlias, String HashCodeDBFields) {
    String strFileName = tableAlias + SCHEME_TABLE_SUFFIX;
    //System.out.println("*****************loadTableSchema" + tableAlias + HashCodeDBFields);
    return DataUtils.readSerializable(mContext, strFileName);
  }
  //endregion
}
