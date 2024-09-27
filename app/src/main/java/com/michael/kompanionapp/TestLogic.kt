package com.michael.kompanionapp

import com.michael.kompanion.utils.conditionalChain

internal class TestLogic {
    init {
        val num = 5
        num.conditionalChain()
            .ifCondition({ it > 10 }) { println("Greater than 10") }
            .ifCondition({ it == 5 }) { println("Equal to 5") }
            .elseCondition { println("None of the conditions met") }

    }

}