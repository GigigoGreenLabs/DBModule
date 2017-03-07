package db.gigigo.com.dbmodule.dbsample;

import android.os.Build;
import com.gigigo.aprocesor.annotations.DataField;
import com.gigigo.aprocesor.annotations.DataTable;
import db.gigigo.com.dbmaster.BuildConfig;
import db.gigigo.com.dbmaster.masterclass.DBTableMaster;
import java.util.Objects;

/**
 * Created by nubor on 07/03/2017.
 */
@DataTable(alias = "Testeo")
public class NewTestModel   extends DBTableMaster {
  @DataField String Title;
  @DataField String Name;
  @DataField String Age;

  public String getTitle() {
    return Title;
  }

  public void setTitle(String title) {
    Title = title;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getAge() {
    return Age;
  }

  public void setAge(String age) {
    Age = age;
  }
//*FUCK esto he tenido que picarlo yo, debería transladarse al wrapper, aunk es del row, si no x reflexction en el Base*/
  @Override public int hashCode() {
    if (BuildConfig.VERSION_CODE < 19) {
      int megaHash = 0;
      megaHash = megaHash + hashCodeObject(this.getName());
      megaHash = megaHash + hashCodeObject(this.getTitle());
      megaHash = megaHash + hashCodeObject(this.getAge());
      return megaHash;
    } else {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        return Objects.hash(Name,Title,Age);
      }
    }
    return -1;
  }
}
