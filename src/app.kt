import java.lang.Exception

fun main() {
    val calc = Calculator()
    var newExpression: String?

    while (true) {
        print(">>> ")
        newExpression = readLine()
        try {
            println(calc.count(newExpression) ?: "")
        } catch (e: Exception) {
            println(e)
        }
    }
}