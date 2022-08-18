package com.nice.avishkar;

import java.util.Comparator;

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

//	@Override
//	public int compare(final Object o1,
//					   final Object o2) {
//		Suggestion suggestion1 = (Suggestion) o1;
//		Suggestion suggestion2 = (Suggestion) o2;
//		if (suggestion2.getScore() > suggestion1.getScore()) {
//			return 1;
//		} else if (suggestion2.getScore() < suggestion1.getScore()) {
//			return -1;
//		} else {
//			return -suggestion1.getName().compareTo(suggestion2.getName());
//		}
//	}
}
