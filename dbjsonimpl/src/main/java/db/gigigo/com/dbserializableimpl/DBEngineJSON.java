package db.gigigo.com.dbserializableimpl;

import android.content.Context;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBFieldMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nubor on 03/03/2017.se
 */
public class DBEngineJSON extends DBEngineMaster {

  public DBEngineJSON(Context context) {
    super(context);
  }

  @Override public void createDB(Class<? extends DBTableMaster> dbMaster) {

  }

  @Override public void createDBTable(Class<? extends DBTableMaster> table) {

  }

  @Override public void clearDBTable(Class<? extends DBTableMaster> table) {

  }

  @Override public ArrayList<DBFieldMaster> getDBFields(String tableAlias) {
    return null;
  }

  @Override
  public void setDBFields(List<DBFieldMaster> fields, Class<? extends DBTableMaster> table) {

  }

  @Override public void saveTable(DBTableWrapperMaster table, String alias) {
    DataUtils.saveSerializable(this.mContext, table.getItems(), alias);
  }

  @Override public ArrayList<? extends DBTableMaster> loadItemsTable(String tableAlias) {
    ArrayList<? extends DBTableMaster> arrayList =
        DataUtils.readSerializable(this.mContext, tableAlias);
    if (arrayList == null) arrayList = new ArrayList<>();
    return arrayList;
  }

  @Override public void clearTable(String tableAlias) {

  }
}
