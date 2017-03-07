package db.gigigo.com.dbmaster.masterclass;

import com.wagnerandade.coollection.query.Query;
import java.util.ArrayList;

/**
 * Created by nubor on 06/03/2017.
 */
public abstract class DBTableWrapperMaster {
  public final DBEngineMaster mDBEngine;
  public final DBTableMaster mDBTable;
  public final String mAlias;
  public DBSaveLoadCallback mDBSaveLoadCallback = null;

  protected DBTableWrapperMaster(DBTableMaster table, DBEngineMaster mDBEngine, String alias) {
    this.mDBEngine = mDBEngine;
    this.mDBTable = table;
    this.mAlias = alias;
  }

  public void setmDBSaveLoadCallback(DBSaveLoadCallback mDBSaveLoadCallback) {
    this.mDBSaveLoadCallback = mDBSaveLoadCallback;
  }

  public abstract ArrayList<? extends DBTableMaster> getItems();

  public abstract void setItems(ArrayList<? extends DBTableMaster> items);

  public abstract void delItem(DBTableMaster item);

  public abstract int findIndex(DBTableMaster item);

  public abstract void setItem(DBTableMaster item, int idx);

  public abstract void setItem(DBTableMaster item);

  public abstract void addItem(DBTableMaster item);

  public abstract void delItem(int idx);

  public abstract Query<? extends DBTableMaster> FROM();

  public void save() {
    if (this.mDBSaveLoadCallback != null) this.mDBSaveLoadCallback.preSave();
    mDBEngine.saveTable(this, mAlias);
    if (this.mDBSaveLoadCallback != null) this.mDBSaveLoadCallback.postSave();
  }

  public void load() {
    if (this.mDBSaveLoadCallback != null) this.mDBSaveLoadCallback.preLoad();
    mDBEngine.loadItemsTable(mAlias);
    if (this.mDBSaveLoadCallback != null) this.mDBSaveLoadCallback.postLoad();
  }
}
