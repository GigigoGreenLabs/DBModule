package com.gigigo.dbsqliteimpl;

import android.os.Build;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import java.util.Objects;

/**
 * Created by nubor on 14/03/2017.
 */
@Deprecated //asv esto solo es para comprobar el casteo al usermodelv2 para ver si es necesario add el parametro al engine.loaditems
public class UsersModelproxy extends DBTableMaster {
  public String Name;
  public String Surname;
  public String Age;

  //  String Name;
  //  String Surname;
  //  String Age;
  public UsersModelproxy() {
  }

  public UsersModelproxy(String name, String surname, String age) {
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
