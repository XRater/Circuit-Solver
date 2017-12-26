package ru.spbau.mit.circuit.ui;

import ru.spbau.mit.circuit.MainActivity;
import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.model.elements.Element;
import ru.spbau.mit.circuit.model.elements.Wire;
import ru.spbau.mit.circuit.ui.DrawableElements.Drawable;
import ru.spbau.mit.circuit.ui.DrawableElements.DrawableWire;

public class Uploader {
    public static void load(DrawableModel drawableModel) {
        Model model = MainActivity.ui.getModel();
        for (Element element : model.elements()) {
            drawableModel.loadElement((Drawable) element);
        }
        for (Wire wire : model.wires()) {
            DrawableNode to = (DrawableNode) DrawableModel.getByPoint(wire.to().position());
            if (to == null) {
                drawableModel.addRealNode((DrawableNode) wire.to());
            }
            DrawableNode from = (DrawableNode) DrawableModel.getByPoint(wire.from().position());
            if (from == null) {
                drawableModel.addRealNode((DrawableNode) wire.from());
            }
            drawableModel.loadWire((DrawableWire) wire);
        }
//        for (Element element : model.elements()) {
//            if (element instanceof Resistor) {
//                DrawableResistor resistor = new DrawableResistor(element.center());
//                if (element.isVertical()) {
//                    resistor.flip();
//                }
//                drawableModel.addElement(resistor);
//            }
//
//            if (element instanceof Capacitor) {
//                DrawableCapacitor capacitor = new DrawableCapacitor(element.center());
//                if (element.isVertical()) {
//                    capacitor.flip();
//                }
//                drawableModel.addElement(capacitor);
//            }
//
//            if (element instanceof Battery) {
//                DrawableBattery battery = new DrawableBattery(element.center());
//                if (element.isVertical()) {
//                    battery.flip();
//                }
//                drawableModel.addElement(battery);
//            }
//        }
//
//        for (Node node : model.nodes()) {
//            if (drawableModel.getByPoint(node.position()) == null) {
//
//            }
//        }
//        ArrayList<CircuitObject> toBeAdded = new ArrayList<>();
//        for (Wire wire : model.wires()) {
//            DrawableNode to = (DrawableNode) DrawableModel.getByPoint(wire.to().position());
//            if (to == null) {
//                to = new DrawableNode(wire.to().position(), true);
//                drawableModel.addRealNode(to);
//                toBeAdded.add(to);
//            }
//            DrawableNode from = (DrawableNode) DrawableModel.getByPoint(wire.from().position());
//            if (from == null) {
//                from = new DrawableNode(wire.from().position(), true);
//                drawableModel.addRealNode(from);
//                toBeAdded.add(from);
//            }
//            try {
//                DrawableWire drawableWire = new DrawableWire(to, from);
//                drawableModel.addWire(drawableWire);
//                toBeAdded.add(drawableWire);
//            } catch (IllegalWireException e) {
//                e.printStackTrace();
//            }
//        }
//
//        MainActivity.ui.clearModel();
//        try {
//            MainActivity.ui.addToModel(toBeAdded);
//        } catch (NodesAreAlreadyConnected nodesAreAlreadyConnected) {
//            nodesAreAlreadyConnected.printStackTrace();
//        }
    }

}
