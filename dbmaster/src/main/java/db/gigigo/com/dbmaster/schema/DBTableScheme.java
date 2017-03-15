package db.gigigo.com.dbmaster.schema;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nubor on 10/03/2017.
 */
public class DBTableScheme implements Serializable {

  final String  tableAlias;
  ArrayList<DBTableFieldScheme> lstTableFields;

  public DBTableScheme(String tableAlias) {
    this.tableAlias = tableAlias;
  }

  public DBTableScheme(String tableAlias, ArrayList<DBTableFieldScheme> lstTableFields) {
    this.tableAlias = tableAlias;
    this.lstTableFields = lstTableFields;
  }

  public String getTableAlias() {
    return tableAlias;
  }

  public ArrayList<DBTableFieldScheme> getLstTableFields() {
    return lstTableFields;
  }

  public void setLstTableFields(ArrayList<DBTableFieldScheme> lstTableFields) {
    this.lstTableFields = lstTableFields;
  }
}
