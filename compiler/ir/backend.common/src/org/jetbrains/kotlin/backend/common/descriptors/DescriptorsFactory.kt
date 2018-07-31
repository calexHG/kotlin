/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common.descriptors

import org.jetbrains.kotlin.ir.declarations.*

interface DescriptorsFactory {
    object FIELD_FOR_OUTER_THIS : IrDeclarationOriginImpl("FIELD_FOR_OUTER_THIS")

    fun getSymbolForEnumEntry(enumEntry: IrEnumEntry): IrProperty
    fun getOuterThisFieldSymbol(innerClass: IrClass): IrProperty
    fun getInnerClassConstructorWithOuterThisParameter(innerClassConstructor: IrConstructor): IrConstructor
    fun getSymbolForObjectInstance(singleton: IrClass): IrProperty
}