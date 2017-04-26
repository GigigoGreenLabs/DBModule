package sqllitefortest;

import com.wagnerandade.coollection.query.Query;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;
import db.gigigo.com.dbmodule.dbsample.UsersModelv2;
import java.util.ArrayList;

import static com.wagnerandade.coollection.Coollection.from;



public class UsersModelv2WrapperSQLTEST extends DBTableWrapperMaster {

  public UsersModelv2WrapperSQLTEST(UsersModelv2 table, DBEngineMaster engineMaster, String alias) {
    super(table, engineMaster, alias);
  }

  ArrayList<UsersModelv2> mItems;
  //region asv new




  //endregion
  @Override public ArrayList<UsersModelv2> getItems() {
    if (mItems == null) {
      mItems = (ArrayList<UsersModelv2>) load();

    }
    return mItems;
  }


  //region revisar uso de las colecciones

  //region update/delete bulk
  @Override public void setItems(ArrayList<? extends DBTableMaster> items) {
    if (!items.isEmpty() && items.get(0) instanceof UsersModelv2) {
      mySetItems((ArrayList<UsersModelv2>) items);

      //save();//asv
      //resetItemsFor();//asv
    }
  }

  @Override public void clearItems() {
    mySetItems(new ArrayList<UsersModelv2>());
  }

  public void mySetItems(ArrayList<UsersModelv2> items) {
    itemsForDelete.addAll((ArrayList<UsersModelv2>) this.getItems());//asv
    mItems = (ArrayList<UsersModelv2>) items;
    itemsForInsert.addAll((ArrayList<UsersModelv2>)items);//asv
    save();

  }
  //endregion

  //region update REVISADO / , necesario llamar explicitamente al save desde app
  @Override public void setItem(DBTableMaster item, int idx) {
    if (item instanceof UsersModelv2 && idx > -1 && idx < mItems.size() - 1) {
      mItems = getItems();
      mItems.set(idx, (UsersModelv2) item);
      itemsForUpdate.add((UsersModelv2) item);//asv
      //setItems(mItems); //asv maybe save();?
    }
  }

  @Override public void setItem(DBTableMaster item) {
    int idx = findIndex(item);
    setItem(item, idx);

  }

  //endregion

  //region delete REVISADO /comprobado, necesario llamar explicitamente al save desde app
  @Override public void delItem(int idx) {
    mItems = getItems();
    itemsForDelete.add((UsersModelv2) mItems.get(idx));//asv
    mItems.remove(idx);
   // setItems(mItems); //asv this maybe will be save(), but is better if the save is only called from app
  }

  @Override public void delItem(DBTableMaster item) {

    if (item instanceof UsersModelv2) {
      int delIDX = -1;
      mItems = getItems();
      for (int i = 0; i < mItems.size(); i++) {
        if (item.hashCode() == mItems.get(i).hashCode()) delIDX = i;
      }
      if (delIDX > -1) {
        itemsForDelete.add((UsersModelv2) mItems.get(delIDX));//asv
        mItems.remove(delIDX);

        setItems(mItems);
      }
    }
  }
  //endregion

  //region insert REVISADO / funciona , necesario llamar explicitamente al save desde app
  @Override public void addItem(DBTableMaster item) {
    if (item instanceof UsersModelv2) {
      mItems = getItems();
      mItems.add((UsersModelv2) item);

      itemsForInsert.add((UsersModelv2) item);//asv the next line execute a save by resetAll items, this way we need call save after call additem for persist data, maybe put save but no call setitems
      //setItems(mItems);
    }
  }
  //endregion

  //endregion

  @Override public int findIndex(DBTableMaster item) {
    int idx = -1;
    for (int i = 0; i < mItems.size(); i++) {
      if (item.hashCode() == mItems.get(i).hashCode()) idx = i;
    }
    return idx;
  }
  @Override public Query<UsersModelv2> FROM() {
    Query<UsersModelv2> from = from(this.mItems);
    return from;
  }

  @Override public String HashCodeTableFields() {
    return SQLliteDB.UsuariosFieldsHashCode;
  }

  @Override public String ModelClass() {
    return "UsersModelv2";
  }
}