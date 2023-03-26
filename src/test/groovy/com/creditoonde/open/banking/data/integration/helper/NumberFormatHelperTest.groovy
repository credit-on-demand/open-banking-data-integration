package com.creditoonde.open.banking.data.integration.helper

import spock.lang.Specification
import spock.lang.Unroll

class NumberFormatHelperTest extends Specification {

    @Unroll
    def 'Should return expected double value for valid input'() {
        when:
        def result = NumberFormatHelper.formatToDouble(value)

        then:
        result == expectedValue

        where:
        value        | expectedValue
        '5.5'        | 550.0
        '1.23456789' | 123.456789
        '0.1'        | 10.0
        '0.01'       | 1.0
        '0.001'      | 0.1
        '0.0001'     | 0.01
    }

    @Unroll
    def 'formatToDouble should throw NumberFormatException for invalid input: #value'() {
        when:
        NumberFormatHelper.formatToDouble(value)

        then:
        def e = thrown(NumberFormatException)
        e.getMessage() == message

        where:
        value      | message
        '-5.5'     | 'Value is invalid.'
        '0'        | 'Value is invalid.'
        '-0.00001' | 'Value is invalid.'
        null       | 'Value cannot be null.'
    }
}
