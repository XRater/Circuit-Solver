package ru.spbau.mit.circuit.logic.graph;


import android.support.annotation.NonNull;

import java.util.Deque;
import java.util.LinkedList;

class Path {

    private final Deque<Edge> edges = new LinkedList<>();

    @NonNull
    Deque<Edge> edges() {
        return edges;
    }

    void push(@NonNull Edge e) {
        if (edges.size() != 0 && !edges.getLast().adjacent(e)) {
            throw new IllegalArgumentException();
        }
        edges.offerLast(e);
    }

    @SuppressWarnings("unused")
    void pop() {
        edges.pollLast();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Edge edge : edges) {
            sb.append(edge).append("--");
        }
        return sb.toString();
    }
}
