package com.example.tomek.androidtetrisjava.GameLogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.tomek.androidtetrisjava.R;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {

    private Paint paint = new Paint();
    private float measuredWidth;
    private float measuredHeight;

    private boolean[][] board = new boolean[20][10];

    // The information of the current block
    private int currentX;
    private int currentY;
    //type of the current block
    private int currentType;
    //how many times is block rotated
    private int currentRotateTimes;
    private List<Point> currentCoordinates = new ArrayList<>();

    // required constructors
    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet a) {
        super(context, a);
    }

    public GameView(Context context, AttributeSet a, int b) {
        super(context, a, b);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background
        canvas.drawColor(getResources().getColor(R.color.color_main_view_background));

        // Draw the small pixels
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                // Change the color of the paint
                if (board[i][j]) {
                    this.paint.setColor(this.getResources().getColor(R.color.color_solid_pixel));
                } else {
                    this.paint.setColor(this.getResources().getColor(R.color.color_empty_pixel));
                }

                // Draw the rectangle using the paint
                this.drawRectAtGivenPosition(i, j, canvas, paint);
            }
        }
    }

    /**
     * Draws the rectrangle on the given position
     * @param i x
     * @param j y
     * @param canvas canvas to paint on
     * @param paint paint to be used to paint on Canvas
     */

    private void drawRectAtGivenPosition(int i, int j, Canvas canvas, Paint paint) {
        // Calculate the unit of length
        float unit = 0.0f;
        float margin = 0.0f;
        if (this.measuredHeight >= this.measuredWidth * 2) {
            unit = this.measuredWidth / 10;
            margin = this.measuredWidth / 100;
        } else {
            unit = this.measuredHeight / 20;
            margin = this.measuredHeight / 200;
        }

        float left = unit * j;
        float top = unit * i;
        float right = unit * (j + 1);
        float bottom = unit * (i + 1);
        RectF rectF = new RectF(left + margin, top + margin, right - margin, bottom - margin);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectF, paint);

        Path path = new Path();
        path.moveTo(left + margin * 1 / 3, top + margin * 1 / 3);
        path.lineTo(right - margin * 1 / 3, top + margin * 1 / 3);
        path.lineTo(right - margin * 1 / 3, bottom - margin * 1 / 3);
        path.lineTo(left + margin * 1 / 3, bottom - margin * 1 / 3);
        path.lineTo(left + margin * 1 / 3, top + margin * 1 / 3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    // update measuredWidth and measuredHeight when the view changes size
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.measuredWidth = this.getMeasuredWidth();
        this.measuredHeight = this.getMeasuredHeight();
    }

    public void normalDown() {
        this.currentX += 1;
        // 'remove' old coordinates
        for (Point coordinate : this.currentCoordinates) {
            this.board[coordinate.x][coordinate.y] = false;
        }
        // update & occupy new coordinates
        for (Point coordinate : this.currentCoordinates) {
            coordinate.x += 1;
            this.board[coordinate.x][coordinate.y] = true;
        }

        this.invalidate();
    }

    public void moveLeft() {
        this.currentY -= 1;
        for (Point coordinate : this.currentCoordinates) {
            this.board[coordinate.x][coordinate.y] = false;
        }
        for (Point coordinate : this.currentCoordinates) {
            coordinate.y -= 1;
            this.board[coordinate.x][coordinate.y] = true;
        }

        this.invalidate();
    }

    public void moveRight() {
        this.currentY += 1;
        for (Point coordinate : this.currentCoordinates) {
            this.board[coordinate.x][coordinate.y] = false;
        }
        for (Point coordinate : this.currentCoordinates) {
            coordinate.y += 1;
            this.board[coordinate.x][coordinate.y] = true;
        }

        this.invalidate();
    }

    public void rotate() {
        List<Point> nextCoordinates = Block.getRelativeCoordinates(this.currentType,
                (this.currentRotateTimes + 1) % 4);
        for (Point coordinate : nextCoordinates) {
            if (this.currentType == 5) {
                if (this.currentRotateTimes == 0 || this.currentRotateTimes == 2) {
                    coordinate.x = coordinate.x + this.currentX - 1;
                    coordinate.y = coordinate.y + this.currentY + 1;
                } else {
                    coordinate.x = coordinate.x + this.currentX + 1;
                    coordinate.y = coordinate.y + this.currentY - 1;
                }
            } else {
                coordinate.x = coordinate.x + this.currentX;
                coordinate.y = coordinate.y + this.currentY;
            }
        }

        for (Point coordinate : this.currentCoordinates) {
            this.board[coordinate.x][coordinate.y] = false;
        }

        for (Point coordinate : nextCoordinates) {
            this.board[coordinate.x][coordinate.y] = true;
        }

        this.currentCoordinates = nextCoordinates;

        if (this.currentType == 5) {
            if (this.currentRotateTimes == 0 || this.currentRotateTimes == 2) {
                this.currentX -= 1;
                this.currentY += 1;
            } else {
                this.currentX += 1;
                this.currentY -= 1;
            }
        }
        this.currentRotateTimes = (this.currentRotateTimes + 1) % 4;

        this.invalidate();
    }

    // checks if the current block can move down
    public boolean canDown() {

        for (Point coordinate : this.currentCoordinates) {
            if (withinCurrentCoordinates(new Point(coordinate.x + 1, coordinate.y))) {
                continue;
            } else if (coordinate.x == 19) {
                return false;
            } else if (this.board[coordinate.x + 1][coordinate.y]) {
                return false;
            }
        }

        return true;
    }

    // checks if the current block can move left
    public boolean canLeft() {

        for (Point coordinate : this.currentCoordinates) {
            if (withinCurrentCoordinates(new Point(coordinate.x, coordinate.y - 1))) {
                continue;
            } else if (coordinate.y == 0) {
                return false;
            } else if (this.board[coordinate.x][coordinate.y - 1]) {
                return false;
            }
        }

        return true;
    }

    // checks if the current block can move right
    public boolean canRight() {

        for (Point coordinate : this.currentCoordinates) {
            if (withinCurrentCoordinates(new Point(coordinate.x, coordinate.y + 1))) {
                continue;
            } else if (coordinate.y == 9) {
                return false;
            } else if (this.board[coordinate.x][coordinate.y + 1]) {
                return false;
            }
        }

        return true;
    }

    // checks if the current block can rotate
    public boolean canRotate() {

        List<Point> nextCoordinates = Block.getRelativeCoordinates(this.currentType,
                (this.currentRotateTimes + 1) % 4);
        for (Point coordinate : nextCoordinates) {
            if (this.currentType == 5) {
                if (this.currentRotateTimes == 0 || this.currentRotateTimes == 2) {
                    coordinate.x = (coordinate.x + this.currentX - 1);
                    coordinate.y = (coordinate.y + this.currentY + 1);
                } else {
                    coordinate.x = (coordinate.x + this.currentX + 1);
                    coordinate.y = (coordinate.y + this.currentY - 1);
                }
            } else {
                coordinate.x = (coordinate.x + this.currentX);
                coordinate.y = (coordinate.y + this.currentY);
            }
        }
        for (Point coordinate : nextCoordinates) {
            if (this.withinCurrentCoordinates(coordinate)) {
                continue;
            } else if (coordinate.x < 0 ||
                    coordinate.x > 19 ||
                    coordinate.y < 0 ||
                    coordinate.y > 9) {
                return false;
            } else if (this.board[coordinate.x][coordinate.y]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check rows and return 1 if any row can be removed
     * @return 1 if any row can be removed
     */
    public synchronized int checkForRowsElimination() {
        if(checkRowEliminationPossibility()){
            return 1;
        }
        return 0;
    }

    /**
     * Checks if row can be eliminated and if yes elimiantes it.
     *
     */
    private synchronized boolean checkRowEliminationPossibility() {
        // Try to eliminate the row
        for (int i = 19; i >= 0; i--) {
            // Judges whether the current row can be eliminated
            boolean canEliminate = true;
            for (int j = 0; j < 10; j++) {
                canEliminate = canEliminate && this.board[i][j];
            }

            // If the current row can be eliminated, eliminate it
            if (canEliminate) {
                for (int row = i; row >= 1; row--) {
                    for (int column = 0; column < 10; column++) {
                        this.board[row][column] = this.board[row - 1][column];
                    }
                }
                for (int column = 0; column < 10; column++) {
                    this.board[0][column] = false;
                }


                return true;
            }
        }

        return false;
    }

    /**
     * Checks if given coordinates doesn't exceed the board.
     * @param coordinate to check
     * @return true if they fit to coordinates, false otherwise
     */
    private boolean withinCurrentCoordinates(Point coordinate) {
        for (Point coord : this.currentCoordinates) {
            if (coordinate.x == coord.x && coordinate.y == coord.y) {
                return true;
            }
        }
        return false;
    }

    // checks if the game is over
    public boolean isGameOver(int type) {

        for (Point coordinate : Block.getRelativeCoordinates(type, 0)) {
            if (this.board[coordinate.x + 0][coordinate.y + 3]) {
                return true;
            }
        }

        return false;
    }

    public void reset() {
        /* Resets all the data and refresh the UI */

        // Reset the board
        for (int row = 0; row < 20; row++) {
            for (int column = 0; column < 10; column++) {
                this.board[row][column] = false;
            }
        }

        // Reset the information of the current block
        this.currentX = 0;
        this.currentY = 3;
        this.currentType = 0;
        this.currentRotateTimes = 0;
        this.currentCoordinates.clear();

        // Refresh the UI
        this.invalidate();
    }

    // push a new block to the board
    public void pushBlock(int type) {

        this.currentType = type;
        this.currentX = 0;
        this.currentY = 3;
        this.currentRotateTimes = 0;
        this.currentCoordinates.clear();
        for (Point coordinate : Block.getRelativeCoordinates(type, 0)) {
            this.board
                    [this.currentX + coordinate.x]
                    [this.currentY + coordinate.y]
                    = true;
            this.currentCoordinates.add(new Point(
                    this.currentX + coordinate.x,
                    this.currentY + coordinate.y
            ));
        }
    }


    // set all rows to to be filled in the board
    public void setRowAllFilledAt(int row) {
        /* Sets all the blocks in the row filled */

        for (int column = 0; column < 10; column++) {
            this.board[row][column] = true;
        }

        this.invalidate();
    }


}
