package ru.spbau.mit.circuit.logic.system_solving.polynoms;


public interface Monom<T> extends Linear<Monom<T>> {

    double coefficient();

    T value();

//    @Override
//    default int compareTo(@NonNull Monom<T> o) {
//        return value().compareTo(o.value());
//    }
}
