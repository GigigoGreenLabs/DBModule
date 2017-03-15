package db.gigigo.com.dbmodule.dbsample;

import android.os.Build;
import com.gigigo.aprocesor.annotations.DataField;
import com.gigigo.aprocesor.annotations.DataTable;
import db.gigigo.com.dbmaster.BuildConfig;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by nubor on 06/03/2017.
 */
//@DataTable(alias = "Usuarios",isOnlyOneRecord = false)
public class UsersModel extends DBTableMaster {
   String Name;
   String Surname;
   String id;


  //@DataField String Name;
  //@DataField String Surname;
  //@DataField String id;
  public UsersModel() {
  }

  public UsersModel(String name, String surname, String id) {
    this.Name = name;
    this.Surname = surname;
    this.id = id;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getSurname() {
    return Surname;
  }

  public void setSurname(String surname) {
    Surname = surname;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /*apt generated, una linea de megaHash por field , you can generate by android Studio*/
  @Override public int hashCode() {
    if (BuildConfig.VERSION_CODE < 19) {
      int megaHash = 0;
      megaHash = megaHash + hashCodeObject(this.getName());
      megaHash = megaHash + hashCodeObject(this.getSurname());
      megaHash = megaHash + hashCodeObject(this.getId());
      return megaHash;
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        return Objects.hash(Name, Surname, id);
      }
    }
    return -1;
  }
}

