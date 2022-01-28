package com.company.UnitTests;

import com.company.GameLogic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MouseClickTests {

    GameLogic gameLogic = new GameLogic(null);
    private double mouseX = 100, mouseY = 100;

    @Test
    void isItClickOnBoardTest() {
        assertTrue(gameLogic.isItClickOnBoard(mouseX, mouseY));
        mouseX = 10; mouseY = 10;
        assertFalse(gameLogic.isItClickOnBoard(mouseX, mouseY));
    }

    private int x1 = 1, y1 = 0, x2 = 1, y2 = 0;

    @Test
    void isItSameClickCoordinateTest() {
        assertTrue(gameLogic.isItSameClickPos(x1, y1, x2, y2));
        x2 = 3;
        assertFalse(gameLogic.isItSameClickPos(x1, y1, x2, y2));
    }


}