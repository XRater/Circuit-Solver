package ru.spbau.mit.circuit.logic.math.algebra;

import org.junit.Test;

import ru.spbau.mit.circuit.logic.math.algebra.dummies.DummyOG;
import ru.spbau.mit.circuit.logic.math.algebra.dummies.DummyP;

import static org.junit.Assert.assertEquals;

public class PolyElementTest {

    private DummyP zero = DummyP.zero();

    private DummyP d2_1 = zero.singleton(Numerical.number(2), DummyOG.element(1));
    private DummyP d0_2 = zero.singleton(Numerical.number(0), DummyOG.element(2));
    private DummyP d3_2 = zero.singleton(Numerical.number(3), DummyOG.element(2));
    private DummyP d1_0 = zero.singleton(Numerical.number(1), DummyOG.element(0));
    private DummyP dm1_1 = zero.singleton(Numerical.number(-1), DummyOG.element(1));
    private DummyP dm1_0 = zero.singleton(Numerical.number(-1), DummyOG.element(0));


    @Test
    public void testInit() {
        assertEquals("empty", zero.toString());

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 0) {
                    assertEquals("empty",
                            zero.singleton(Numerical.number(i), DummyOG.element(j)).toString());
                    continue;
                }
                assertEquals(String.valueOf(i) + ".0*" + String.valueOf(j),
                        zero.singleton(Numerical.number(i), DummyOG.element(j)).toString());
            }
        }
    }

    @Test
    public void testAdd() {
        assertEquals("2.0*1", d2_1.toString());
        assertEquals("2.0*1", d2_1.add(d0_2).toString());
        assertEquals("1.0*0 + 2.0*1", d2_1.add(d1_0).toString());
        assertEquals("1.0*0 + 2.0*1", d1_0.add(d2_1).toString());
        assertEquals("1.0*0 + 2.0*1", d2_1.add(d0_2).add(d1_0).toString());
        assertEquals("1.0*1", d2_1.add(dm1_1).toString());
        assertEquals("empty", d2_1.add(dm1_1).add(dm1_1).toString());
        assertEquals("empty", dm1_1.add(d2_1).add(dm1_1).toString());
        assertEquals("-1.0*1", d0_2.add(d0_2).add(dm1_1).toString());
        assertEquals("-1.0*0 + 1.0*1", dm1_0.add(d2_1).add(dm1_1).toString());
    }

    @Test
    public void testMulC() {
        assertEquals("empty",
                d1_0.add(d1_0).add(d2_1).multiplyConstant(Numerical.zero()).toString());
        assertEquals("empty",
                dm1_0.add(dm1_1).add(d0_2).multiplyConstant(Numerical.zero()).toString());

        assertEquals("2.0*0 + 2.0*1",
                d1_0.add(d1_0).add(d2_1).multiplyConstant(Numerical.identity()).toString());
        assertEquals("-1.0*0 + -1.0*1",
                dm1_0.add(dm1_1).add(d0_2).multiplyConstant(Numerical.identity()).toString());

        assertEquals("-2.0*0 + -2.0*1",
                d1_0.add(d1_0).add(d2_1).multiplyConstant(Numerical.number(-1)).toString());
        assertEquals("1.0*0 + 1.0*1",
                dm1_0.add(dm1_1).add(d0_2).multiplyConstant(Numerical.number(-1)).toString());


        assertEquals("4.0*0 + -2.0*1",
                d1_0.add(d1_0).add(dm1_1).multiplyConstant(Numerical.number(2)).toString());
        assertEquals("-2.0*0 + -2.0*1",
                dm1_0.add(dm1_1).add(d0_2).multiplyConstant(Numerical.number(2)).toString());

        assertEquals("-2.0*0 + -2.0*1",
                d1_0.add(d1_0).add(d2_1).negate().toString());
        assertEquals("1.0*0 + 1.0*1",
                dm1_0.add(dm1_1).add(d0_2).negate().toString());
    }

    @Test
    public void testMul() {
        assertEquals("empty",
                d2_1.multiply(d0_2).toString());
        assertEquals("empty",
                d0_2.multiply(d0_2).toString());
        assertEquals("empty",
                d0_2.multiply(d2_1.add(d3_2)).toString());
        assertEquals("empty",
                d3_2.add(d1_0).multiply(d0_2).toString());

        assertEquals("4.0*2",
                d2_1.multiply(d2_1).toString());
        assertEquals("6.0*3",
                d3_2.multiply(d2_1).toString());
        assertEquals("6.0*3",
                d2_1.multiply(d3_2).toString());

        assertEquals("4.0*2 + 6.0*3",
                d3_2.add(d2_1).multiply(d2_1).toString());
        assertEquals("4.0*2 + 6.0*3",
                d2_1.multiply(d2_1.add(d3_2)).toString());

        assertEquals("2.0*1 + 7.0*2 + 6.0*3",
                d3_2.add(d2_1).multiply(d2_1.add(d1_0)).toString());

        assertEquals("-1.0*0 + 4.0*2",
                d2_1.add(d1_0).multiply(d2_1.add(dm1_0)).toString());

    }
}