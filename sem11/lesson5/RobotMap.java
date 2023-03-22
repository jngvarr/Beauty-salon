package ru.gb.lesson5;

import java.util.HashMap;
import java.util.Map;

public class RobotMap {

    public static Point position;
    private final int n;
    private final int m;

    private Map<Long, Robot> robots;

    public RobotMap(int n, int m) {
        if (n < 0 || m < 0) {
            throw new IllegalArgumentException("Недопустимые значения размера карты!");
        }
        this.n = n;
        this.m = m;
        this.robots = new HashMap<>();
    }

    public Point getPosition() {
        return position;
    }

    public Robot createRobot(Point position) throws PositionException {
        checkPosition(position);

        Robot robot = new Robot(position);
        robots.put(robot.id, robot);

//        for (Long k : robots.keySet()) {
//            String key = k.toString();
//            String value = robots.get(k).toString();
//            System.out.println(key + "" + value);
//            System.out.println(key);
//            System.out.println(value);
//        }
        return robot;
    }

    private void checkPosition(Point position) throws PositionException {
        if (position.getX() < 0 || position.getY() < 0 || position.getX() > n || position.getY() > m) {
            throw new PositionException("Некорректное значение точки: " + position);
        }
        if (!isFree(position)) {
            throw new PositionException("Точка " + position + " занята!");
        }
    }

    private boolean isFree(Point position) {
        return robots.values().stream()
                .map(Robot::getPosition)
                .noneMatch(position::equals);
    }

    public Map getRobots() {
        return robots;
    }

    public class Robot {
        Map getRobots() {
            return robots;
        }

        static Long id = 0L;
        private String name;
        private Point position;
        private Direction direction;

        public Robot(Point position) {
            this.name = String.format("#%d", + ++id);
            this.position = position;
            this.direction = Direction.TOP;
        }

        public Long getId() {
            return id;
        }

        public Point getPosition() {
            return position;
        }


        public void move(Robot robot) throws PositionException {
            Point newPosition = switch (robot.direction) {
                case TOP -> new Point(robot.position.getX() - 1, robot.position.getY());
                case RIGHT -> new Point(robot.position.getX(), robot.position.getY() + 1);
                case BOTTOM -> new Point(robot.position.getX() + 1, robot.position.getY());
                case LEFT -> new Point(robot.position.getX(), robot.position.getY() - 1);
            };
            checkPosition(newPosition);

            position = newPosition;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public Direction changeDirection(String direction) {
            if (Direction.valueOf(direction) == Direction.BOTTOM) this.direction = Direction.BOTTOM;
            if (Direction.valueOf(direction) == Direction.TOP) this.direction = Direction.TOP;
            if (Direction.valueOf(direction) == Direction.RIGHT) this.direction = Direction.RIGHT;
            if (Direction.valueOf(direction) == Direction.LEFT) this.direction = Direction.LEFT;
            return this.direction;
        }
//        public void changeDirection(Direction direction) {
//            this.direction = direction;
//        }

        @Override
        public String toString() {
            return String.format("[%s], position=%s, direction=%s", name, position.toString(), direction.toString());
        }

        public enum Direction {

            TOP, RIGHT, BOTTOM, LEFT

        }
    }
}