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
package com.google.j2cl.transpiler.readable.libraryinfo;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;

public class Main {
  public static String STATIC_FIELD = "STATIC_FIELD";

  interface FunctionnalInterface {
    void foo();
  }

  interface JsAccessibleFunctionnalInterface {
    @JsMethod
    void accessibleFunctionalInterfaceMethod();
  }

  @JsFunction
  interface Function {
    void apply(String s);
  }

  private static final class FunctionImpl implements Function {
    public void apply(String s) {}
  }

  @JsMethod
  public static void entryPoint() {
    new Main().execute();

    Function jsFunction = new FunctionImpl();
    jsFunction = s -> log(s);
    jsFunction.apply("foo");
  }

  private void execute() {
    log("Foo");
  }

  @Override
  public String toString() {
    return super.toString();
  }

  @JsMethod(namespace = "console")
  public static native void log(Object o);
}
