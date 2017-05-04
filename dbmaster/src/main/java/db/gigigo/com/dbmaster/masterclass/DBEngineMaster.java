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

  public abstract void createDB(DBScheme dbMasterScheme);

  public abstract boolean isDBCreated(DBScheme dbMasterScheme);

  public abstract void createDBTable(DBTableScheme table);

  public abstract boolean isDBTableCreated(String tableName);

  public abstract void clearDBTable(String tableAlias);

  public abstract DBTableScheme getDBTableSchema(String tableAlias);//esto devuelve los campos

  public abstract void saveTable(DBTableWrapperMaster table, String tableAlias);

  public abstract ArrayList<? extends DBTableMaster> loadItemsTable(
      String tableAliasWithHashCodeOfField);

  @Deprecated
  //asv el clearTable no se si se utiliza, ya q está dentro del propio wrapper de cada table, esto tal vez deberia ser un clearDB y q vacie la DB completa de registro
  public abstract void clearTable(String tableAlias);/*elimina los registros xo no el fichero*/

  public abstract void createDBScheme(DBScheme dbScheme);

  public abstract DBScheme loadDBScheme();

  public abstract void createDBTableScheme(DBTableScheme dbScheme);

  public abstract void saveTableSchema(String tableAlias,
      String HashCodeDBFields);//si no existe el fichero lo crea

  public abstract DBTableScheme loadTableSchema(String tableAlias, String HashCodeDBFields);

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


}
