package ru.spbau.mit.circuit.logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FractionalTest {

    @Test
    public void testSimplify() {
        assertEquals(new Fractional(1, 2), new Fractional(2, 4).simplify());
        assertEquals(new Fractional(1, 2), new Fractional(-2, -4).simplify());
        assertEquals(new Fractional(0, 1), new Fractional(0, 14).simplify());
        assertEquals(new Fractional(0, 1), new Fractional(0, -4).simplify());
        assertEquals(new Fractional(-2, 3), new Fractional(-10, 15).simplify());
        assertEquals(new Fractional(-7, 2), new Fractional(21, -6).simplify());
    }

    @Test
    public void testNeg() {
        assertEquals(new Fractional(1, 1), new Fractional(-1, 1).neg());
        assertEquals(new Fractional(0, 2), new Fractional(0, -7).neg());
        assertEquals(new Fractional(-3, 2), new Fractional(6, 4).neg());
    }

    @Test
    public void testAdd() {
        assertEquals(new Fractional(1, 1), new Fractional(-1, 1).add(new Fractional(2, 1)));
        assertEquals(new Fractional(0, 1), new Fractional(-1, 2).add(new Fractional(1, 2)));
        assertEquals(new Fractional(41, 28), new Fractional(5, 7).add(new Fractional(3, 4)));
        assertEquals(new Fractional(1, 2), new Fractional(2, 3).add(new Fractional(1, -6)));
        assertEquals(new Fractional(-3, 4), new Fractional(-2, 3).add(new Fractional(-1, 12)));
    }

    @Test
    public void testMul() {
        assertEquals(new Fractional(1, 2), new Fractional(2, 4).mul(new Fractional(3, 3)));
        assertEquals(new Fractional(-1, 2), new Fractional(2, -4).mul(new Fractional(3, 3)));
        assertEquals(new Fractional(-1, 2), new Fractional(2, 4).mul(new Fractional(-3, 3)));
        assertEquals(new Fractional(1, 2), new Fractional(2, -4).mul(new Fractional(-3, 3)));
        assertEquals(new Fractional(2, 3), new Fractional(5, 6).mul(new Fractional(4, 5)));
    }

    @Test
    public void testInverse() {
        assertEquals(new Fractional(1, 2), new Fractional(2).inverse());
        assertEquals(new Fractional(2, 1), new Fractional(1, 2).inverse());
        assertEquals(new Fractional(-2, 3), new Fractional(6, -4).inverse());
    }
}