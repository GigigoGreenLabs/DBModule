package com.gigigo.dbsqliteimpl;

import java.util.ArrayList;



public class ModelObj {

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
}
