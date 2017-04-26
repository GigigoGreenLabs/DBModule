package db.gigigo.com.dbmodule.dbsample;

import android.os.Build;
import com.gigigo.aprocesor.annotations.DataField;
import com.gigigo.aprocesor.annotations.DataTable;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmodule.BuildConfig;
import java.util.Objects;

/**
 * Created by nubor on 14/03/2017.
 */
@DataTable(alias = "Usuarios", isOnlyOneRecord = false) public class UsersModelv2
    extends DBTableMaster {
  public @DataField String Name;
  public @DataField String Surname;
  public @DataField String Age;

  //  String Name;
  //  String Surname;
  //  String Age;
  public UsersModelv2() {
  }

  public UsersModelv2(String name, String surname, String age) {
    this.Name = name;
    this.Surname = surname;
    this.Age = age;
  }

  public String getAge() {
    return Age;
  }

  public void setAge(String age) {
    Age = age;
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

  /*apt generated, una linea de megaHash por field , you can generate by android Studio*/
  @Override public int hashCode() {
    if (BuildConfig.VERSION_CODE < 19) {
      int megaHash = 0;
      megaHash = megaHash + hashCodeObject(this.getName());
      megaHash = megaHash + hashCodeObject(this.getSurname());
      megaHash = megaHash + hashCodeObject(this.getAge());
      return megaHash;
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        return Objects.hash(Name, Surname, Age);
      }
    }
    return -1;
  }
}
