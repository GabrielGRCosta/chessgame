package com.chessgame.engine;

public class Point {

	public final int x;
	public final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Point of(int x, int y) {
		return new Point(x, y);
	}

	@Override
	public String toString() {
		return "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
    		result = prime * result + (x ^ (x >>> 32));
    		result = prime * result + (y ^ (x >>> 32));
    		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null || getClass() != o.getClass()) {
			return false;
		} else {
			Point point = (Point) o;
			return this.x == point.x && this.y == point.y;
		}
	}

	public Point add(Point point) {
		return new Point(this.x + point.x, this.y + point.y);
	}

	public Point sub(Point point) {
		return new Point(this.x - point.x, this.y - point.y);
	}

}
