package com.gigigo.gigigocrud_sqliteandroid.Objects;

import android.databinding.BaseObservable;

/**
 * Created by pablo.rojas on 17/5/17.
 */

public class Bind extends BaseObservable{

  private String databaseTitle;

  public Bind(String tittle) {
    this.databaseTitle = tittle;
  }

  public String getDatabaseTitle() {
    return databaseTitle;
  }

  public void setDatabaseTitle(String databaseTitle) {
    this.databaseTitle = databaseTitle;
  }
}
