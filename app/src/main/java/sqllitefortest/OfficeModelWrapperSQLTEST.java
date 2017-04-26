package sqllitefortest;

import com.gigigo.dbmodule.generated.MyDB;
import com.wagnerandade.coollection.query.Query;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import db.gigigo.com.dbmodule.dbsample.OfficeModel;
import java.util.ArrayList;

import static com.wagnerandade.coollection.Coollection.from;
import static sqllitefortest.SQLliteDB.OfisFieldsHashCode;

public class OfficeModelWrapperSQLTEST extends DBTableWrapperMaster{

	 public OfficeModelWrapperSQLTEST(OfficeModel table, DBEngineMaster engineMaster, String alias){
		 super(table, engineMaster, alias);
  } 
	 ArrayList<OfficeModel> mItems;

  @Override public ArrayList<OfficeModel> getItems() {
    if (mItems == null) {
      mItems = (ArrayList<OfficeModel>) load();
    }
    return mItems;
  }

  @Override public void setItems(ArrayList<? extends DBTableMaster> items) {
    if (!items.isEmpty() && items.get(0) instanceof OfficeModel) {
      mySetItems((ArrayList<OfficeModel>) items);
    }
  }

 @Override public void clearItems() {
    mySetItems(new ArrayList<OfficeModel>());
  }
  public void mySetItems(ArrayList<OfficeModel> items) {
    mItems = (ArrayList<OfficeModel>) items;
    save();
  }

  @Override public void delItem(int idx) {
    mItems = getItems();
    mItems.remove(idx);
    setItems(mItems);
  }

  @Override public void delItem(DBTableMaster item) {

    if (item instanceof OfficeModel) {
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
    if (item instanceof OfficeModel && idx > -1 && idx < mItems.size() - 1) {
      mItems = getItems();
      mItems.set(idx, (OfficeModel) item);
      setItems(mItems);
    }
  }

  @Override public void setItem(DBTableMaster item) {
    int idx = findIndex(item);
    setItem(item, idx);
  }

  @Override public void addItem(DBTableMaster item) {
    if (item instanceof OfficeModel) {
      mItems = getItems();
      mItems.add((OfficeModel) item);
      setItems(mItems);
    }
  }

  @Override public Query<OfficeModel> FROM() {
    Query<OfficeModel> from = from(this.mItems);
    return from;
  } 
 

 @Override public String HashCodeTableFields(){ return SQLliteDB.OfisFieldsHashCode ;}
  @Override public String ModelClass() {return "OfficeModel";}}