package com.oops.wallsandwarriors.view;

import com.oops.wallsandwarriors.GameConstants;
import com.oops.wallsandwarriors.util.DrawUtils;
import com.oops.wallsandwarriors.util.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GamePaletteView implements ViewObject {

    @Override
    public void draw(GraphicsContext graphics, double deltaTime) {
        drawSinglePalette(graphics, 
                GameConstants.PALETTE_X, GameConstants.PALETTE_Y,
                GameConstants.PALETTE_WIDTH, GameConstants.PALETTE_HEIGHT);
    }
    
    protected void drawSinglePalette(GraphicsContext graphics,
            double x, double y, double width, double height) {
        graphics.setFill(Color.BEIGE);
        graphics.fillRoundRect(x, y, width, height, 50, 50);
    }
    
    protected static void drawPaletteFrame(GraphicsContext graphics, Rectangle box, boolean isSelected) {
        if (isSelected) {
            DrawUtils.setAttributes(graphics, Color.LIGHTGRAY, Color.YELLOW, 8);
        } else {
            DrawUtils.setAttributes(graphics, Color.LIGHTGRAY, Color.WHITE, 8);
        }
        DrawUtils.drawRoundRect(graphics,
                box.x + GameConstants.PALETTE_MARGIN,
                box.y + GameConstants.PALETTE_MARGIN,
                box.width - 2 * GameConstants.PALETTE_MARGIN,
                box.height - 2 * GameConstants.PALETTE_MARGIN, 50);
    }
    
    protected static Rectangle getPaletteBox(int index) {
        double boxHeight = GameConstants.PALETTE_ELEMENT_HEIGHT;
        double boxWidth = GameConstants.PALETTE_WIDTH;
        Rectangle box = new Rectangle(
                GameConstants.PALETTE_X + (index / GameConstants.PALETTE_ELEMENT_NO) * (boxWidth + GameConstants.PALETTE_MARGIN),
                GameConstants.PALETTE_Y + (index % GameConstants.PALETTE_ELEMENT_NO) * boxHeight,
                boxWidth, boxHeight);
        return box;
    }

}