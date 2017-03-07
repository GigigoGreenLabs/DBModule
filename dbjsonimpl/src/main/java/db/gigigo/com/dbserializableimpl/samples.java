package db.gigigo.com.dbserializableimpl;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by nubor on 03/03/2017.
 */
public class samples {
  private class MyOwnPojo {

  }

  //el TYPE DEL OBJECT por ejemplo deberia venir del apt
  public static void saveObject(Context context, MyOwnPojo obj, String filename) {
    try {
      FileOutputStream fos = context.openFileOutput(filename + ".json", Context.MODE_PRIVATE);
      ObjectOutputStream os = new ObjectOutputStream(fos);
      os.writeObject(obj);
      os.close();
      fos.close();
    } catch (Exception e) {
      Log.e("Error", e.getMessage());
    }
  }

  public static List<MyOwnPojo> loadCardInfo(Context context, String filename) {
    try {
      FileInputStream fis = context.openFileInput(filename + ".json");
      ObjectInputStream is = new ObjectInputStream(fis);
      List<MyOwnPojo> lstCardsInfo = (List<MyOwnPojo>) is.readObject();
      is.close();
      fis.close();
      return lstCardsInfo;
    } catch (Exception e) {
      Log.e("Error", e.getMessage());
    }
    return null;
  }
}
