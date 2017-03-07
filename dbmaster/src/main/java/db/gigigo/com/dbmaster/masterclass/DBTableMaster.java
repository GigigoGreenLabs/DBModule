package db.gigigo.com.dbmaster.masterclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nubor on 03/03/2017.
 */
public abstract class DBTableMaster implements Serializable {
  @Override public abstract int hashCode();
  public static int hashCodeObject(Object o) {
    return (o == null) ? 0 : o.hashCode();
  }
}

