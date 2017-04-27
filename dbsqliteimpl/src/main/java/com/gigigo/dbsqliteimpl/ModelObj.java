package com.gigigo.dbsqliteimpl;

import com.gigigo.aprocesor.annotations.DataField;
import com.gigigo.aprocesor.annotations.DataTable;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import java.io.Serializable;
import java.util.ArrayList;

public class ModelObj extends DBTableMaster {

  private ArrayList<String> list;

  public ArrayList<String> getList() {
    return list;
  }

  public void setList(ArrayList<String> list) {
    this.list = list;
  }

  public ModelObj() {
    list = new ArrayList<>();
  }

  @Override public int hashCode() {
    return 0;
  }

}
