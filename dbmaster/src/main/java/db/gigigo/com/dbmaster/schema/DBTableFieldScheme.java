package db.gigigo.com.dbmaster.schema;

import java.io.Serializable;

/**
 * Created by nubor on 10/03/2017.
 */
public class DBTableFieldScheme implements Serializable {
  //todo for the future, maybe with engines implementations by SQL we need
  //set atributtes to the primary_key, foregn_key, not null, default value

  boolean isPrimaryKey;

  boolean isForeignKey;

  boolean isNotNull;

  String defaultValueForField;

  /*Now use only*/
  String typeDBField;      //the value will be string, int or whatever the engine must to convert the java type to engine type, java String --> sqllite text
  String nameField;

  public DBTableFieldScheme(String typeDBField, String nameField) {
    this.typeDBField = typeDBField;
    this.nameField = nameField;
  }

  public boolean isPrimaryKey() {
    return isPrimaryKey;
  }

  public void setPrimaryKey(boolean primaryKey) {
    isPrimaryKey = primaryKey;
  }

  public boolean isForeignKey() {
    return isForeignKey;
  }

  public void setForeignKey(boolean foreignKey) {
    isForeignKey = foreignKey;
  }

  public boolean isNotNull() {
    return isNotNull;
  }

  public void setNotNull(boolean notNull) {
    isNotNull = notNull;
  }

  public String getDefaultValueForField() {
    return defaultValueForField;
  }

  public void setDefaultValueForField(String defaultValueForField) {
    this.defaultValueForField = defaultValueForField;
  }

  public String getTypeDBField() {
    return typeDBField;
  }

  public void setTypeDBField(String typeDBField) {
    this.typeDBField = typeDBField;
  }

  public String getNameField() {
    return nameField;
  }

  public void setNameField(String nameField) {
    this.nameField = nameField;
  }
}
