package db.gigigo.com.dbmaster.masterclass;

/**
 * Created by nubor on 09/03/2017.
 */
public abstract class DBMapperMaster  {
  final String inputClass;
  final String outputClass;

  public DBMapperMaster(String inputClass, String outputClass) {
    this.inputClass = inputClass;
    this.outputClass = outputClass;
  }

  public String getInputClass() {
    return inputClass;
  }

  public String getOutputClass() {
    return outputClass;
  }

  public abstract <I, O> O convert(I input ) ;
}
