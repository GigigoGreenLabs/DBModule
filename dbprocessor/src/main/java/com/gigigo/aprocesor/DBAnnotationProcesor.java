package com.gigigo.aprocesor;

import com.gigigo.aprocesor.annotations.DataField;
import com.gigigo.aprocesor.annotations.DataTable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({
    "com.gigigo.aprocesor.annotations.DataBase", "com.gigigo.aprocesor.annotations.DataTable",
    "com.gigigo.aprocesor.annotations.DataField"
}) @SupportedSourceVersion(SourceVersion.RELEASE_7) public class DBAnnotationProcesor
    extends AbstractProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    String packageGenerated = "package com.gigigo.dbmodule.generated;";
    //asv de momento sólo nos importan los @DataTable, los @DataBase de momento no estarán ni presentes
    //y los @Datafield tomaran sentido una vez que utilizemos un SQLite, un Realm, un Ormlite...
    StringBuilder builder = new StringBuilder();

    StringBuilder builderDataBaseMaster = new StringBuilder();

    builderDataBaseMaster.append(packageGenerated + "\n\n");

    for (Element element : roundEnv.getElementsAnnotatedWith(DataTable.class)) {
      builderDataBaseMaster.append("import " + element.asType().toString() + ";\n\n");
    }

    builderDataBaseMaster.append("import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;\n\n")
        .append("import db.gigigo.com.dbmaster.masterclass.DBMaster;\n\n")
        .append("import java.util.ArrayList;\n\n")
        .append("import db.gigigo.com.dbmaster.schema.DBScheme;\n\n")
        .append("import db.gigigo.com.dbmaster.schema.DBSchemeItem;\n\n")
        .append("import db.gigigo.com.dbmaster.schema.DBTableFieldScheme;\n\n")
        .append("import db.gigigo.com.dbmaster.schema.DBTableScheme;\n\n");

    builderDataBaseMaster.append("public class MyDB extends DBMaster {\n\n");


    /*calculo de los hascode de los schemas  de las tablas*/
    final Set<? extends Element> dataFields = roundEnv.getElementsAnnotatedWith(DataField.class);
    final Map<Element, List<Element>> dataTables = new HashMap<>();
    final List<Element> lstTables4Hascode = new ArrayList<>();

    for (final Element element : dataFields) {
      final Element classElement = element.getEnclosingElement();
      if (classElement.getAnnotation(DataTable.class) != null) {
        if (!lstTables4Hascode.contains(classElement)) lstTables4Hascode.add(classElement);
        List<Element> list = dataTables.get(classElement);
        if (list == null) {
          list = new ArrayList<>();
          dataTables.put(classElement, list);
        }
        list.add(element);
      }
    }

    for (Element ele : lstTables4Hascode) {
      List<Element> list = dataTables.get(ele);
      int megaHash = 0;
      for (Element fieldElement : list) {
        megaHash = megaHash + hashCodeObject(fieldElement.asType().toString());
        megaHash = megaHash + hashCodeObject(fieldElement.getSimpleName().toString());
      }

      DataTable dataTableAnnotation = ele.getAnnotation(DataTable.class);
      String aliasTable = dataTableAnnotation.alias();

      builderDataBaseMaster.append(
          "public static final String " + aliasTable + "FieldsHashCode = \"" + megaHash + "\" ;\n");
    }

    builderDataBaseMaster.append("public MyDB(DBEngineMaster mDBEngine) {\n"
        + "        super(mDBEngine);\n"
        + "        DBScheme dbScheme = new DBScheme();\n"
        + "    dbScheme.setDbName(this.getClass().toString());\n "
        + "     ArrayList<DBSchemeItem> lstDBSchemeItem = new ArrayList<>();\n"
        + "    if (!mDBEngine.isDBCreated(dbScheme)) {\n"
        + "      mDBEngine.createDB(dbScheme);\n"
        + "    }\n else{\n"
        + "       dbScheme=mDBEngine.loadDBScheme();\n"
        + "       lstDBSchemeItem.addAll(dbScheme.getLstSchemaItems());}\n"
        + "       ");

    for (Element ele : lstTables4Hascode) {
      DataTable dataTableAnnotation = ele.getAnnotation(DataTable.class);
      String aliasTable = dataTableAnnotation.alias();
      builderDataBaseMaster.append("\nlstDBSchemeItem.add(\n"
          + " new DBSchemeItem(\""
          + aliasTable
          + "\" , "
          + ele
          + ".class.getSimpleName(), "
          + aliasTable
          + "FieldsHashCode,System.currentTimeMillis()));");
    }
    builderDataBaseMaster.append("\n dbScheme.setLstSchemaItems(lstDBSchemeItem);"
        + "\nmDBEngine.createDBScheme(dbScheme);");

    for (Element ele : lstTables4Hascode) {
      DataTable dataTableAnnotation = ele.getAnnotation(DataTable.class);
      String aliasTable = dataTableAnnotation.alias();
      builderDataBaseMaster.append("\nif (!mDBEngine.isTableCreated(\""
          + aliasTable
          + "\")) {\n"
          + "      ArrayList<DBTableFieldScheme> lstFields_"
          + aliasTable
          + " = new ArrayList<>(); ");

      List<Element> list = dataTables.get(ele);

      for (Element fieldElement : list) {
        builderDataBaseMaster.append(
            "\n lstFields_" + aliasTable + ".add(new DBTableFieldScheme(\"" + fieldElement.asType()
                .toString() + "\", \"" + fieldElement.getSimpleName().toString() + "\"));");
      }
      builderDataBaseMaster.append("\nDBTableScheme table"
          + aliasTable
          + " = new DBTableScheme(\""
          + aliasTable
          + "\", lstFields_"
          + aliasTable
          + ");");
      builderDataBaseMaster.append("\nmDBEngine.createTableScheme(table" + aliasTable + ");");
      builderDataBaseMaster.append("\nmDBEngine.createTable(table" + aliasTable + ");\n}");
    }

    builderDataBaseMaster.append("      }\n\n");
    String MigrateDBMethodIni = " public void migrateDB(){\n";
    String MigrateDBMethodBody = "";

    //1º datatables
    for (Element element : roundEnv.getElementsAnnotatedWith(DataTable.class)) {
      String dataTableClassName = element.getSimpleName().toString();
      String importToTableClass = element.asType().toString();
      DataTable dataTableAnnotation = element.getAnnotation(DataTable.class);
      String aliasTable = dataTableAnnotation.alias();
      builder = new StringBuilder();

      builder
          //package
          .append(packageGenerated + "\n\n")
          //import
          .append("import com.wagnerandade.coollection.query.Query;\n")
          .append("import db.gigigo.com.dbmaster.masterclass.DBEngineMaster;\n")
          .append("import db.gigigo.com.dbmaster.masterclass.DBTableMaster;\n")
          .append("import db.gigigo.com.dbmaster.masterclass.DBTableWrapperMaster;\n")
          .append("import "
              + importToTableClass
              + ";\n\n") // import db.gigigo.com.dbmodule.dbsample.UsersModel;
          .append("import java.util.ArrayList;\n")
          .append("import static com.wagnerandade.coollection.Coollection.*;\n")

          .append("public class "
              + dataTableClassName
              + "Wrapper extends DBTableWrapperMaster{\n\n") // open class
          .append("\t public "
              + dataTableClassName
              + "Wrapper("
              + dataTableClassName
              + " table, DBEngineMaster engineMaster, String alias){\n") // open method
          .append("\t\t super(table, engineMaster, alias);\n" + "  } \n")
          .append("\t ArrayList<"
              + dataTableClassName
              + "> mItems;\n"
              + "\n"
              + "  @Override public ArrayList<"
              + dataTableClassName
              + "> getItems() {\n"
              + "    if (mItems == null) {\n"
              + "      mItems = (ArrayList<"
              + dataTableClassName
              + ">) load();\n"
              + "    }\n"
              + "    return mItems;\n"
              + "  }\n"
              + "\n"
              + "  @Override public void setItems(ArrayList<? extends DBTableMaster> items) {\n"
              + "    if (items!=null && !items.isEmpty() && items.get(0) instanceof "
              + dataTableClassName
              + ") {\n"
              + "      mySetItems((ArrayList<"
              + dataTableClassName
              + ">) items);\n"
              + "    }\n"
              + "  }\n"
              + "\n @Override public void clearItems() {\n"
              + "    mySetItems(new ArrayList<"
              + dataTableClassName
              + ">());\n"
              + "  }\n"
              + "  public void mySetItems(ArrayList<"
              + dataTableClassName
              + "> items) {\n"
              + " itemsForDelete.addAll((ArrayList<"
              + dataTableClassName
              + ">) this.getItems());\n"
              + "    mItems = (ArrayList<"
              + dataTableClassName
              + ">) items;\n"
              + "  itemsForInsert.addAll((ArrayList<"+ dataTableClassName+">) items); \n"
              + "    save();\n"
              + "  }\n"
              + "\n"
              + "  @Override public void delItem(int idx) {\n"
              + "    mItems = getItems();\n  "
              + " itemsForDelete.add(("+ dataTableClassName+") mItems.get(idx)); \n"
              + "    mItems.remove(idx);\n"
              + "    //setItems(mItems);\n"
              + "  }\n"
              + "\n"
              + "  @Override public void delItem(DBTableMaster item) {\n"
              + "\n"
              + "    if (item instanceof "
              + dataTableClassName
              + ") {\n"
              + "      int delIDX = -1;\n"
              + "      mItems = getItems();\n"
              + "      for (int i = 0; i < mItems.size(); i++) {\n"
              + "        if (item.hashCode() == mItems.get(i).hashCode()) delIDX = i;\n"
              + "      }\n"
              + "      if (delIDX > -1) {\n"
              + "      itemsForDelete.add(("+ dataTableClassName+")mItems.get(delIDX));"
              + "        mItems.remove(delIDX);\n"
              + "       //setItems(mItems);\n"
              + "      }\n"
              + "    }\n"
              + "  }\n"
              + "\n"
              + "  @Override public int findIndex(DBTableMaster item) {\n"
              + "    int idx = -1;\n"
              + "    for (int i = 0; i < mItems.size(); i++) {\n"
              + "      if (item.hashCode() == mItems.get(i).hashCode()) idx = i;\n"
              + "    }\n"
              + "    return idx;\n"
              + "  }\n"
              + "\n"
              + "  @Override public void setItem(DBTableMaster item, int idx) {\n"
              + "    if (item instanceof "
              + dataTableClassName
              + " && idx > -1 && idx < mItems.size() - 1) {\n"
              + "      mItems = getItems();\n"
              + "      mItems.set(idx, ("
              + dataTableClassName
              + ") item);\n"
              + "      setItems(mItems);\n"
              + "    }\n"
              + "  }\n"
              + "\n"
              + "  @Override public void setItem(DBTableMaster item) {\n"
              + "    int idx = findIndex(item);\n"
              + "    setItem(item, idx);\n"
              + "  }\n"
              + "\n"
              + "  @Override public void addItem(DBTableMaster item) {\n"
              + "    if (item instanceof "
              + dataTableClassName
              + ") {\n"
              + "      mItems = getItems();\n"
              + "      mItems.add(("
              + dataTableClassName
              + ") item);\n"
              + " itemsForInsert.add(("+dataTableClassName+") item);\n"
              + "      //setItems(mItems);\n"
              + "    }\n"
              + "  }\n"
              + "\n"
              + "  @Override public Query<"
              + dataTableClassName
              + "> FROM() {\n"
              + "    Query<"
              + dataTableClassName
              + "> from = from(this.mItems);\n"
              + "    return from;\n"
              + "  } \n \n\n"
              + " @Override public String HashCodeTableFields(){ return MyDB."
              + aliasTable
              + "FieldsHashCode ;}\n"
              + "  @Override public String ModelClass() {return \""
              + dataTableClassName
              + "\";}"
              + " @Override\n"
              + "  public   <T extends DBTableMaster> T last(){\n"
              + "    if(this.mItems!=null && this.mItems.size()>0 )\n"
              + "      return (T)this.mItems.get(this.mItems.size()-1);\n"
              + "    else\n"
              + "      return null;\n"
              + "  }\n"
              + "  @Override\n"
              + "  public   <T extends DBTableMaster> T first(){\n"
              + "    if(this.mItems!=null && this.mItems.size()>0 )\n"
              + "      return (T)this.mItems.get(1);\n"
              + "    else\n"
              + "      return null;\n"
              + "\n"
              + "  }\n"
              + "  @Override\n"
              + "  public   int size(){\n"
              + "    if(this.mItems!=null && this.mItems.size()>0 )\n"
              + "      return this.mItems.size();\n"
              + "    else\n"
              + "      return 0;\n"
              + "  }\n"
              + "  @Override\n"
              + "  public   boolean hasItems()\n"
              + "  {\n"
              + "    if(this.mItems!=null && this.mItems.size()>0 )\n"
              + "      return true;\n"
              + "    else\n"
              + "      return false;\n"
              + "  }\n"
              + "  @Override\n"
              + "  public   boolean hasThisItem(int index){\n"
              + "    if(this.mItems!=null && this.mItems.size()>index )\n"
              + "      return true;\n"
              + "    else\n"
              + "      return false;\n"
              + "  }"
              + ""
              + "}");
      //todo ver si una vez instanciado el wrapper no ir a buscarlo de nuevo a disco
      builderDataBaseMaster.append(dataTableClassName
          + "Wrapper m"
          + aliasTable
          + ";\n"
          + "  public "
          + dataTableClassName
          + "Wrapper "
          + aliasTable
          + "() {"
          + "\nif(m"
          + aliasTable
          + "==null){"
          + "\n"
          + "    ArrayList<"
          + dataTableClassName
          + "> listItems = (ArrayList<"
          + dataTableClassName
          + ">) mDBEngine.loadItemsTable(\""
          + aliasTable
          + "\"+ \"|\" +"
          + aliasTable
          + "FieldsHashCode);\n"
          + "    m"
          + aliasTable
          + " = new "
          + dataTableClassName
          + "Wrapper (new "
          + dataTableClassName
          + "(), mDBEngine, \""
          + aliasTable
          + "\");\n"
          + "    m"
          + aliasTable
          + ".setItems(listItems);\n}"
          + "    return  m"
          + aliasTable
          + ";\n"
          + "  }");

      MigrateDBMethodBody = MigrateDBMethodBody + "this." + aliasTable + "().save();\n";
      try { // write the file
        JavaFileObject source = processingEnv.getFiler()
            .createSourceFile("com.gigigo.dbmodule.generated." + dataTableClassName + "Wrapper");

        Writer writer = source.openWriter();
        writer.write(builder.toString());
        writer.flush();
        writer.close();
      } catch (IOException e) {
        // Note: calling e.printStackTrace() will print IO errors
        // that occur from the file already existing after its first run, this is normal
      }
    }

    builderDataBaseMaster.append(MigrateDBMethodIni + MigrateDBMethodBody + "}\n\n }");
    //chamo falta terminar lo del for de myDB, y despues cerrar la clase y por ultimo escribir en el
    //file, falta tb mejorar la legibilidad des esto, lo primero una funcion de
    //escribir clase, pasamdo nombre y builder y crear regiones para encapsular las distintas areas

    try { // write the file
      JavaFileObject source =
          processingEnv.getFiler().createSourceFile("com.gigigo.dbmodule.generated.MyDB");

      Writer writer = source.openWriter();
      writer.write(builderDataBaseMaster.toString());
      writer.flush();
      writer.close();
    } catch (IOException e) {
      // Note: calling e.printStackTrace() will print IO errors
      // that occur from the file already existing after its first run, this is normal
    }

    return true;
  }

  //@Override public int hashCode(Object obj) {
  //
  //
  //  for (final Field field : clazz.getDeclaredFields()) {
  //  int megaHash = 0;
  //  megaHash = megaHash + hashCodeObject(this.getName());
  //  megaHash = megaHash + hashCodeObject(this.getSurname());
  //  megaHash = megaHash + hashCodeObject(this.getId());
  //  return megaHash;
  //}

  public static int hashCodeObject(Object o) {
    return (o == null) ? 0 : o.hashCode();
  }
}
