package agh.po.Utilities;
import agh.po.MapElements.Plant;
import agh.po.Map.GameArea;
import agh.po.Properties.Vector2d;

import java.util.List;

    /**
     * The map visualizer converts the  map into a string
     * representation.
     *
     * @author apohllo
     */
    public class MapVisualizer {
        private static final String EMPTY_CELL = "    ";
        private static final String FRAME_SEGMENT = " - ";
        private static final String CELL_SEGMENT = "|";
        private GameArea map;

        /**
         * Initializes the MapVisualizer with an instance of map to visualize.
         * @param map
         */
       public MapVisualizer(GameArea map)
        {
            this.map = map;
        }

        /**
         * Convert selected region of the map into a string. It is assumed that the
         * indices of the map will have no more than two characters (including the
         * sign).
         *
         * @param lowerLeft  The lower left corner of the region that is drawn.
         * @param upperRight The upper right corner of the region that is drawn.
         * @return String representation of the selected region of the map.
         */
        public String draw(Vector2d lowerLeft, Vector2d upperRight) {
            StringBuilder builder = new StringBuilder();
            for (int i = upperRight.y + 1; i >= lowerLeft.y - 1; i--) {
                if (i == upperRight.y + 1) {
                    builder.append(drawHeader(lowerLeft, upperRight));
                }
                builder.append(String.format("%3d: ", i));
                for (int j = lowerLeft.x; j <= upperRight.x + 1; j++) {
                    if (i < lowerLeft.y || i > upperRight.y) {
                        builder.append(drawFrame(j <= upperRight.x));
                    } else {
                        builder.append(CELL_SEGMENT);
                        if (j <= upperRight.x) {
                            builder.append(drawObject(new Vector2d(j, i)));
                        }
                    }
                }
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }

         private String drawFrame(boolean innerSegment) {
        if (innerSegment) {
            return FRAME_SEGMENT + FRAME_SEGMENT;
        } else {
            return FRAME_SEGMENT;
        }
    }


        private String drawHeader(Vector2d lowerLeft, Vector2d upperRight) {
            StringBuilder builder = new StringBuilder();
            builder.append(" y\\x ");
            for (int j = lowerLeft.x; j < upperRight.x + 1; j++) {
                if(j<10)
                builder.append(String.format("  %2d ", j));
                else
                    builder.append(String.format(" %2d", j));
            }
            builder.append(System.lineSeparator());
            return builder.toString();
        }

        private String drawObject(Vector2d currentPosition) {
            String result = null;
            if (this.map.isOccupied(currentPosition)) {
                List<Object> objects = this.map.objectsAt(currentPosition);
                Object object = this.map.objectsAt(currentPosition).get(0);
                int animalQuantity=this.map.animalsAtPosition(currentPosition).size();

                if (animalQuantity>=2) result = " \uD83D\uDC3E";
                else if (object != null) {
                    result = object.toString();
                    if (object instanceof Plant && this.map.isInsideJungle(currentPosition))
                        result = " \ud83c\udf34";
                } else {
                    result = EMPTY_CELL;
                }
            } else {
                result = EMPTY_CELL;
            }
            return result;
        }
}

