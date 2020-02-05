package net.nocturne.utils.dumper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created at: Jan 5, 2017 7:15:08 PM
 * 
 * @author Walied-Yassen A.K.A Cody
 */
public class DumpList {

	/**
	 * The group entries.
	 */
	private List<Integer> entries;

	/**
	 * Constructs a new {@link DumpList} class object instance.
	 */
	public DumpList() {
		this.entries = new ArrayList<Integer>();
	}

	/**
	 * Adds a new entry to the group.
	 * 
	 * @param hash
	 *            the entry hash.
	 */
	public void add(int hash) {
		entries.add(hash);
	}

	/**
	 * Gets the entries list.
	 * 
	 * @return
	 * 
	 * @return the entries list.
	 */
	public List<Integer> entries() {
		return entries;
	}
}
