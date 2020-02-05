package net.nocturne.utils.dumper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.nocturne.cache.Cache;
import net.nocturne.cache.loaders.IComponentDefinitions;
import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.cache.loaders.ObjectDefinitions;
import net.nocturne.utils.Utils;

/**
 * Created at: Jan 5, 2017 7:19:29 PM
 * 
 * @author Walied-Yassen A.K.A Cody
 */
public class Dumper {
	/**
	 * The main entry point of each Java Application, in this application we are
	 * going to dump all the var usage which can be found at config types.
	 * 
	 * @param args
	 *            the arguments which are being passed from the command-line to
	 *            our application.
	 * @throws IOException
	 *             if any error occurs while trying to load our cache system.
	 */
	public static void main(String[] args) throws IOException {
		/* initiate the cache system */
		Cache.init();

		/* creates the dump storage */
		Map<Integer, DumpGroup> varps = new HashMap<Integer, DumpGroup>();
		DumpGroup varpbits = new DumpGroup(DumpGroup.VARPBIT);
		DumpGroup varcints = new DumpGroup(DumpGroup.VARCINT);
		DumpGroup varcstrs = new DumpGroup(DumpGroup.VARCSTR);

		for (int id = 0; id < Utils.getInterfaceDefinitionsSize(); id++) {
			try {
				IComponentDefinitions[] components = IComponentDefinitions
						.getInterface(id);
				for (IComponentDefinitions component : components) {
					if (component.anIntArray4863 != null) {
						for (int var : component.anIntArray4863) {
							DumpGroup group = varps.get(var);
							if (group == null) {
								varps.put(var, group = new DumpGroup(
										DumpGroup.VARPBIT));
							}
							DumpList list = group.map
									.get(DumpGroup.GROUP_COMPONENTS);
							if (list == null) {
								group.map.put(DumpGroup.GROUP_COMPONENTS,
										list = new DumpList());
							}
							list.add(component.ihash);
						}
					}
				}
			} catch (Exception e) {

			}
		}
		/*
		 * for (int id = 0; id < Utils.getNPCDefinitionsSize(); id++) {
		 * NPCDefinitions npc = NPCDefinitions.getNPCDefinitions(id); if (npc !=
		 * null) { if (npc.config != -1) { int var = npc.config; DumpGroup group
		 * = varps.get(var); if (group == null) { varps.put(var, group = new
		 * DumpGroup(DumpGroup.VARPBIT)); } DumpList list =
		 * group.map.get(DumpGroup.GROUP_NPCS); if (list == null) {
		 * group.map.put(DumpGroup.GROUP_NPCS, list = new DumpList()); }
		 * list.add(id); } } } for (int id = 0; id <
		 * Utils.getObjectDefinitionsSize(); id++) { ObjectDefinitions npc =
		 * ObjectDefinitions.getObjectDefinitions(id); if (npc != null) { if
		 * (npc.configId != -1) { int var = npc.configId; DumpGroup group =
		 * varps.get(var); if (group == null) { varps.put(var, group = new
		 * DumpGroup(DumpGroup.VARPBIT)); } DumpList list =
		 * group.map.get(DumpGroup.GROUP_OBJECTS); if (list == null) {
		 * group.map.put(DumpGroup.GROUP_OBJECTS, list = new DumpList()); }
		 * list.add(id); } } }
		 */
		File file = new File("./information/varbitDump.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		varps.keySet()
				.stream()
				.sorted()
				.forEach(
						id -> {
							try {
								DumpGroup group = varps.get(id);
								writer.append("Start of varp " + id);
								writer.newLine();
								DumpList list = group.map
										.get(DumpGroup.GROUP_COMPONENTS);
								if (list != null) {
									for (Integer ihash : list.entries()) {
										writer.append("\t> Found component: "
												+ (ihash >> 16) + ", "
												+ (ihash & 0xffff));
										writer.newLine();
									}
								}
								list = group.map.get(DumpGroup.GROUP_NPCS);
								if (list != null) {
									for (Integer npc : list.entries()) {
										writer.append("\t> Found npc: " + npc);
										writer.newLine();
									}
								}
								list = group.map.get(DumpGroup.GROUP_OBJECTS);
								if (list != null) {
									for (Integer object : list.entries()) {
										writer.append("\t> Found object: "
												+ object);
										writer.newLine();
									}
								}
								writer.flush();
							} catch (Exception e) {
								System.out.println(e);
							}
						});
		writer.close();
	}
}
