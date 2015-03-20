package com.cho.chove.core.annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Using Export Excel column Annotation into Apache poi
 * @author ChoHoyoung
 * @since 2015.03.20
 * @version 0.1
 * @see
 *
 * <pre>
 * << Modification Information >>
 *   
 *  UpdateDate  Updator     Description
 *  ----------  ----------  ---------------------------
 *  2014.03.20  ChoHoyoung  initialize
 *
 * </pre>
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
	String value();
}
