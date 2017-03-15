package db.gigigo.com.dbmaster.schema;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nubor on 09/03/2017.
 */
public class DBScheme implements Serializable {


  String dbName;
  //asv maybe DBname
  ArrayList<DBSchemeItem> lstSchemaItems;

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public ArrayList<DBSchemeItem> getLstSchemaItems() {
    return lstSchemaItems;
  }

  public void setLstSchemaItems(ArrayList<DBSchemeItem> lstSchemaItems) {
    this.lstSchemaItems = lstSchemaItems;
  }
}
