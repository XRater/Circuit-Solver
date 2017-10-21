package ru.spbau.mit.circuit.logic;


import java.util.ArrayList;

public class EquationSystem { //TODO invalid arguments

    private final int n;
    private final Vector[] equations;

    EquationSystem(Vector[] equations) {
        n = equations.length;
        this.equations = new Vector[n];
        System.arraycopy(equations, 0, this.equations, 0, equations.length);
    }

    Vector solve() {
        normalize();
        ArrayList<Fractional> ans = new ArrayList<>();
        for (int i = 0; i < n; i++)
            ans.add(equations[i].getCoord(i));
        return new Vector(ans);
    }

    private void normalize() {
        for (int i = 0; i < n - 1; i++) {
            for (int j = i; j < n; j++) {
                if (!equations[j].getCoord(i).isZero()) {
                    swapEquations(i, j);
                    break;
                }
            }
            Fractional k = equations[i].getCoord(i).neg();
            if (k.isZero())
                continue;
            for (int j = i + 1; j < n; j++) {
                equations[j].normalize();
                equations[j].mul(k);
                equations[j].add(equations[i]);
            }
        }
    }

    private void swapEquations(int i, int j) {
        Vector tmp = equations[i];
        equations[i] = equations[j];
        equations[j] = tmp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(equations[i]);
            sb.append('\n');
        }
        return sb.toString();
    }
}
