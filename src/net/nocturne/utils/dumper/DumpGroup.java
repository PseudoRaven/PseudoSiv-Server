package net.nocturne.utils.dumper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at: Jan 5, 2017 7:12:07 PM
 * 
 * @author Walied-Yassen A.K.A Cody
 */
public class DumpGroup {

	/**
	 * The VarPlayer type.
	 */
	public static final int VARP = 0;

	/**
	 * The VarPlayerBit type.
	 */
	public static final int VARPBIT = 1;

	/**
	 * The VarClientInteger type.
	 */
	public static final int VARCINT = 2;

	/**
	 * The VarClientString type.
	 */
	public static final int VARCSTR = 3;

	/**
	 * The components group.
	 */
	public static final int GROUP_COMPONENTS = 0;

	/**
	 * The NPCs group.
	 */
	public static final int GROUP_NPCS = 1;

	/**
	 * The objects group.
	 */
	public static final int GROUP_OBJECTS = 2;

	/**
	 * The var type.
	 */
	public int type;

	/**
	 * The dump map.
	 */
	public Map<Integer, DumpList> map;

	/**
	 * Constructs a new {@link DumpGroup} class object instance.
	 * 
	 * @param type
	 *            the var type.
	 */
	public DumpGroup(int type) {
		this.type = type;
		map = new HashMap<Integer, DumpList>();
	}

}
