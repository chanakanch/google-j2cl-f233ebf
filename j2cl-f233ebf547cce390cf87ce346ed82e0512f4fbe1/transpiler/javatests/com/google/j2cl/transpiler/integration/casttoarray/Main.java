/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.j2cl.transpiler.integration.casttoarray;

import static com.google.j2cl.transpiler.utils.Asserts.assertThrowsClassCastException;
import static com.google.j2cl.transpiler.utils.Asserts.assertTrue;

/**
 * Test cast to array type.
 */
public class Main {
  @SuppressWarnings("unused")
  public static void main(String... args) {
    testDimensionCasts();
    testTypeCasts();
    testBasics();
    testErasureCastsOnArrayAccess_fromArrayOfT();
    testErasureCastsOnArrayAccess_fromT();
  }

  private static void testBasics() {
    // Cast null to Object[]
    Object o = (Object[]) null;

    // Cast null to Object[][]
    o = (Object[][]) null;

    // Cast JS "[]" to Object[]
    o = new Object[] {}; // Actually emits as the JS array literal "[]".
    o = (Object[]) o;

    // Cast JS "$Arrays.$init([], Object, 2))" to Object[][]
    o = new Object[][] {};
    o = (Object[][]) o;
  }

  private static void testTypeCasts() {
    Object o = null;

    Object[] objects = new Object[0];
    String[] strings = new String[0];
    CharSequence[] charSequences = new CharSequence[0];

    o = (Object[]) objects;
    o = (Object[]) strings;
    o = (String[]) strings;
    o = (CharSequence[]) strings;
    o = (Object[]) charSequences;
    o = (CharSequence[]) charSequences;

    assertThrowsClassCastException(
        () -> {
          Object unused = (String[]) objects;
        },
        String[].class);

    assertThrowsClassCastException(
        () -> {
          Object unused = (CharSequence[]) objects;
        },
        CharSequence[].class);

    assertThrowsClassCastException(
        () -> {
          Object unused = (String[]) charSequences;
        },
        String[].class);
  }

  private static void testDimensionCasts() {
    Object object = new Object[10][10];

    // These are fine.
    Object[] object1d = (Object[]) object;
    Object[][] object2d = (Object[][]) object;

    // A 2d array cannot be cast to a 3d array.
    assertThrowsClassCastException(
        () -> {
          Object[][][] unused = (Object[][][]) object2d;
        },
        Object[][][].class);

    // A non-array cannot be cast to an array.
    assertThrowsClassCastException(
        () -> {
          Object[] unused = (Object[]) new Object();
        },
        Object[].class);
  }

  private static void testErasureCastsOnArrayAccess_fromArrayOfT() {
    // Array of the right type.
    ArrayContainer<String> stringArrayInArrayContainer = new ArrayContainer<>(new String[1]);
    String unusedString = stringArrayInArrayContainer.data[0];
    int len = stringArrayInArrayContainer.data.length;
    assertTrue(len == 1);

    // Array of the wrong type.
    ArrayContainer<String> objectArrayInArrayContainer = new ArrayContainer<>(new Object[1]);
    assertThrowsClassCastException(
        () -> {
          String unused = objectArrayInArrayContainer.data[0];
        },
        String[].class);
    // Make sure access to the length field performs the right cast. The length field
    // has special handling in CompilationUnitBuider.
    assertThrowsClassCastException(
        () -> {
          int unused = objectArrayInArrayContainer.data.length;
        },
        String[].class);

    // Not even an array.
    assertThrowsClassCastException(
        () -> {
          ArrayContainer<String> container = new ArrayContainer<>(new Object());
        },
        Object[].class);
  }

  private static class ArrayContainer<T> {
    ArrayContainer(Object array) {
      this.data = (T[]) array;
    }

    T[] data;
  }

  private static void testErasureCastsOnArrayAccess_fromT() {
    // Array of the right type.
    Container<String[]> stringArrayInContainer = new Container<>(new String[1]);
    String unusedString = stringArrayInContainer.data[0];
    int len = stringArrayInContainer.data.length;
    assertTrue(len == 1);

    // Array of the wrong type.
    Container<String[]> objectArrayInContainer = new Container<>(new Object[1]);
    assertThrowsClassCastException(
        () -> {
          String unused = objectArrayInContainer.data[0];
        },
        String[].class);
    assertThrowsClassCastException(
        () -> {
          int unused = objectArrayInContainer.data.length;
        },
        String[].class);

    // Not even an array.
    Container<String[]> notAnArrayInContainer = new Container<>(new Object());
    assertThrowsClassCastException(
        () -> {
          String unused = notAnArrayInContainer.data[0];
        },
        String[].class);
    assertThrowsClassCastException(
        () -> {
          int unused = notAnArrayInContainer.data.length;
        },
        String[].class);
  }

  private static class Container<T> {
    Container(Object array) {
      this.data = (T) array;
    }

    T data;
  }
}
