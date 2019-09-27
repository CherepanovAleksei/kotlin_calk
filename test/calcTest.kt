import org.junit.jupiter.api.Test

internal class CalculatorTest {
    @Test
    fun countTest(){
        val calc = Calculator()
        assert(calc.count("2+5") == 7)
        assert(calc.count("3 + 4 * 2 / (1 - 5)") == 1)
        assert(calc.count("(1+2)*4+3") == 15)
        assert(calc.count("let v = (1+2)*4+3") == null)
        assert(calc.count("v+5") == 20)
        assert(calc.count("-12") == -12)
        calc.count("let       a      =       -9 ")
        assert(calc.count("45/a") == -5)
    }
}