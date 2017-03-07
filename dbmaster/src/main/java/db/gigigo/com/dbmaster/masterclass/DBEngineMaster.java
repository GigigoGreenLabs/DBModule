package db.gigigo.com.dbmaster.masterclass;

import android.content.Context;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nubor on 03/03/2017.
 *
 * a esta clase es a la q hara referencia el apt, a través del Db Manager de forma
 * q el app redefina esta clase mediante una implemnetacion
 */
public abstract class DBEngineMaster {
public final Context mContext;
  public DBEngineMaster(Context context) {
    this.mContext=context;
  }
  public abstract void createDB(Class<? extends DBTableMaster> dbMaster);
  public abstract void createDBTable(Class<? extends DBTableMaster> table);
  public abstract void clearDBTable(Class<? extends DBTableMaster> table);
  public abstract ArrayList<DBFieldMaster> getDBFields(String tableAlias);
  public abstract void setDBFields(List<DBFieldMaster> fields, Class<? extends DBTableMaster> table);
  public abstract void saveTable(DBTableWrapperMaster table,String Alias);
  public abstract ArrayList<? extends DBTableMaster> loadItemsTable(String tableAlias) ;
  public abstract void clearTable(String tableAlias);/*elimina los registros xo no el fichero*/
}
