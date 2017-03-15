package db.gigigo.com.dbmodule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.gigigo.dbmodule.generated.MyDB;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBMapperMaster;
import db.gigigo.com.dbmaster.masterclass.DBSaveLoadCallback;
import db.gigigo.com.dbmodule.dbsample.DataGenerator;
import db.gigigo.com.dbmodule.dbsample.NewTestModel;
import db.gigigo.com.dbmodule.dbsample.UsersModel;

import db.gigigo.com.dbmodule.dbsample.UsersModelv2;
import db.gigigo.com.dbserializableimpl.DBEngineJSON;
import java.util.ArrayList;
import java.util.List;

import static com.wagnerandade.coollection.Coollection.eq;

public class MainActivity extends AppCompatActivity {
  public static MyDB mMyDataBase;//asv this maybe in the application
  UsersModelv2 lastUsersModel;
  NewTestModel lastNewTestModel;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initDB();
    setCallbackLoadSave();
    Button btnAdd = (Button) findViewById(R.id.btnAdd);
    btnAdd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        ArrayList<UsersModelv2> lst25 = new ArrayList<UsersModelv2>();
        for (int i = 0; i < 2; i++) {

          lastUsersModel = new UsersModelv2(DataGenerator.getRandomName() + "NEW",
              DataGenerator.getRandomSurName() + "NEW", System.currentTimeMillis() + "");
          lst25.add(lastUsersModel);
        }

        mMyDataBase.Usuarios().setItems(lst25);
      }
    });

    Button btnDel = (Button) findViewById(R.id.btnDel);
    btnDel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        mMyDataBase.Usuarios().delItem(lastUsersModel);
      }
    });

    Button btnList = (Button) findViewById(R.id.btnList);
    btnList.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        for (UsersModelv2 item : mMyDataBase.Usuarios().getItems()) {
          System.out.println("Name: "
              + item.getName()
              + " SurName: "
              + item.getSurname()
              + " hashcode: "
              + item.hashCode());
        }
        System.out.println("TOTAL: " + mMyDataBase.Usuarios().getItems().size());
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
            .where("getName", eq("Pepito"))
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
    DBEngineMaster bdEngine = new DBEngineJSON(this);
    DBMapperMaster myMapper = new DBMapperMaster("UsersModel", "UsersModelv2") {
      @Override public <I, O> O convert(I input) {
        UsersModel u1 = (UsersModel) input;
        UsersModelv2 u2 =
            new UsersModelv2(u1.getName() + " Migrated", u1.getSurname() + " Migrated", "357");
        return (O) u2;
      }
    };
    bdEngine.setMigrationMappers(myMapper);
    mMyDataBase = new MyDB(bdEngine);
    mMyDataBase.migrateDB(); //FOR EXECUTE MIGRATION
  }
}
