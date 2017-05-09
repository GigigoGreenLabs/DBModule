package sqllitefortest;

import com.gigigo.dbmodule.generated.MyDB;
import com.wagnerandade.coollection.query.Query;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import db.gigigo.com.dbmodule.dbsample.NewTestModel;
import java.util.ArrayList;

import static com.wagnerandade.coollection.Coollection.from;

public class NewTestModelWrapperSQLTEST extends DBTableWrapperMaster{

	 public NewTestModelWrapperSQLTEST(NewTestModel table, DBEngineMaster engineMaster, String alias){
		 super(table, engineMaster, alias);
  } 
	 ArrayList<NewTestModel> mItems;

  @Override public ArrayList<NewTestModel> getItems() {
    if (mItems == null) {
      mItems = (ArrayList<NewTestModel>) load();
    }
    return mItems;
  }

  @Override public void setItems(ArrayList<? extends DBTableMaster> items) {
    if (!items.isEmpty() && items.get(0) instanceof NewTestModel) {
      mySetItems((ArrayList<NewTestModel>) items);
    }
  }

 @Override public void clearItems() {
    mySetItems(new ArrayList<NewTestModel>());
  }
  public void mySetItems(ArrayList<NewTestModel> items) {
    mItems = (ArrayList<NewTestModel>) items;
    save();
  }

  @Override public void delItem(int idx) {
    mItems = getItems();
    mItems.remove(idx);
    setItems(mItems);
  }

  @Override public void delItem(DBTableMaster item) {

    if (item instanceof NewTestModel) {
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
    if (item instanceof NewTestModel && idx > -1 && idx < mItems.size() - 1) {
      mItems = getItems();
      mItems.set(idx, (NewTestModel) item);
      setItems(mItems);
    }
  }

  @Override public void setItem(DBTableMaster item) {
    int idx = findIndex(item);
    setItem(item, idx);
  }

  @Override public void addItem(DBTableMaster item) {
    if (item instanceof NewTestModel) {
      mItems = getItems();
      mItems.add((NewTestModel) item);
      setItems(mItems);
    }
  }

  @Override public Query<NewTestModel> FROM() {
    Query<NewTestModel> from = from(this.mItems);
    return from;
  } 
 

 @Override public String HashCodeTableFields(){ return MyDB.TesteoFieldsHashCode ;}
  @Override public String ModelClass() {return "NewTestModel";}

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
}