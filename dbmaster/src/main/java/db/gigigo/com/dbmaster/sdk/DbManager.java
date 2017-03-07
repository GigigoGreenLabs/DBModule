package db.gigigo.com.dbmaster.sdk;

import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;

/**
 * Created by nubor on 03/03/2017.
 */
public abstract class DbManager {
  final DBEngineMaster mDBImplementation;
  public DbManager(DBEngineMaster implementation) {
    mDBImplementation=implementation;
  }

}
