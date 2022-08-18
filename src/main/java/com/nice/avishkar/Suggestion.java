package com.nice.avishkar;

public class Suggestion {

	public String Id;
	public int Score;
	public String Name;
	
	public Suggestion(String id, int score, String name) {
		super();
		Id = id;
		Score = score;
		Name = name;
	}


	public String getId() {
		return Id;
	}


	public void setId(String id) {
		Id = id;
	}


	public int getScore() {
		return Score;
	}


	public void setScore(int score) {
		Score = score;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	@Override
	public String toString() {
		return "Suggestion [Id=" + Id + ", Score=" + Score + ", Name=" + Name + "]";
	}
	
}
