package com.group.libraryapp.calculator


fun main() {
    val calculator = CalculatorTest()
    calculator.addTest()
    calculator.minusTest()
    calculator.multipleTest()
    calculator.divideTest()
    calculator.divideExceptionTest()
}

class CalculatorTest {

    fun addTest() {
        // given
        val calculator = Calculator(3)

        // when
        calculator.add(5)

        // then
        if (calculator.number != 8) {
            throw IllegalStateException()
        }

        // data class를 사용할 때
//        val expectedCalculator = Calculator(8)
//        if (calculator != expectedCalculator) {
//            throw IllegalStateException()
//        }
    }

    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        if (calculator.number != 2) {
            throw IllegalStateException()
        }
    }

    fun multipleTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiple(3)

        // then
        if (calculator.number != 15) {
            throw IllegalStateException()
        }
    }

    fun divideTest() {
        // given
        val calculator = Calculator(15)

        // when
        calculator.divide(3)

        // then
        if (calculator.number != 5) {
            throw IllegalStateException()
        }
    }

    fun divideExceptionTest() {
        // given
        val calculator = Calculator(15)

        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            if (e.message != "0으로 나눌 수 없습니다.") {
                throw IllegalStateException("메시지가 다릅니다.")
            }
            return
        } catch (e: Exception) {
            throw IllegalStateException()
        }
        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
    }

}