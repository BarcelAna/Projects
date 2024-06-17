package hr.fer.ooup.lab3.model;

import java.util.Objects;

public class Location {
	public int row;
	public int column;

	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return row == location.row && column == location.column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, column);
	}
}
