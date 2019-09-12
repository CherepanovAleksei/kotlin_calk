import java.lang.Exception
import java.util.*
val variables: MutableMap<String, Int> = mutableMapOf<String, Int>()

fun main() {
    var newExpression: String?
    while(true) {
        print(">>>")
        newExpression = readLine()
        if(newExpression == null || newExpression == ""){
            continue
        }
        val cutExp = newExpression.split("=")
        try {
            if (cutExp[0] == newExpression) {
                println(countPolishExpr(infixToPolishNotation(newExpression)))
            } else {
                val cutExp2 = cutExp[0].split("let ")
                if (cutExp2[0] != cutExp[0]) {
                    variables[cutExp2[1].trimEnd()] = countPolishExpr(infixToPolishNotation(cutExp[1]))
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}

fun countPolishExpr(stack:LinkedList<Any>):Int{
    var countStack = LinkedList<Int>()
    while(stack.isNotEmpty()){
        var top = stack.pop()
        when (top) {
            '+' -> {
                var a = countStack.pop()
                var b = countStack.pop()
                countStack.push(a+b)
            }
            '-' -> {
                var a = countStack.pop()
                var b = countStack.pop()
                countStack.push(b-a)
            }
            '*' -> {
                var a = countStack.pop()
                var b = countStack.pop()
                countStack.push(a*b)
            }
            '/' -> {
                var a = countStack.pop()
                var b = countStack.pop()
                countStack.push(b/a)
            }
            else -> {
                countStack.push(top.toString().toInt())
            }
        }
    }
    return countStack.first
}

fun infixToPolishNotation(expr: String):LinkedList<Any>{
    val outStack = LinkedList<Any>()
    val operationStack = LinkedList<Char>()
    var stringNumber = ""
    var stringVariable = ""
    fun check(){

        if(stringVariable != ""){
            val variableValue = variables[stringVariable]

            if(variableValue != null){
                outStack.push(variableValue)
                stringVariable = ""
            } else {
                throw Exception("No such variable")
            }
        } else if(stringNumber != ""){
            outStack.push(stringNumber.toInt())
            stringNumber = ""
        }
    }
    fun getPriority(topStack:Char, operation:Char): Boolean{
        if(topStack == '*' || topStack == '/') {
            return true
        } else if (topStack == '+' || topStack == '-'){
            return operation == '+' || operation == '-'
        }
        return false
    }
    loop@ for(i:Char in expr){
        when (i) {
            ' ' -> {
                check()
                continue@loop
            }
            '+', '-', '*', '/' -> {
                check()
                if(operationStack.isEmpty()){
                    operationStack.push(i)
                    continue@loop
                }
                var topStack = operationStack.first
                while(getPriority(topStack, i)){
                    outStack.push(operationStack.pop())
                    if(operationStack.isEmpty()){
                        break
                    }
                    topStack = operationStack.first
                }
                operationStack.push(i)
            }
            '(' -> {
                check()
                operationStack.push('(')
            }
            ')' -> {
                check()
                var operation = operationStack.pop()
                while(operation != '('){
                    outStack.push(operation)
                    operation = operationStack.pop()
                }
                //TODO: ERROR whith bracets
            }
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' -> {
                stringNumber += i
            }
            else -> {
                stringVariable += i
            }
        }
    }
    check()
    for(operation in operationStack){
        outStack.push(operation)
    }
    outStack.reverse()
//    for(i in outStack){
//        print(i)
//    }
//    println()

    return outStack
}

// 3 + 4 * 2 / (1 - 5)
// (1+2)*4+3
// let v = (1+2)*4+3