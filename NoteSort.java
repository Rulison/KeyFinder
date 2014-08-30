/**
 * A class to help sort notes.  Stores the index of a note
 * and the count of how many times it appeared in a file.
 * @author Jared
 *
 */
public class NoteSort implements Comparable{
	int noteIndex;
	int count;
	public NoteSort(int noteIndex, int count) {
		this.noteIndex = noteIndex;
		this.count = count;
	}
	public int compareTo(Object otherNote) {
		return count-((NoteSort)otherNote).count;
	}
}
