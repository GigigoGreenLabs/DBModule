package com.gigigo.aprocesor;

import com.gigigo.aprocesor.annotations.DataTable;
import java.io.IOException;
import java.io.Writer;
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

    StringBuilder builder = new StringBuilder();


    //1º datatables
    for (Element element : roundEnv.getElementsAnnotatedWith(DataTable.class)) {
      String dataTableClassName = element.getSimpleName().toString();
      String importToTableClass = element.asType().toString();
      DataTable dataTableAnnotation = element.getAnnotation(DataTable.class);
      String aliasTable = dataTableAnnotation.alias();
      builder = new StringBuilder();

      builder
          //package
          .append("package com.gigigo.dbmodule.generated;\n\n")
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
              + ">) mDBEngine.loadItemsTable(this.mAlias);\n"
              + "    }\n"
              + "    return mItems;\n"
              + "  }\n"
              + "\n"
              + "  @Override public void setItems(ArrayList<? extends DBTableMaster> items) {\n"
              + "    if (!items.isEmpty() && items.get(0) instanceof "
              + dataTableClassName
              + ") {\n"
              + "      mySetItems((ArrayList<"
              + dataTableClassName
              + ">) items);\n"
              + "    }\n"
              + "  }\n"
              + "\n"
              + "  public void mySetItems(ArrayList<"
              + dataTableClassName
              + "> items) {\n"
              + "    mItems = (ArrayList<"
              + dataTableClassName
              + ">) items;\n"
              + "    mDBEngine.saveTable(this, this.mAlias);\n"
              + "  }\n"
              + "\n"
              + "  @Override public void delItem(int idx) {\n"
              + "    mItems = getItems();\n"
              + "    mItems.remove(idx);\n"
              + "    setItems(mItems);\n"
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
              + "        mItems.remove(delIDX);\n"
              + "        setItems(mItems);\n"
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
              + "    if (item instanceof "+ dataTableClassName+") {\n"
              + "      mItems = getItems();\n"
              + "      mItems.add(("+ dataTableClassName+") item);\n"
              + "      setItems(mItems);\n"
              + "    }\n"
              + "  }\n"
              + "\n"
              + "  @Override public Query<"+ dataTableClassName+"> FROM() {\n"
              + "    Query<"+ dataTableClassName+"> from = from(this.mItems);\n"
              + "    return from;\n"
              + "  } \n \n\n}");


    // for each javax.lang.model.element.Element annotated with the CustomAnnotation
   /* for (Element element : roundEnv.getElementsAnnotatedWith(DataTable.class)) {
      String objectType = element.getSimpleName().toString();
      // this is appending to the return statement
      builder.append(objectType).append(" says hello!\\n");
    }
    builder.append("\";\n") // end return
        .append("\t}\n") // close method
        .append("}\n"); // close class
*/
    try { // write the file
      JavaFileObject source =
          processingEnv.getFiler().createSourceFile("com.gigigo.dbmodule.generated."+ dataTableClassName + "Wrapper");

      Writer writer = source.openWriter();
      writer.write(builder.toString());
      writer.flush();
      writer.close();
    } catch (IOException e) {
      // Note: calling e.printStackTrace() will print IO errors
      // that occur from the file already existing after its first run, this is normal
    }
    }
    return true;
  }
}
