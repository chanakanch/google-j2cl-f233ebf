/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package java.util;

import javaemul.internal.JsUtils;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * A factory to create JavaScript Map instances.
 * This is a simpler version than GWT as polyfill is not needed, instead provided by Closure.
 */
class InternalJsMapFactory {

  @JsType(isNative = true, name = "Map", namespace = JsPackage.GLOBAL)
  private static class NativeMap<V> {}

  public static  <V> InternalJsMap<V> newJsMap() {
    // We know that NativeMap implements InternalJsMap contract but we don't want to put a bunch
    // stubs by adding implements above.
    return JsUtils.uncheckedCast(new NativeMap<V>());
  }
}
