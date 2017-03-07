package db.gigigo.com.dbmaster.masterclass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nubor on 03/03/2017.
 */
public abstract class DBMaster {
  public static DBEngineMaster mDBEngine;

  protected DBMaster(DBEngineMaster mDBEngine) {
    this.mDBEngine = mDBEngine;
  }

  //public void createDB(){};
  //public void clearDB(){};
  //public List<DBTableMaster> getDBTables(){ return new ArrayList<>();};
  //public void setDBTables(List<DBTableMaster> tables){ };
}
