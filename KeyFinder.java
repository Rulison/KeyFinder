import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import list.*;
import dict.*;

/**
 * KeyFinder finds keys of a song based on the chords.  It finds the notes in each
 * chord and adds each note to a running total.  It takes the 7 highest occurring
 * notes and finds the major key that best matches them.
 * 
 * Types of chords handled, where X is A,A#/Bb,B/Cb,...,G#/Ab
 * X, Xm, X7, X6, X5, X9, XM7
 * @author Jared
 *
 */
public class KeyFinder {

	public static void main(String[] args) { //for testing:
		KeyFinder kf = null;
		try {
			kf = new KeyFinder("A D E \n E B A E B E B");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(kf.findKey());
		System.out.println(getNaturalIndex('A'));
	}
	private String text; //the text to be parsed for chords
	private HashTableChained signatures; //table of every key signature

	/**
	 * Creates a new KeyFinder with text to be parsed. 
	 * @param ch text to be parsed
	 */
	public KeyFinder(String text) {
		this.text = text;
		signatures = new HashTableChained();
		String key = "A";
		SList notes = new SList();
		notes.insertBack(new String[] {"a", "b", "c#", "d", "e", "f#", "g#"});
		signatures.insert(key,  notes);

		key = "A#";
		notes = new SList();
		notes.insertBack(new String[] {"a#", "b#", "c##", "d#", "e#", "f##", "g##"});
		signatures.insert(key,  notes);

		key = "Bb";
		notes = new SList();
		notes.insertBack(new String[] {"bb", "c", "d", "eb", "f", "g", "a"});
		signatures.insert(key,  notes);

		key = "B";
		notes = new SList();
		notes.insertBack(new String[] {"b", "c#", "d#", "e", "f#", "g#", "a#"});
		signatures.insert(key,  notes);

		key = "Cb";
		notes = new SList();
		notes.insertBack(new String[] {"cb", "db", "eb", "fb", "gb", "ab", "bb"});
		signatures.insert(key,  notes);

		key = "C";
		notes = new SList();
		notes.insertBack(new String[] {"c", "e", "e", "f", "g", "a", "b"});
		signatures.insert(key,  notes);

		key = "C#";
		notes = new SList();
		notes.insertBack(new String[] {"c#", "d#", "e#", "f#", "g#", "a#", "b#"});
		signatures.insert(key,  notes);

		key = "Db";
		notes = new SList();
		notes.insertBack(new String[] {"db", "eb", "f", "gb", "ab", "bb", "c"});
		signatures.insert(key,  notes);

		key = "D";
		notes = new SList();
		notes.insertBack(new String[] {"d", "e", "f#", "g", "a", "b", "c#"});
		signatures.insert(key,  notes);

		key = "D#";
		notes = new SList();
		notes.insertBack(new String[] {"d#", "e#", "f##", "g#", "a#", "b#", "c##"});
		signatures.insert(key,  notes);

		key = "Eb";
		notes = new SList();
		notes.insertBack(new String[] {"eb", "f", "g", "ab", "bb", "c", "d"});
		signatures.insert(key,  notes);

		key = "E";
		notes = new SList();
		notes.insertBack(new String[] {"e", "f#", "g#", "a", "b", "c#", "d#"});
		signatures.insert(key,  notes);

		key = "F";
		notes = new SList();
		notes.insertBack(new String[] {"f", "g", "a", "bb", "c", "d", "e"});
		signatures.insert(key,  notes);

		key = "E#";
		notes = new SList();
		notes.insertBack(new String[] {"e#", "f##", "g##", "a#", "b#", "c##", "d##"});
		signatures.insert(key,  notes);

		key = "F#";
		notes = new SList();
		notes.insertBack(new String[] {"f#", "g#", "a#", "b", "c#", "d#", "e#"});
		signatures.insert(key,  notes);

		key = "Gb";
		notes = new SList();
		notes.insertBack(new String[] {"gb", "ab", "bb", "cb", "db", "eb", "f"});
		signatures.insert(key,  notes);

		key = "G";
		notes = new SList();
		notes.insertBack(new String[] {"g", "a", "b", "c", "d", "e", "f#"});
		signatures.insert(key,  notes);

		key = "G#";
		notes = new SList();
		notes.insertBack(new String[] {"g#", "a#", "b#", "c#", "d#", "e#", "f##"});
		signatures.insert(key,  notes);

		key = "Ab";
		notes = new SList();
		notes.insertBack(new String[] {"ab", "bb", "c", "db", "eb", "f", "g"});
		signatures.insert(key,  notes);

	}

	/**
	 * Change the text to be parsed.
	 * @param text new text
	 */
	public void setChords(String text) {
		this.text = text;
	}

	/**
	 * Actually finds the key of the text.  Takes each word, and if a chord, takes the notes
	 * in the chord and adds to a running total for each note.
	 * 
	 * Then, it takes the top 7 occuring notes and matches it to a major scale.
	 * @return the key
	 */
	public String findKey() {
		Scanner reader = null;
		try {
			reader = new Scanner(text); //for parsing
		} catch (Exception e) {
			e.printStackTrace();
		}
		int[] noteCounts = new int[12];  //a is index 0, a# is 1, etc.
		String line = null;
		try {
			line = reader.nextLine();
		} catch (NullPointerException e) {

		}
		while(line != null) {
			while(line.length() != 0) {
				String[] notes = null;
				int spaceIndex = line.indexOf(' ');
				String chord;
				if(spaceIndex == -1) { //if spaceIndex == -1, we are on the last chord in the line
					chord = line;
					line = "";
				}
				else {
					chord = line.substring(0, spaceIndex);
					line = line.substring(spaceIndex+1);  //delete chord from the line
				}
				chord = chord.trim();//remove spaces
				if(chord.equals("")) { //In case of multiple spaces
					continue;
				}
				if(signatures.find(chord.substring(0,1)) == null) { //This means either the chord is actually a word, or it couldn't recognize the type of chord
					continue;
				}
				if(chord.length() == 1) {  //A, B, C, D, etc.
					try {
						notes = (String[]) ((SList) signatures.find(chord).value()).front().item();
					} catch (Exception e) {
						continue;
					}
					noteCounts[getChromaticIndex(notes[0])]++; //1st
					noteCounts[getChromaticIndex(notes[2])]++; //Major 3rd
					noteCounts[getChromaticIndex(notes[4])]++; //5th
				}
				else if(chord.length() == 2) { //Ab, Am, A7, A9, A6 etc.
					try {

						if(chord.charAt(1) == 'b' || chord.charAt(1) == '#') {  //like Ab
							notes = (String[]) ((SList) signatures.find(chord).value()).front().item();
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[2])]++;
							noteCounts[getChromaticIndex(notes[4])]++;
						}
						else if(chord.charAt(1) == 'm'){  //like Am
							notes = (String[]) ((SList) signatures.find(""+chord.substring(0, 1)).value()).front().item();
							noteCounts[getChromaticIndex(notes[0])]++;
							System.out.println((getChromaticIndex(notes[2])+11)%12);
							noteCounts[(getChromaticIndex(notes[2])+11)%12]++;
							noteCounts[getChromaticIndex(notes[4])]++;
						}
						else if(chord.charAt(1) == '5') {
							notes = (String[]) ((SList) signatures.find(""+chord.substring(0, 1)).value()).front().item();
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[4])]++;
						}
						else if(chord.charAt(1) == '7') {
							notes = (String[]) ((SList) signatures.find(""+chord.substring(0, 1)).value()).front().item();
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[2])]++;
							noteCounts[getChromaticIndex(notes[4])]++;
							noteCounts[getChromaticIndex(notes[6])-1]++;
						}
						else if(chord.charAt(1) == '9') {
							System.out.println(chord);
							notes = (String[]) ((SList) signatures.find(""+chord.substring(0, 1)).value()).front().item();
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[1])]++;
							noteCounts[getChromaticIndex(notes[2])]++;
							noteCounts[getChromaticIndex(notes[4])]++;
							noteCounts[getChromaticIndex(notes[6])-1]++;
						}
					} catch (InvalidNodeException e) {
						e.printStackTrace();
					}

				}
				else if(chord.length() == 3) {// Abm, Ab7, Am7, AM9
					if(chord.charAt(1) == 'b' || chord.charAt(1) == '#') {
						if(chord.charAt(2) == 'm') {
							try {
								notes = (String[]) ((SList) signatures.find(chord.substring(0, 2)).value()).front().item();
							} catch (InvalidNodeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[2])-1]++;
							noteCounts[getChromaticIndex(notes[4])]++;
						}
						else if (chord.charAt(2) == '7') {
							try {
								notes = (String[]) ((SList) signatures.find(chord.substring(0, 2)).value()).front().item();
							} catch (InvalidNodeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[2])]++;
							noteCounts[getChromaticIndex(notes[4])]++;
							noteCounts[getChromaticIndex(notes[6])-1]++;
						}

					}
					else if (chord.charAt(1) == 'M') {
						if(chord.charAt(2) == '7') {
							try {
								notes = (String[]) ((SList) signatures.find(chord.substring(0, 1)).value()).front().item();
							} catch (InvalidNodeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[2])]++;
							noteCounts[getChromaticIndex(notes[4])]++;
							noteCounts[getChromaticIndex(notes[6])]++;
						}
						if(chord.charAt(2) == '9') {
							try {
								notes = (String[]) ((SList) signatures.find(chord.substring(0, 1)).value()).front().item();
							} catch (InvalidNodeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[1])]++;
							noteCounts[getChromaticIndex(notes[2])]++;
							noteCounts[getChromaticIndex(notes[4])]++;
							noteCounts[getChromaticIndex(notes[6])+1]++;
						}
					}
					else if(chord.charAt(1) == 'm') {
						if(chord.charAt(2) == '7') {
							try {
								notes = (String[]) ((SList) signatures.find(chord.substring(0,1)).value()).front().item();
							} catch (InvalidNodeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							noteCounts[getChromaticIndex(notes[0])]++;
							noteCounts[getChromaticIndex(notes[2])-1]++;
							noteCounts[getChromaticIndex(notes[4])]++;
							noteCounts[getChromaticIndex(notes[6])-1]++;
						}
					}
				}
			}
			try {
				line = reader.nextLine(); //gets next line
			} catch (NoSuchElementException e) {//end of text
				break;
			}
		}

		//----------------------------------------------------------
		//now find top 7 notes
		NoteSort[] sortedNotes = new NoteSort[noteCounts.length];
		for(int i=0;i<noteCounts.length;i++){
			sortedNotes[i] = new NoteSort(i, noteCounts[i]);
		}
		quicksort(sortedNotes, 0, sortedNotes.length-1); //sort the notes to find the most occurring
		int[] top7Notes = new int[7];
		for(int i=0;i<7;i++) { //get 7 most occurring

			if(sortedNotes[11-i].count == 0) {
				top7Notes[i] = -1;
			}
			else {
				top7Notes[i] = sortedNotes[11-i].noteIndex;
			}
		}

		int maxCorrelation = 0;
		String bestFit = "No key detected :("; //default best fit key
		int numCorrelation = getNumCorrelation(top7Notes, "A");
		if(numCorrelation == 7) {
			return "A";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "A";
		}
		numCorrelation = getNumCorrelation(top7Notes, "A#");
		if(numCorrelation == 7) {
			return "A#";
		}

		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "A#";
		}
		numCorrelation = getNumCorrelation(top7Notes, "B");
		if(numCorrelation == 7) {
			return "B";

		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "B";
		}
		numCorrelation = getNumCorrelation(top7Notes, "B");
		if(numCorrelation == 7) {
			return "B";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "B";
		}
		numCorrelation = getNumCorrelation(top7Notes, "C");

		if(numCorrelation == 7) {
			return "C";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "C";
		}
		numCorrelation = getNumCorrelation(top7Notes, "C#");
		if(numCorrelation == 7) {
			return "C#";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "C#";
		}
		numCorrelation = getNumCorrelation(top7Notes, "D");
		if(numCorrelation == 7) {
			return "D";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "D";
		}
		numCorrelation = getNumCorrelation(top7Notes, "D#");
		if(numCorrelation == 7) {
			return "D#";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "D#";
		}
		numCorrelation = getNumCorrelation(top7Notes, "E");
		if(numCorrelation == 7) {
			return "E";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "E";
		}
		numCorrelation = getNumCorrelation(top7Notes, "F");
		if(numCorrelation == 7) {
			return "F";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "F";
		}
		numCorrelation = getNumCorrelation(top7Notes, "F#");
		if(numCorrelation == 7) {
			return "F#";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "F#";
		}
		numCorrelation = getNumCorrelation(top7Notes, "G");
		if(numCorrelation == 7) {
			return "G";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "G";
		}
		numCorrelation = getNumCorrelation(top7Notes, "G#");
		if(numCorrelation == 7) {
			return "G#";
		}
		else if(numCorrelation > maxCorrelation) {
			maxCorrelation = numCorrelation;
			bestFit = "G#";
		}

		return bestFit;
	}

	/**
	 * Finds the number of notes that match between a key and the 7 most occurring notes in a chunk of text
	 * @param top7Notes int[] of 7 most occurring notes
	 * @param key key to be compared with
	 * @return number of matching notes. -1 if key not valid
	 */
	private int getNumCorrelation(int[] top7Notes, String key) {
		try {
			int numCorrelation = 0;
			String[] Signature = (String[]) ((SList) signatures.find(key).value()).front().item();
			String compressedSignature = ""; //creates pseudo-hashtable, taking advantage of how java treats Strings
			for(int i=0;i<Signature.length;i++) {
				compressedSignature+=numToWord(getChromaticIndex(Signature[i]));
			};
			for(int i=0;i<top7Notes.length;i++) {
				if(compressedSignature.indexOf(numToWord(top7Notes[i]))!= -1) { //indexOf takes O(1) time, which is why the signature is a String
					numCorrelation++;
				}
			}
			return numCorrelation;

		} catch (InvalidNodeException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Standard quicksort for sorting an array of Comparables.
	 * @param a
	 * @param lo
	 * @param hi
	 */
	public static void quicksort(Comparable[] a, int lo, int hi) {
		if (lo < hi) {
			int pivotIndex = lo;
			Comparable pivot = a[pivotIndex];
			a[pivotIndex] = a[hi];
			a[hi] = pivot;

			int i = lo - 1;
			int j = hi;
			do {
				do { 
					i++; 
				} 
				while (a[i].compareTo(pivot) < 0);
				do { 
					j--; 
				} 
				while ((a[j].compareTo(pivot) > 0) && (j > lo));
				if (i < j) 
				{
					Comparable holder = a[i];
					a[i]=a[j];
					a[j] = holder;
				}
			} while (i < j);

			a[hi] = a[i];
			a[i] = pivot;          
			quicksort(a, lo, i - 1);   
			quicksort(a, i + 1, hi); 
		}
	}
	/**
	 * Returns the chromatic index of a note.  Matches A with 0, A#/Bb with 1, B/Cb with 2, etc.
	 * @param note the String to be converted to chromatic index.
	 * @return chromatic index, or -1 if invalid note
	 */
	private static int getChromaticIndex(String note) {
		int base = getNaturalIndex(note.charAt(0));
		if(note.length() == 1) {
			return base;
		}
		if(note.length() == 2) {
			if(note.charAt(1)=='b') {
				return (base+11)%12;
			}
			if(note.charAt(1)=='#') {
				return (base+1);
			}
		}
		if(note.length() == 3) {
			if(note.charAt(2)=='b') {
				return (base+10)%12;
			}
			if(note.charAt(2)=='#') {
				return (base+2);
			}
		}
		return -1;
	}
	/**
	 * returns the chromatic index of an unmodified note, like a, b, c
	 * @param note
	 * @return
	 */
	private static int getNaturalIndex(char note) {
		switch(note) {
		case 'a': return 0;
		case 'b': return 2;
		case 'c': return 3;
		case 'd': return 5;
		case 'e': return 7;
		case 'f': return 8;
		case 'g': return 10;
		}
		return -1;
	}

	/**
	 * Converts a number between 0 and 12 to its written equivalent.
	 * @param i number to convert
	 * @return written form of i
	 */
	private static String numToWord(int i) {
		switch(i) {
		case 0: return "zero";
		case 1: return "one";
		case 2: return "two";
		case 3: return "three";
		case 4: return "four";
		case 5: return "five";
		case 6: return "six";
		case 7: return "seven";
		case 8: return "eight";
		case 9: return "nine";
		case 10: return "ten";
		case 11: return "eleven";
		case 12: return "twelve";
		}
		return "uhoh";
	}
}
