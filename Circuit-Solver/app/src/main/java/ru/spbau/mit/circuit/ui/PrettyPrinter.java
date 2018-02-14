package ru.spbau.mit.circuit.ui;

import android.graphics.Canvas;
import android.graphics.Rect;

import ru.spbau.mit.circuit.model.circuitObjects.elements.Element;

import static ru.spbau.mit.circuit.ui.Drawer.ELEMENTS_PAINT;

class PrettyPrinter {
    static void print(Canvas canvas, int x, int y, Element element) {
        String current = element.getCurrent().toString().replace('*', '\u00B7') + "A";
        Rect size = new Rect();
        int pos = current.indexOf("exp(");
        float fontSize = ELEMENTS_PAINT.getTextSize();
        while (pos != -1) {
            // printing everything before exp
            canvas.drawText(current.substring(0, pos + 1), x, y, ELEMENTS_PAINT);
            ELEMENTS_PAINT.getTextBounds(current, 0, pos + 1, size);
            x += size.width();
            ELEMENTS_PAINT.setTextSize(fontSize * 2 / 3);
            current = current.substring(pos + 4);

            //printing exp power
            pos = current.indexOf(")");
            canvas.drawText(current.substring(0, pos), x, y - size.height(), ELEMENTS_PAINT);
            ELEMENTS_PAINT.getTextBounds(current, 0, pos, size);
            x += size.width();
            ELEMENTS_PAINT.setTextSize(fontSize);
            current = current.substring(pos + 1);
            pos = current.indexOf("exp(");
        }

        canvas.drawText(current, x, y, ELEMENTS_PAINT);
    }
}
