package sqllitefortest;


import db.gigigo.com.dbmodule.dbsample.NewTestModel;

import db.gigigo.com.dbmodule.dbsample.OfficeModel;

import db.gigigo.com.dbmodule.dbsample.UsersModelv2;

import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;

import db.gigigo.com.dbmaster.masterclass.DBMaster;

import java.util.ArrayList;

import db.gigigo.com.dbmaster.schema.DBScheme;

import db.gigigo.com.dbmaster.schema.DBSchemeItem;

import db.gigigo.com.dbmaster.schema.DBTableFieldScheme;

import db.gigigo.com.dbmaster.schema.DBTableScheme;
//fixme sobra
@Deprecated
public class SQLliteDB extends DBMaster {

  public static final String TesteoFieldsHashCode = "1710625327";
  public static final String OfisFieldsHashCode = "-760778162";
  public static final String UsuariosFieldsHashCode = "-895042700";

  public SQLliteDB(DBEngineMaster mDBEngine) {
    super(mDBEngine);
    DBScheme dbScheme = new DBScheme();
    dbScheme.setDbName("Usuarios");//asv revisar xq se inventa nombres el sqllite this.toString());
    ArrayList<DBSchemeItem> lstDBSchemeItem = new ArrayList<>();
    if (!mDBEngine.isDBCreated(dbScheme)) {
      mDBEngine.createDB(dbScheme);
    } else {
      dbScheme = mDBEngine.loadDBScheme();
      lstDBSchemeItem.addAll(dbScheme.getLstSchemaItems());
    }

    lstDBSchemeItem.add(new DBSchemeItem("Testeo",
        db.gigigo.com.dbmodule.dbsample.NewTestModel.class.getSimpleName(), TesteoFieldsHashCode,
        System.currentTimeMillis()));
    lstDBSchemeItem.add(
        new DBSchemeItem("Ofis", db.gigigo.com.dbmodule.dbsample.OfficeModel.class.getSimpleName(),
            OfisFieldsHashCode, System.currentTimeMillis()));
    lstDBSchemeItem.add(new DBSchemeItem("Usuarios",
        db.gigigo.com.dbmodule.dbsample.UsersModelv2.class.getSimpleName(), UsuariosFieldsHashCode,
        System.currentTimeMillis()));
    dbScheme.setLstSchemaItems(lstDBSchemeItem);
    mDBEngine.createDBScheme(dbScheme);
    if (!mDBEngine.isTableCreated("Testeo")) {
      ArrayList<DBTableFieldScheme> lstFields_Testeo = new ArrayList<>();
      lstFields_Testeo.add(new DBTableFieldScheme("java.lang.String", "Title"));
      lstFields_Testeo.add(new DBTableFieldScheme("java.lang.String", "Nombre"));
      lstFields_Testeo.add(new DBTableFieldScheme("java.lang.Integer", "Age"));
      DBTableScheme tableTesteo = new DBTableScheme("Testeo", lstFields_Testeo);
      mDBEngine.createTableScheme(tableTesteo);
      mDBEngine.createTable(tableTesteo);
    }
    if (!mDBEngine.isTableCreated("Ofis")) {
      ArrayList<DBTableFieldScheme> lstFields_Ofis = new ArrayList<>();
      lstFields_Ofis.add(new DBTableFieldScheme("java.lang.String", "Nombre"));
      DBTableScheme tableOfis = new DBTableScheme("Ofis", lstFields_Ofis);
      mDBEngine.createTableScheme(tableOfis);
      mDBEngine.createTable(tableOfis);
    }
    if (!mDBEngine.isTableCreated("Usuarios")) {
      ArrayList<DBTableFieldScheme> lstFields_Usuarios = new ArrayList<>();
      lstFields_Usuarios.add(new DBTableFieldScheme("java.lang.String", "Name"));
      lstFields_Usuarios.add(new DBTableFieldScheme("java.lang.String", "Surname"));
      lstFields_Usuarios.add(new DBTableFieldScheme("java.lang.Integer", "Age"));
      DBTableScheme tableUsuarios = new DBTableScheme("Usuarios", lstFields_Usuarios);
      mDBEngine.createTableScheme(tableUsuarios);
      mDBEngine.createTable(tableUsuarios);
    }
  }

  NewTestModelWrapperSQLTEST mTesteo;

  public NewTestModelWrapperSQLTEST Testeo() {
    if (mTesteo == null) {
      ArrayList<NewTestModel> listItems = (ArrayList<NewTestModel>) mDBEngine.loadItemsTable(
          "Testeo"/*+TesteoFieldsHashCode*/); //asv poner este valor de hashcode en abs para evitar el fail de name with -
      mTesteo = new NewTestModelWrapperSQLTEST(new NewTestModel(), mDBEngine, "Testeo");
      mTesteo.setItems(listItems);
    }
    return mTesteo;
  }

  OfficeModelWrapperSQLTEST mOfis;

  public OfficeModelWrapperSQLTEST Ofis() {
    if (mOfis == null) {
      ArrayList<OfficeModel> listItems =
          (ArrayList<OfficeModel>) mDBEngine.loadItemsTable("Ofis"/*+OfisFieldsHashCode*/);
      mOfis = new OfficeModelWrapperSQLTEST(new OfficeModel(), mDBEngine, "Ofis");
      mOfis.setItems(listItems);
    }
    return mOfis;
  }

  UsersModelv2WrapperSQLTEST mUsuarios;

  public UsersModelv2WrapperSQLTEST Usuarios() {
    if (mUsuarios == null) {
      ArrayList<UsersModelv2> listItems =
          (ArrayList<UsersModelv2>) mDBEngine.loadItemsTable("Usuarios"/*+UsuariosFieldsHashCode*/);
      mUsuarios = new UsersModelv2WrapperSQLTEST(new UsersModelv2(), mDBEngine, "Usuarios");
      mUsuarios.setItems(listItems);
    }
    return mUsuarios;
  }

  public void migrateDB() {
    this.Testeo().save();
    this.Ofis().save();
    this.Usuarios().save();
  }

}