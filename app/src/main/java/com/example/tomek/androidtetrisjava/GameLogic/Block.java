package com.example.tomek.androidtetrisjava.GameLogic;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Block {

    public static List<Point> getRelativeCoordinates(int type, int rotateTimes) {
        List<Point> relativeCoordinates = new ArrayList<>();

        switch (type) {
            case 0:
                relativeCoordinates.add(new Point(0, 0));
                relativeCoordinates.add(new Point(0, 1));
                relativeCoordinates.add(new Point(1, 0));
                relativeCoordinates.add(new Point(1, 1));

                break;

            case 1:
                switch (rotateTimes) {
                    case 0:
                        relativeCoordinates.add(new Point(0, 2));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(1, 2));

                        break;

                    case 1:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(2, 1));

                        break;

                    case 2:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(0, 2));
                        relativeCoordinates.add(new Point(1, 0));

                        break;

                    case 3:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(2, 0));
                        relativeCoordinates.add(new Point(2, 1));

                        break;

                    default:
                        break;
                }

                break;

            case 2:
                switch (rotateTimes) {
                    case 0:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(1, 2));

                        break;

                    case 1:
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(2, 0));
                        relativeCoordinates.add(new Point(2, 1));

                        break;

                    case 2:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(0, 2));
                        relativeCoordinates.add(new Point(1, 2));

                        break;

                    case 3:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(2, 0));

                        break;

                    default:
                        break;
                }

                break;

            case 3:
                switch (rotateTimes) {
                    case 0:case 2:
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(0, 2));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));

                        break;

                    case 1:case 3:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(2, 1));

                        break;

                    default:
                        break;
                }

                break;

            case 4:
                switch (rotateTimes) {
                    case 0:case 2:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(1, 2));

                        break;

                    case 1:case 3:
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(2, 0));

                        break;

                    default:
                        break;
                }

                break;

            case 5:
                switch (rotateTimes) {
                    case 0:case 2:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(0, 2));
                        relativeCoordinates.add(new Point(0, 3));

                        break;

                    case 1:case 3:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(2, 0));
                        relativeCoordinates.add(new Point(3, 0));

                        break;

                    default:
                        break;
                }

                break;

            case 6:
                //T
                switch (rotateTimes) {
                    case 0:
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(1, 2));

                        break;

                    case 1:
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(2, 1));

                        break;

                    case 2:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(0, 1));
                        relativeCoordinates.add(new Point(0, 2));
                        relativeCoordinates.add(new Point(1, 1));

                        break;

                    case 3:
                        relativeCoordinates.add(new Point(0, 0));
                        relativeCoordinates.add(new Point(1, 0));
                        relativeCoordinates.add(new Point(1, 1));
                        relativeCoordinates.add(new Point(2, 0));

                        break;

                    default:
                        break;
                }

                break;

            default:
                break;
        }

        return relativeCoordinates;
    }

}
