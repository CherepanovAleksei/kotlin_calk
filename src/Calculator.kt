import java.lang.Exception
import java.util.*

class Calculator {
    private val variables: MutableMap<String, Int> = mutableMapOf()

    fun count(newExpression: String?): Int? {

        if (newExpression == null || newExpression == "") {
            return null
        }

        val cutExp = newExpression.split("=")

        if (cutExp[0] == newExpression) {
            //обычное выражение без присваивания - считаем
            return countPolishExpr(infixToPolishNotation(newExpression))
        } else {
            val cutExp2 = cutExp[0].split("let ")
            if (cutExp2[0] != cutExp[0]) {
                //создаем переменную и считаем ее значение
                variables[cutExp2[1].trim()] = countPolishExpr(infixToPolishNotation(cutExp[1]))
            }
            return null
        }
    }

    //калькулятор для выражений в польской записи
    private fun countPolishExpr(stack: LinkedList<Any>): Int {
        val countStack = LinkedList<Int>()
        while (stack.isNotEmpty()) {
            try {
                when (val top = stack.pop()) {
                    '+' -> {
                        val a = countStack.pop()
                        val b = countStack.pop()
                        countStack.push(a + b)
                    }
                    '-' -> {
                        val a = countStack.pop()
                        val b = countStack.pop()
                        countStack.push(b - a)
                    }
                    '*' -> {
                        val a = countStack.pop()
                        val b = countStack.pop()
                        countStack.push(a * b)
                    }
                    '/' -> {
                        val a = countStack.pop()
                        if (a == 0) throw Exception("Devide by zero!!!")
                        val b = countStack.pop()
                        countStack.push(b / a)
                    }
                    else -> {
                        countStack.push(top.toString().toInt())
                    }
                }
            } catch (e: Exception){
                throw Exception("Wrong input expression!!!")
            }
        }

        return countStack.first
    }

    //инфиксное представление to польская запись + подставление переменнных
    private fun infixToPolishNotation(expr: String): LinkedList<Any> {
        val outStack = LinkedList<Any>()
        val operationStack = LinkedList<Char>()
        var stringNumber = ""
        var stringVariable = ""
        var isMinusPrefix = true
        var minus = false

        fun check() {
            if (stringVariable != "") {
                var variableValue = variables[stringVariable]

                if (variableValue != null) {
                    if (minus) {
                        variableValue = -variableValue
                        minus = false
                    }
                    outStack.push(variableValue)
                    stringVariable = ""
                } else {
                    throw Exception("No such variable")
                }
            } else if (stringNumber != "") {
                var number = stringNumber.toInt()
                if (minus) {
                    number = -number
                    minus = false
                }
                outStack.push(number)
                stringNumber = ""
            }

        }

        fun getPriority(topStack: Char, operation: Char): Boolean {
            if (topStack == '*' || topStack == '/') {
                return true
            } else if (topStack == '+' || topStack == '-') {
                return operation == '+' || operation == '-'
            }
            return false
        }

        loop@ for (i: Char in expr) {
            when (i) {
                ' ' -> {
                    check()
                    continue@loop
                }
                '+', '-', '*', '/' -> {
                    if(isMinusPrefix && i == '-'){
                        minus = true
                        continue@loop
                    }
                    isMinusPrefix = true

                    check()
                    if (operationStack.isEmpty()) {
                        operationStack.push(i)
                        continue@loop
                    }
                    var topStack = operationStack.first
                    while (getPriority(topStack, i)) {
                        outStack.push(operationStack.pop())
                        if (operationStack.isEmpty()) {
                            break
                        }
                        topStack = operationStack.first
                    }
                    operationStack.push(i)
                }
                '(' -> {
                    isMinusPrefix = true
                    check()
                    operationStack.push('(')
                }
                ')' -> {
                    isMinusPrefix = false
                    check()
                    if (operationStack.isEmpty()) {
                        throw Exception("Wrong input! Check brackets!")
                    }
                    var operation = operationStack.pop()
                    while (operation != '(') {
                        outStack.push(operation)
                        if (operationStack.isEmpty()) {
                            throw Exception("Wrong input! Check brackets!")
                        }
                        operation = operationStack.pop()
                    }

                }
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' -> {
                    isMinusPrefix = false
                    stringNumber += i
                }
                else -> {
                    isMinusPrefix = false
                    stringVariable += i
                }
            }
        }
        check()
        for (operation in operationStack) {
            outStack.push(operation)
        }
        outStack.reverse()
        return outStack
    }
}