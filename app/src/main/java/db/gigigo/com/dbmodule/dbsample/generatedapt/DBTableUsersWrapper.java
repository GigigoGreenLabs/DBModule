package db.gigigo.com.dbmodule.dbsample.generatedapt;

import com.wagnerandade.coollection.query.Query;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import db.gigigo.com.dbmodule.dbsample.UsersModel;
import java.util.ArrayList;

import static com.wagnerandade.coollection.Coollection.from;

/**
 * Created by nubor on 06/03/2017.
 */ /* apt generation*/
public class DBTableUsersWrapper extends DBTableWrapperMaster {
  public DBTableUsersWrapper(UsersModel table, DBEngineMaster engineMaster, String alias) {
    super(table, engineMaster, alias);
  }

  ArrayList<UsersModel> mItems;

  @Override public ArrayList<UsersModel> getItems() {
    if (mItems == null) {
      mItems = (ArrayList<UsersModel>) mDBEngine.loadItemsTable(this.mAlias);
    }
    return mItems;
  }



  @Override public void setItems(ArrayList<? extends DBTableMaster> items) {
    if (!items.isEmpty() && items.get(0) instanceof UsersModel) {
      mySetItems((ArrayList<UsersModel>) items);
    }
  }

  @Override public void clearItems() {

  }

  public void mySetItems(ArrayList<UsersModel> items) {
    mItems = (ArrayList<UsersModel>) items;
    mDBEngine.saveTable(this, this.mAlias);
  }

  @Override public void delItem(int idx) {
    mItems = getItems();
    mItems.remove(idx);
    setItems(mItems);
  }

  @Override public void delItem(DBTableMaster item) {

    if (item instanceof UsersModel) {
      int delIDX = -1;
      mItems = getItems();
      for (int i = 0; i < mItems.size(); i++) {
        if (item.hashCode() == mItems.get(i).hashCode()) delIDX = i;
      }
      if (delIDX > -1) {
        mItems.remove(delIDX);
        setItems(mItems);
      }
    }
  }

  @Override public int findIndex(DBTableMaster item) {
    int idx = -1;
    for (int i = 0; i < mItems.size(); i++) {
      if (item.hashCode() == mItems.get(i).hashCode()) idx = i;
    }
    return idx;
  }

  @Override public void setItem(DBTableMaster item, int idx) {
    if (item instanceof UsersModel && idx > -1 && idx < mItems.size() - 1) {
      mItems = getItems();
      mItems.set(idx, (UsersModel) item);
      setItems(mItems);
    }
  }

  @Override public void setItem(DBTableMaster item) {
    int idx = findIndex(item);
    setItem(item, idx);
  }

  @Override public void addItem(DBTableMaster item) {
    if (item instanceof UsersModel) {
      mItems = getItems();
      mItems.add((UsersModel) item);
      setItems(mItems);
    }
  }

  @Override public Query<UsersModel> FROM() {
    Query<UsersModel> from = from(this.mItems);
    return from;
  }

  @Override public String HashCodeTableFields() {
    return null;
  }

  @Override public String ModelClass() {
    return null;
  }

  @Override public <T extends DBTableMaster> T last() {
    return null;
  }

  @Override public <T extends DBTableMaster> T first() {
    return null;
  }

  @Override public int size() {
    return 0;
  }

  @Override public boolean hasItems() {
    return false;
  }

  @Override public boolean hasThisItem(int index) {
    return false;
  }
  /**/
}
