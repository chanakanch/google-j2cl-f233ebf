/*
 * Copyright 2015 Google Inc.
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
package com.google.j2cl.ast;

import java.util.List;

/**
 * A general Visitor class to traverse/modify the AST.
 */
public class VisitorUtils {
  @SuppressWarnings("unchecked")
  static <T extends Node> void visitList(Processor processor, List<T> nodeList) {
    for (int i = 0; i < nodeList.size(); i++) {
      T oldNode = nodeList.get(i);
      T newNode = (T) oldNode.accept(processor);
      if (newNode == null) {
        // Node is removed from list.
        nodeList.remove(i);
        i--;
        continue;
      }
      if (newNode != oldNode) {
        // Node is replaced.
        nodeList.set(i, newNode);
      }
    }
  }

  private VisitorUtils() {}
}
