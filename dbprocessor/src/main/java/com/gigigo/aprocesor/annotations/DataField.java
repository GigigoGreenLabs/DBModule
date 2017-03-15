package com.gigigo.aprocesor.annotations;

/**
 * Created by nubor on 03/03/2017.
 */

//asv this kind of field annotation attributes if for engins like sql,realm, ormlite
public @interface DataField {
 //asv match by pojo property name String alias() default "";

  boolean isPrimaryKey() default false;

  boolean isForeignKey() default false;

  boolean isNotNull() default false;

  String defaultValueForField() default "";
}
