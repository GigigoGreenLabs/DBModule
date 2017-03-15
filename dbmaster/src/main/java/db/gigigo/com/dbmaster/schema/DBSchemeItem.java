package db.gigigo.com.dbmaster.schema;

import java.io.Serializable;

/**
 * Created by nubor on 09/03/2017.
 */
public class DBSchemeItem implements Serializable {
  String tableAlias;
  String modelClass;
  String hashCodeTableFields;
  long dateTableLastSave;

  public DBSchemeItem(String tableAlias, String modelClass, String hashCodeTableFields,
      long dateTableLastSave) {
    this.tableAlias = tableAlias;
    this.modelClass = modelClass;
    this.hashCodeTableFields = hashCodeTableFields;
    this.dateTableLastSave = dateTableLastSave;
  }

  public String getTableAlias() {
    return tableAlias;
  }

  public void setTableAlias(String tableAlias) {
    this.tableAlias = tableAlias;
  }

  public String getModelClass() {
    return modelClass;
  }

  public void setModelClass(String modelClass) {
    this.modelClass = modelClass;
  }

  public String getHashCodeTableFields() {
    return hashCodeTableFields;
  }

  public void setHashCodeTableFields(String hashCodeTableFields) {
    this.hashCodeTableFields = hashCodeTableFields;
  }

  public long getDateTableLastSave() {
    return dateTableLastSave;
  }

  public void setDateTableLastSave(long dateTableLastSave) {
    this.dateTableLastSave = dateTableLastSave;
  }
}
