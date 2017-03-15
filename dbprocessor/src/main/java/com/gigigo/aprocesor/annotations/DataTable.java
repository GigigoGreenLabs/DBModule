package com.gigigo.aprocesor.annotations;

/**
 * Created by nubor on 03/03/2017.
 */
public @interface DataTable {
  String alias();
  boolean isOnlyOneRecord();//asv es para si queremos q ese wrapper solo sea un recurso
}
