/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.backend.js.transformers.irToJs

import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.ir.backend.js.utils.JsGenerationContext
import org.jetbrains.kotlin.ir.backend.js.utils.constructedClass
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.js.backend.ast.*

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class IrElementToJsStatementTransformer : BaseIrElementToJsNodeTransformer<JsStatement, JsGenerationContext> {

    override fun visitBlockBody(body: IrBlockBody, context: JsGenerationContext): JsStatement {
        return JsBlock(body.statements.map { it.accept(this, context) })
    }

    override fun visitBlock(expression: IrBlock, context: JsGenerationContext): JsBlock {
        return JsBlock(expression.statements.map { it.accept(this, context) })
    }

    override fun visitComposite(expression: IrComposite, context: JsGenerationContext): JsStatement {
        // TODO introduce JsCompositeBlock?
        return JsBlock(expression.statements.map { it.accept(this, context) })
    }

    override fun visitExpression(expression: IrExpression, context: JsGenerationContext): JsStatement {
        return JsExpressionStatement(expression.accept(IrElementToJsExpressionTransformer(), context))
    }

    override fun visitBreak(jump: IrBreak, context: JsGenerationContext): JsStatement {
        return JsBreak(context.getNameForLoop(jump.loop)?.makeRef())
    }

    override fun visitContinue(jump: IrContinue, context: JsGenerationContext): JsStatement {
        return JsContinue(context.getNameForLoop(jump.loop)?.makeRef())
    }

    override fun visitReturn(expression: IrReturn, context: JsGenerationContext): JsStatement {
        return JsReturn(expression.value.accept(IrElementToJsExpressionTransformer(), context))
    }

    override fun visitThrow(expression: IrThrow, context: JsGenerationContext): JsStatement {
        return JsThrow(expression.value.accept(IrElementToJsExpressionTransformer(), context))
    }

    override fun visitVariable(declaration: IrVariable, context: JsGenerationContext): JsStatement {
        val varName = context.getNameForSymbol(declaration.symbol)
        return jsVar(varName, declaration.initializer, context)
    }

    override fun visitDelegatingConstructorCall(expression: IrDelegatingConstructorCall, context: JsGenerationContext): JsStatement {
        if (KotlinBuiltIns.isAny(expression.symbol.constructedClass)) {
            return JsEmpty
        }
        return expression.accept(IrElementToJsExpressionTransformer(), context).makeStmt()
    }

    override fun visitInstanceInitializerCall(expression: IrInstanceInitializerCall, context: JsGenerationContext): JsStatement {

        // TODO: implement
        return JsEmpty
    }

    override fun visitTry(aTry: IrTry, context: JsGenerationContext): JsStatement {

        val jsTryBlock = aTry.tryResult.accept(this, context).asBlock()

        val jsCatch = aTry.catches.singleOrNull()?.let {
            val name = context.getNameForSymbol(it.catchParameter.symbol)
            val jsCatchBlock = it.result.accept(this, context)
            JsCatch(context.currentScope, name.ident, jsCatchBlock)
        }

        val jsFinallyBlock = aTry.finallyExpression?.accept(this, context)?.asBlock()

        return JsTry(jsTryBlock, jsCatch, jsFinallyBlock)
    }

    override fun visitWhen(expression: IrWhen, context: JsGenerationContext): JsStatement {
        return expression.toJsNode(this, context, ::JsIf) ?: JsEmpty
    }

    override fun visitWhileLoop(loop: IrWhileLoop, context: JsGenerationContext): JsStatement {
        //TODO what if body null?
        val label = context.getNameForLoop(loop)
        val loopStatement = JsWhile(loop.condition.accept(IrElementToJsExpressionTransformer(), context), loop.body?.accept(this, context))
        return label?.let { JsLabel(it, loopStatement) } ?: loopStatement
    }

    override fun visitDoWhileLoop(loop: IrDoWhileLoop, context: JsGenerationContext): JsStatement {
        //TODO what if body null?
        val label = context.getNameForLoop(loop)
        val loopStatement =
            JsDoWhile(loop.condition.accept(IrElementToJsExpressionTransformer(), context), loop.body?.accept(this, context))
        return label?.let { JsLabel(it, loopStatement) } ?: loopStatement
    }

//    override fun visitSuspendableRoot(expression: IrSuspendableRoot, context: JsGenerationContext): JsStatement {
//        val resumePoints = mutableListOf<Pair<Int, JsStatement>>()
//        val coroutineContext = context.newSuspendableContext(resumePoints)
//        val label = coroutineContext.coroutineLabel
//
//        val dispatchExpression = expression.suspensionPointId.accept(IrElementToJsExpressionTransformer(), context)
//
//        val startId = context.nextState()
//
//        val body = expression.result.accept(this, coroutineContext)
//        resumePoints.add(0, Pair(startId, body))
//
//        val defaultStatement = JsDefault().apply { statements += JsInvocation(JsNameRef("Error")).makeStmt() }
//
//        val cases = resumePoints.map { (id, statement) ->
//            val case = JsCase()
//            case.caseExpression = JsIntLiteral(id)
//            case.statements += statement
//            case
//        }
//
//        val stateSwitch = JsSwitch(dispatchExpression, cases + defaultStatement)
//
//        val rootLoop = JsDoWhile(JsBooleanLiteral(true), stateSwitch)
//
//        return JsLabel(label, rootLoop)
//        TODO()
//    }
}
