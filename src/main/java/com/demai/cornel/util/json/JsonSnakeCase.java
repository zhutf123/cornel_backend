/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util.json;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation which indicates that the annotated case class should be
 * serialized and deserialized using {@code snake_case} JSON field names instead
 * of {@code camelCase} field names.
 * Create By zhutf  19-10-6  下午4:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonSnakeCase {
}