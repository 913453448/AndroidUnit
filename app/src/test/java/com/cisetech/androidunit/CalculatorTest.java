package com.cisetech.androidunit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * author：yinqingy
 * date：2016-11-13 21:08
 * blog：http://blog.csdn.net/vv_bug
 * desc：
 */
public class CalculatorTest {
    private Calculator mCalculator;

    @Before
    public void setUp() throws Exception {
        mCalculator = new Calculator();
    }

    @Test
    public void testSum() throws Exception {
        assertEquals("1+5=6",6d, mCalculator.sum(1d, 5d), 0);
    }

    @Test
    public void testSubstract() throws Exception {
        assertEquals("5-4=1",2d, mCalculator.substract(5d, 4d), 0);
    }

    @Test
    public void testDivide() throws Exception {
        assertEquals("20/5=4",4d, mCalculator.divide(20d, 5d), 0);
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals("2*5=10",0, mCalculator.multiply(2d, 5d), 0);
    }
}