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
package com.google.j2cl.transpiler.integration.arrayleafinsertion;

import static com.google.j2cl.transpiler.utils.Asserts.fail;

public class Main {
  public static void main(String... args) {
    testFullArray();
    testPartialArray();
  }

  private static void testFullArray() {
    Object[] array = new HasName[2];

    // You can insert a leaf value of a different but conforming type.
    array[0] = new Person();

    // When inserting a leaf value the type must conform.
    try {
      array[0] = new Object();
      fail("An expected failure did not occur.");
    } catch (ArrayStoreException e) {
      // expected
    }

    // You can always insert null.
    array[0] = null;
  }

  private static void testPartialArray() {
    // You can create a partially initialized array.
    Object[][] partialArray = new Object[1][];

    // When trying to insert into the uninitialized section you'll get an NPE.
    try {
      partialArray[0][0] = new Person();
      fail("An expected failure did not occur.");
    } catch (NullPointerException e) {
      // expected
    }

    // You can replace it with a fully initialized array.
    partialArray = new Object[1][1];

    // And then insert a leaf value that conforms to the strictest leaf type
    partialArray[0][0] = new Person();
    partialArray[0][0] = null;
  }
}
