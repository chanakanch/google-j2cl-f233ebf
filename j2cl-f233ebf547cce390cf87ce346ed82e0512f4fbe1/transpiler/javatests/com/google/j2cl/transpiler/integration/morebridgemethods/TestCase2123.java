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
package com.google.j2cl.transpiler.integration.morebridgemethods;

import static com.google.j2cl.transpiler.utils.Asserts.assertTrue;

public class TestCase2123 {
  static interface BI1 {
    @SuppressWarnings("unused")
    default String get(String value) {
      return "BI1 get String";
    }
  }

  static class B implements BI1 {}

  static class C extends B {
    @Override
    public String get(String value) {
      return "C get String";
    }
  }

  @SuppressWarnings("unchecked")
  public static void test() {
    C c = new C();
    assertTrue(((B) c).get("").equals("C get String"));
    assertTrue(c.get("").equals("C get String"));
    assertTrue(((BI1) c).get("").equals("C get String"));
  }
}
