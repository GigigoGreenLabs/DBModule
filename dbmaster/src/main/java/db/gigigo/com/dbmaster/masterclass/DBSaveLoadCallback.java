package db.gigigo.com.dbmaster.masterclass;

/**
 * Created by nubor on 07/03/2017.
 */
public interface DBSaveLoadCallback {
  /*this maybe inside callback, and  the engine do the calls*/
  void preSave();

  void postSave();

  void preLoad();

  void postLoad();
}
