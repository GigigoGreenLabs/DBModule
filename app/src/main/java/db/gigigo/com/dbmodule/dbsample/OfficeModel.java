package db.gigigo.com.dbmodule.dbsample;

import com.gigigo.aprocesor.annotations.DataField;
import com.gigigo.aprocesor.annotations.DataTable;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;

/**
 * Created by nubor on 15/03/2017.
 */
@DataTable(alias="Ofis",isOnlyOneRecord = false)
public class OfficeModel extends DBTableMaster {

  @DataField String Nombre;

  public String getNombre() {
    return Nombre;
  }

  public void setNombre(String nombre) {
    Nombre = nombre;
  }

  @Override public int hashCode() {
    return 12343;
  }
}
