package db.gigigo.com.dbmaster.masterclass;

import android.content.Context;
import db.gigigo.com.dbmaster.schema.DBScheme;
import db.gigigo.com.dbmaster.schema.DBTableScheme;
import java.util.ArrayList;

/**
 * Created by nubor on 03/03/2017.
 *
 * a esta clase es a la q hara referencia el apt, a través del Db Manager de forma
 * q el app redefina esta clase mediante una implemnetacion
 */
public abstract class DBEngineMaster {

  public final String SCHEME_TABLE_SUFFIX = "_SCHEME";
  public final String SCHEME_DB_FILE_NAME = "myDBSCHEME";
  public final Context mContext;

  public DBEngineMaster(Context context) {
    this.mContext = context;
  }

  //region Db creation
  public abstract boolean isDBCreated(DBScheme dbMasterScheme);

  public abstract void createDB(DBScheme dbMasterScheme);
  //endregion
  //region table creation/clear
  public abstract boolean isTableCreated(String tableName);

  public abstract void createTable(DBTableScheme table);

  public abstract void clearTable(String tableAlias);
  //endregion
  //region save/load table items
  public abstract void saveTable(DBTableWrapperMaster table, String tableAlias);

  public abstract ArrayList<? extends DBTableMaster> loadItemsTable(String tableAliasWithHashCodeOfField);
  //endregion
  //region scheme
  //table(fields/type)
  public abstract void createTableScheme(DBTableScheme dbScheme);

  public abstract DBTableScheme loadTableSchema(String tableAlias);//esto devuelve los campos de la tabla(nombre/tipo)
  //database
  public abstract void createDBScheme(DBScheme dbScheme);

  public abstract DBScheme loadDBScheme();
  //endregion
  //region mappers
  ArrayList<DBMapperMaster> mapperMasters;

  public void setMigrationMappers(DBMapperMaster... mappers) {
    if (mapperMasters == null) mapperMasters = new ArrayList<>();

    for (DBMapperMaster mapper : mappers) {
      mapperMasters.add(mapper);
    }
  }

  public ArrayList<DBMapperMaster> getMigrationMappers() {
    return mapperMasters;
  }
  //endregion
}
