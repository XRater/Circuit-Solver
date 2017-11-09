package ru.spbau.mit.circuit.logic;


final class Fractional implements Algebra {
    private final int nominator;
    private final int denominator;

    Fractional(int a) {
        nominator = a;
        denominator = 1;
    }

    Fractional(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException();
        }
        this.nominator = a;
        this.denominator = b;
    }

    private static int gcd(int a, int b) {
        return a == 0 ? b : gcd(b % a, a);
    }

    @Override
    public boolean isZero() {
        return nominator == 0;
    }

    @Override
    public Fractional add(Algebra f) {
        Fractional frac = (Fractional) f;
        return new Fractional(
                nominator * frac.denominator + denominator * frac.nominator,
                denominator * frac.denominator).simplify();
    }

    @Override
    public Fractional mul(Algebra f) {
        Fractional frac = (Fractional) f;
        return new Fractional(
                nominator * frac.nominator,
                denominator * frac.denominator).simplify();
    }

    @Override
    public Fractional div(Algebra f) {
        return this.mul(f.inverse());
    }

    @Override
    public Fractional sub(Algebra o) {
        return this.add(o.neg());
    }

    @Override
    public Fractional inverse() {
        return new Fractional(denominator, nominator);
    }

    @Override
    public Fractional neg() {
        return new Fractional(-nominator, denominator).simplify();
    }

    Fractional simplify() {
        int newa = nominator;
        int newb = denominator;
        int d = gcd(Math.abs(nominator), Math.abs(denominator));
        newa /= d;
        newb /= d;
        return newb < 0 ? new Fractional(-newa, -newb) : new Fractional(newa, newb);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Fractional)) {
            return false;
        }
        Fractional a1 = this.simplify();
        Fractional a2 = ((Fractional) obj).simplify();
        return a1.nominator == a2.nominator && a1.denominator == a2.denominator;
    }

    @Override
    public String toString() {
        return nominator + "/" + denominator;
    }


}
