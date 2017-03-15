package db.gigigo.com.dbmodule.dbsample;

import com.gigigo.aprocesor.annotations.DataBase;
import com.gigigo.dbmodule.generated.NewTestModelWrapper;
import com.gigigo.dbmodule.generated.UsersModelWrapper;
import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;
import db.gigigo.com.dbmaster.masterclass.DBMaster;
import db.gigigo.com.dbmodule.dbsample.generatedapt.DBTableUsersWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nubor on 06/03/2017.
 */
@DataBase public class MyDB extends DBMaster {

  public MyDB(DBEngineMaster mDBEngine) {
    super(mDBEngine);
  }

  /*Apt Generated, de momento lo genero yo, simplemente hay q crear esta clase al completo en el generated*/
  //el tableAlias es "Usuarios"
  UsersModelWrapper mUsuarios;

  //no mola nada de nada q cada vez q lo preguntes lo vuelva a sacar del disco
  //ver pros y contras
  public UsersModelWrapper Usuarios() {
    ArrayList<UsersModel> listItems = (ArrayList<UsersModel>) mDBEngine.loadItemsTable("Usuarios");
    mUsuarios = new UsersModelWrapper(new UsersModel(), mDBEngine, "Usuarios");
    mUsuarios.setItems(listItems);
    return mUsuarios;
  }

  NewTestModelWrapper mTesteo;

  //no mola nada de nada q cada vez q lo preguntes lo vuelva a sacar del disco
  //ver pros y contras
  public NewTestModelWrapper Testeo() {
    ArrayList<NewTestModel> listItems = (ArrayList<NewTestModel>) mDBEngine.loadItemsTable("Testeo");
    mTesteo = new NewTestModelWrapper(new NewTestModel(), mDBEngine, "Testeo");
    mTesteo.setItems(listItems);
    return mTesteo;
  }
}