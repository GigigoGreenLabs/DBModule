package db.gigigo.com.dbmodule;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBSaveLoadCallback;
import db.gigigo.com.dbmodule.dbsample.DataGenerator;
import db.gigigo.com.dbmodule.dbsample.MyDB;
import db.gigigo.com.dbmodule.dbsample.NewTestModel;
import db.gigigo.com.dbmodule.dbsample.UsersModel;
import db.gigigo.com.dbserializableimpl.DBEngineJSON;
import java.util.List;

import static com.wagnerandade.coollection.Coollection.eq;
import static com.wagnerandade.coollection.Coollection.from;

public class MainActivity extends AppCompatActivity {
  public static MyDB mMyDataBase;//asv this maybe in the application
  UsersModel lastUsersModel;
  NewTestModel lastNewTestModel;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initDB();
    setCallbackLoadSave();
    Button btnAdd = (Button) findViewById(R.id.btnAdd);
    btnAdd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        lastUsersModel =
            new UsersModel(DataGenerator.getRandomName(), DataGenerator.getRandomSurName());
        mMyDataBase.Usuarios().addItem(lastUsersModel);
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

        for (UsersModel item : mMyDataBase.Usuarios().getItems()) {
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

        List<UsersModel> all = mMyDataBase.Usuarios()
            .FROM()
            .where("getName", eq("Pepito"))
            .orderBy("getSurname")
            .all();

        for (UsersModel item : all) {
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

        List<UsersModel> all = mMyDataBase.Usuarios()
            .FROM()
            .where("getName", eq("Pepito"))
            .orderBy("getSurname")
            .all();
        if (all.size() > 0) {
          UsersModel user4Set = all.get(0);
          int idxForUpdate = mMyDataBase.Usuarios().findIndex(user4Set);
          user4Set.setName("Papote");
          mMyDataBase.Usuarios().setItem(user4Set, idxForUpdate);

          for (UsersModel item : all) {
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
  }

  private void setCallbackLoadSave() {
    mMyDataBase.Usuarios().setmDBSaveLoadCallback(new DBSaveLoadCallback() {
      @Override public void preSave() {
        System.out.println("preSave");
      }

      @Override public void postSave() {
        System.out.println("postSave");
      }

      @Override public void preLoad() {
        System.out.println("preLoad");
      }

      @Override public void postLoad() {
        System.out.println("postLoad");
      }
    });
  }

  private void initDB() {
    DBEngineMaster bdEngine = new DBEngineJSON(this);
    mMyDataBase = new MyDB(bdEngine);
  }
}
