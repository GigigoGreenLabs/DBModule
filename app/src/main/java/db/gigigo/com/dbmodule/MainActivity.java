package db.gigigo.com.dbmodule;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.gigigo.dbsqliteimpl.DBEngineSQLLite;
import com.gigigo.dbsqliteimpl.SqliteManager;
import com.google.gson.Gson;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBMapperMaster;
import db.gigigo.com.dbmaster.masterclass.DBSaveLoadCallback;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import db.gigigo.com.dbmaster.schema.DBScheme;
import db.gigigo.com.dbmodule.dbsample.DataGenerator;
import db.gigigo.com.dbmodule.dbsample.NewTestModel;
import db.gigigo.com.dbmodule.dbsample.UsersModel;
import db.gigigo.com.dbmodule.dbsample.UsersModelv2;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import sqllitefortest.SQLliteDB;

import static com.wagnerandade.coollection.Coollection.eq;

public class MainActivity extends AppCompatActivity {
  public static SQLliteDB mMyDataBase;//asv this maybe in the application
  UsersModelv2 lastUsersModel;
  NewTestModel lastNewTestModel;
  Class className = null;

  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initDB();
    // mMyDataBase.Usuarios().addItem(new UsersModelv2("Pepe","Monder","4"));
    // mMyDataBase.Usuarios().save();

    //createSampleDB();

    setCallbackLoadSave();
    Button btnAdd = (Button) findViewById(R.id.btnAdd);
    btnAdd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        ArrayList<UsersModelv2> lst20 = new ArrayList<UsersModelv2>();
        for (int i = 0; i < 5; i++) {

          lastUsersModel = new UsersModelv2(DataGenerator.getRandomName() + "SQL" + i,
              DataGenerator.getRandomSurName() + "SQL" + i, System.currentTimeMillis() + "");
          lst20.add(lastUsersModel);
          mMyDataBase.Usuarios().addItem(lastUsersModel);
        }

        // mMyDataBase.Usuarios().setItems(lst20);
        mMyDataBase.Usuarios().save();
      }
    });

    Button btnDel = (Button) findViewById(R.id.btnDel);
    btnDel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (lastUsersModel != null) {
          System.out.println("class+aaa"+lastUsersModel.getClass());
          mMyDataBase.Usuarios().delItem(lastUsersModel);
          mMyDataBase.Usuarios().save();
        }
      }
    });

    Button btnList = (Button) findViewById(R.id.btnList);
    btnList.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Gson gson = new Gson();

        SqliteManager dbmanager = new SqliteManager(getApplicationContext(), "Usuarios");
        SQLiteDatabase db = dbmanager.getWritableDatabase();

        String jsonStrign = dbmanager.loadDatabaseAsJson("Usuarios", db);
        //jsonStrign = "{"+jsonStrign + "}";
        System.out.println(jsonStrign);

        // ArrayList<T> myArray= gson.fromJson(jsonStrign,T);

        UsersModelv2[] darray = gson.fromJson(jsonStrign, UsersModelv2[].class);

        //ArrayList<Object>  array = gson.fromJson(jsonStrign,ArrayList.class);
        ArrayList<UsersModelv2> myArray = new ArrayList<UsersModelv2>();
        for (int i = 0; i < darray.length; i++) {
          myArray.add((UsersModelv2) darray[i]);
        }

        String tableAlias = "Usuarios";
        DBScheme dbScheme = mMyDataBase.Usuarios().mDBEngine.loadDBScheme();

        for (int i = 0; i < dbScheme.getLstSchemaItems().size(); i++) {
          /*
          System.out.println("Model Class" + dbScheme.getLstSchemaItems().get(i).getModelClass());
          */
          if (tableAlias.equals(dbScheme.getLstSchemaItems().get(i).getTableAlias())) {
            /*System.out.println(
                "FIESTA DE LA KREBADA MODEL ENCONTRADO" + dbScheme.getLstSchemaItems().get(i).getModelClass());*/
            try {
              className =Class.forName("db.gigigo.com.dbmodule.dbsample."+dbScheme.getLstSchemaItems().get(i).getModelClass());
            } catch (ClassNotFoundException e) {
              e.printStackTrace();
            }
          }
        }

   /*     //A
        Type listType = new TypeToken<ArrayList<UsersModelv2>>() {
        }.getType();
        List<UsersModelv2> list = new Gson().fromJson(jsonStrign, listType);
*/





        //B
 /*       Type listType2 = new TypeToken<ArrayList<UsersModelv2>>() {
        }.getType();
        List<? extends DBTableMaster> list2 = new Gson().fromJson(jsonStrign, listType2);

        for (DBTableMaster dbTableMaster : list2) {
          System.out.println("--------------"+dbTableMaster);
        }*/



        //C
        Type  type = new ListParametizedType(className);
        List<? extends DBTableMaster> list  = gson.fromJson(jsonStrign, type);

        for (DBTableMaster dbTableMaster : list) {
          System.out.println(dbTableMaster.toString());
        }
        System.out.println("NºUsuarios: "+list.size());



        /*
          File file = dbmanager.loadDatabaseAsXml("Usuarios",db);
          dbmanager.readFromFile(file);

        //NEW USERMODEL SQLLITE-NOT-VALID

        ArrayList<ModelObj> items = mMyDataBase.Usuarios().getItemsModelObj();

        ArrayList<String> itemsStrings;

        for (ModelObj item : items) {
          itemsStrings = item.getList();
          System.out.print("ROW: ");
          for (String itemsString : itemsStrings) {
            System.out.print(itemsString + " ");
          }
          System.out.println(" ");

        }*/

        //OLD USERMODEL JSON-VALID

     /*   for (UsersModelv2 item : mMyDataBase.Usuarios().getItems()) {
          System.out.println("Name: "
              + item.getName()
              + " SurName: "
              + item.getSurname()
              + " hashcode: "
              + item.hashCode());
        }

        System.out.println("TOTAL: " + mMyDataBase.Usuarios().getItemsModelObj().size());
        */

      }
    });

    Button btnQuery = (Button) findViewById(R.id.btnQuery);
    btnQuery.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        List<UsersModelv2> all =
            mMyDataBase.Usuarios().FROM().where("Name", eq("Pepito")).orderBy("Surname").all();

        for (UsersModelv2 item : all) {
          System.out.println("Name: "
              + item.getName()
              + " SurName: "
              + item.getSurname()
              + " hashcode: "
              + item.hashCode());
        }
        System.out.println("TOTAL pepitos: " + all.size());
      }
    });
    Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
    btnUpdate.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        List<UsersModelv2> all = mMyDataBase.Usuarios()
            .FROM()
            .where("getName", eq("Alberto"))
            .and("SurName", eq("Sainz"))
            .orderBy("getSurname")
            .all();
        if (all.size() > 0) {
          UsersModelv2 user4Set = all.get(0);
          int idxForUpdate = mMyDataBase.Usuarios().findIndex(user4Set);
          user4Set.setName("Papote");
          mMyDataBase.Usuarios().setItem(user4Set, idxForUpdate);

          for (UsersModelv2 item : all) {
            System.out.println("Name: "
                + item.getName()
                + " SurName: "
                + item.getSurname()
                + " hashcode: "
                + item.hashCode());
          }
        }
        System.out.println("TOTAL pepitos: " + all.size());
      }
    });

    // mMyDataBase.Usuarios().createFrom(mMyDataBase.Usuarios())

    Button btnSqlAdmin = (Button) findViewById(R.id.btnsqladmin);
    btnSqlAdmin.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        com.gigigo.gigigocrud_sqliteandroid.MainActivity.open(MainActivity.this);
      }
    });
  }

  private void createSampleDB() {
    SqliteManager sql = new SqliteManager(getApplicationContext(), "Usuarios");
    SQLiteDatabase db = sql.getWritableDatabase();
    String sqlCreate = "CREATE TABLE Usuarios (codigo INTEGER, nombre TEXT)";
    db.execSQL(sqlCreate);

    if (db != null) {

      for (int i = 1; i <= 5; i++) {

        int codigo = i;
        String nombre = "DBModule" + i;

        db.execSQL(
            "INSERT INTO Usuarios (codigo, nombre) " + "VALUES (" + codigo + ", '" + nombre + "')");
      }

      db.close();
    }
  }

  long timeFirst = 0;

  private void setCallbackLoadSave() {
    mMyDataBase.Usuarios().setmDBSaveLoadCallback(new DBSaveLoadCallback() {
      @Override public void preSave() {
        timeFirst = System.currentTimeMillis();
        System.out.println("*****************preSave");
      }

      @Override public void postSave() {
        System.out.println(
            "*****************postSave time for save" + (System.currentTimeMillis() - timeFirst));
      }

      @Override public void preLoad() {
        System.out.println("########################preLoad");
      }

      @Override public void postLoad() {
        System.out.println("########################postLoad");
      }
    });
  }

  private void initDB() {
    DBEngineMaster bdEngine = new DBEngineSQLLite(this); //new DBEngineJSON(this);
    DBMapperMaster myMapper = new DBMapperMaster("UsersModel", "UsersModelv2") {
      @Override public <I, O> O convert(I input) {
        UsersModel u1 = (UsersModel) input;
        UsersModelv2 u2 =
            new UsersModelv2(u1.getName() + " Migrated", u1.getSurname() + " Migrated", "357");
        return (O) u2;
      }
    };

    bdEngine.setMigrationMappers(myMapper);
    mMyDataBase = new SQLliteDB(bdEngine);
    //mMyDataBase.migrateDB(); //FOR EXECUTE MIGRATION
  }

  private static class ListParametizedType implements ParameterizedType{

    private Type type;

    public ListParametizedType(Type type) {
      this.type = type;
    }

    @Override public Type[] getActualTypeArguments() {
      return new Type[]{type};
    }

    @Override public Type getRawType() {
      return ArrayList.class;
    }

    @Override public Type getOwnerType() {
      return null;
    }
  }
}
