package hr.fer.ooup.lab3.model;

public class LocationRange {
	private Location start;
	private Location end;

	public LocationRange(int startRow, int startColumn, int endRow, int endColumn) {
		this.start=new Location(startRow, startColumn);
		this.end=new Location(endRow, endColumn);
	}

	public Location getStart() {
		return start;
	}


	public Location getEnd() {
		return end;
	}

	public void setEnd(int endRow, int endColumn) {
		this.end = new Location(endRow, endColumn);
	}

	public void setStart(int startRow, int startColumn) {
		this.start = new Location(startRow, startColumn);
	}
}
