package ru.spbau.mit.circuit.logic.matrix_exponent;


import org.apache.commons.math3.Field;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.spbau.mit.circuit.logic.gauss.functions1.Function;
import ru.spbau.mit.circuit.logic.gauss.functions1.Functions;

class BlockArray {

    private static final Field<Function> functionField = Functions.constant(0).getField();

    ArrayList<Block> blocks = new ArrayList<>();

    public BlockArray(Map<Complex, Integer> roots) {
        for (Map.Entry<Complex, Integer> entry : roots.entrySet()) {
            blocks.add(new Block(entry.getValue(), entry.getKey()));
        }
    }

    public Polynom<Function> first() {
        if (blocks.size() == 0) {
            throw new NoSuchElementException();
        }
        return blocks.get(0).value;
    }

    public void next() {
        ArrayList<Block> newList = new ArrayList<>();

        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (block.size > 1) {
                newList.add(new Block(block.size - 1, block.value));
            }
            if (i != blocks.size() - 1) {
                newList.add(new Block(1, blocks.get(i + 1).value.subtract(block.value)));
            }
        }

        blocks = newList;
    }

    private static class Block {
        private int size;
        private Polynom<Function> value;

        public Block(int size, Polynom<Function> value) {
            this.size = size;
            this.value = value;
        }

        public Block(Integer size, Complex lambda) {
            this.size = size;
            if (lambda.getImaginary() != 0) {
                throw new NotYetException();
            }
            this.value = new Polynom<>(functionField,
                    Collections.singletonList(Functions.exponent(lambda.getReal())));
        }
    }
}
