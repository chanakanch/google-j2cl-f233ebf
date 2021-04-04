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
package com.google.j2cl.ast.visitors;

import com.google.j2cl.ast.AbstractVisitor;
import com.google.j2cl.ast.AstUtils;
import com.google.j2cl.ast.CompilationUnit;
import com.google.j2cl.ast.Method;
import com.google.j2cl.ast.MethodDescriptor;
import com.google.j2cl.ast.Type;

/**
 * Creates a default constructor for class that does not have any explicit constructors.
 *
 * <p>The default constructor has an empty parameter list and an empty body. Its visibility is the
 * same as the visibility of the class.
 */
public class CreateDefaultConstructors extends NormalizationPass {
  @Override
  public void applyTo(CompilationUnit compilationUnit) {
    compilationUnit.accept(
        new AbstractVisitor() {
          @Override
          public void exitType(Type type) {
            if (type.isInterface() || type.getMethods().stream().anyMatch(Method::isConstructor)) {
              // It is an interface or already has a constructor.
              return;
            }

            synthesizeDefaultConstructor(type);
          }

          private void synthesizeDefaultConstructor(Type type) {
            MethodDescriptor methodDescriptor =
                AstUtils.createImplicitConstructorDescriptor(type.getTypeDescriptor());
            type.addMethod(
                0,
                Method.newBuilder()
                    .setMethodDescriptor(methodDescriptor)
                    .setSourcePosition(type.getSourcePosition())
                    .build());
          }
        });
  }

}
