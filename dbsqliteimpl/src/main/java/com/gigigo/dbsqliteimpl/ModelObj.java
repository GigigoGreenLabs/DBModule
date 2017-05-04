package com.gigigo.dbsqliteimpl;

import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import java.util.ArrayList;
import java.util.List;

public class ModelObj extends DBTableMaster {

  private List<String> list;

  public ArrayList<String> getList() {
    return (ArrayList<String>) list;
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
