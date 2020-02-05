package net.nocturne.game.player.actions.skills.smithing;


import net.nocturne.Settings;
import net.nocturne.game.Animation;
import net.nocturne.game.WorldObject;
import net.nocturne.game.item.Item;
import net.nocturne.game.player.Player;
import net.nocturne.game.player.Skills;
import net.nocturne.game.player.actions.Action;
import net.nocturne.game.player.content.SkillsDialogue;

public class Smithing extends Action {

	public static final int HAMMER = 2347, DUNGEONEERING_HAMMER = 17883;

	public enum ForgingBar {

		/**
		 * Bronze
		 */
		BRONZE_DAGGER(1, 6.2, new Item[] { new Item(2349) }, new Item(1205)),

		OFF_HAND_BRONZE_DAGGER(1, 12.5, new Item[] { new Item(2349) },
				new Item(25692)),

		BRONZE_HATCHET(1, 12.5, new Item[] { new Item(2349) }, new Item(1351)),

		BRONZE_MACE(2, 12.5, new Item[] { new Item(2349) }, new Item(1422)),

		OFF_HAND_BRONZE_MACE(2, 12.5, new Item[] { new Item(2349) }, new Item(
				25674)),

		BRONZE_HELM(3, 12.5, new Item[] { new Item(2349) }, new Item(1139)),

		BRONZE_BLOTS_UNF(3, 12.5, new Item[] { new Item(2349) }, new Item(9375,
				10)),

		BRONZE_SWORD(4, 12.5, new Item[] { new Item(2349) }, new Item(1277)),

		OFF_HAND_BRONZE_SWORD(4, 12.5, new Item[] { new Item(2349) }, new Item(
				25710)),

		BRONZE_WIRE(4, 12.5, new Item[] { new Item(2349) }, new Item(1794)),

		BRONZE_DART_TIP(4, 12.5, new Item[] { new Item(2349) }, new Item(819,
				10)),

		BRONZE_NAILS(4, 12.5, new Item[] { new Item(2349) }, new Item(4819, 15)),

		BRONZE_ARROWHEADS(5, 12.5, new Item[] { new Item(2349) }, new Item(39,
				15)),

		BRONZE_SCIMITAR(5, 25, new Item[] { new Item(2349, 2) }, new Item(1321)),

		OFF_HAND_BRONZE_SCIMITAR(5, 25, new Item[] { new Item(2349, 2) },
				new Item(25743)),

		BRONZE_PICKAXE(5, 25, new Item[] { new Item(2349, 2) }, new Item(1265)),

		BRONZE_LIMBS(6, 12.5, new Item[] { new Item(2349, 1) }, new Item(9420)),

		BRONZE_LONGSWORD(6, 25, new Item[] { new Item(2349, 2) },
				new Item(1291)),

		OFF_HAND_BRONZE_LONGSWORD(6, 25, new Item[] { new Item(2349, 2) },
				new Item(25725)),

		BRONZE_KNIFE(7, 12.5, new Item[] { new Item(2349) }, new Item(864, 5)),

		OFF_HAND_BRONZE_KNIFE(7, 12.5, new Item[] { new Item(2349) }, new Item(
				25897, 5)),

		BRONZE_THROWINAXE(7, 12.5, new Item[] { new Item(2349) }, new Item(800,
				5)),

		OFF_HANDBRONZE_THROWINAXE(7, 12.5, new Item[] { new Item(2349) },
				new Item(25903, 5)),

		BRONZE_FULL_HELM(7, 25, new Item[] { new Item(2349, 2) }, new Item(
				11550)),

		BRONZE_SQ_SHIELD(8, 25, new Item[] { new Item(2349, 2) },
				new Item(1173)),

		BRONZE_WARHAMMER(9, 37.5, new Item[] { new Item(2349, 3) }, new Item(
				1337)),

		OFF_HAND_BRONZE_WARHAMMER(9, 37.5, new Item[] { new Item(2349, 3) },
				new Item(25579)),

		BRONZE_BATTLEAXE(10, 37.5, new Item[] { new Item(2349, 3) }, new Item(
				1375)),

		OFF_HAND_BRONZE_BATTLEAXE(10, 37.5, new Item[] { new Item(2349, 3) },
				new Item(25761)),

		BRONZE_CHAINBODY(11, 37.5, new Item[] { new Item(2349, 3) }, new Item(
				1103)),

		BRONZE_KITESHIELD(12, 37.5, new Item[] { new Item(2349, 3) }, new Item(
				1189)),

		BRONZE_CLAW(13, 25, new Item[] { new Item(2349, 2) }, new Item(3095)),

		OFF_HAND_BRONZE_CLAW(13, 25, new Item[] { new Item(2349, 2) },
				new Item(25933)),

		BRONZE_2H_SWORD(14, 37.5, new Item[] { new Item(2349, 3) }, new Item(
				1307)),

		BRONZE_PLATELEGS(16, 37.5, new Item[] { new Item(2349, 3) }, new Item(
				1075)),

		BRONZE_PLATESKIRT(16, 37.5, new Item[] { new Item(2349, 3) }, new Item(
				1087)),

		BRONZE_PLATEBODY(18, 62.5, new Item[] { new Item(2349, 5) }, new Item(
				1117)),

		/**
		 * Iron
		 */
		IRON_DAGGER(15, 25, new Item[] { new Item(2351) }, new Item(1203)),

		Off_HAND_IRON_Dagger(15, 25, new Item[] { new Item(2351) }, new Item(
				25694)),

		IRON_HATCHET(16, 25, new Item[] { new Item(2351) }, new Item(1349)),

		IRON_SPIT(16, 25, new Item[] { new Item(2351) }, new Item(7225)),

		IRON_MACE(17, 25, new Item[] { new Item(2351) }, new Item(1420)),

		OFF_HAND_IRON_MACE(17, 25, new Item[] { new Item(2351) }, new Item(
				25676)),

		IRON_HELM(18, 25, new Item[] { new Item(2351) }, new Item(1137)),

		IRON_BLOTS_UNF(18, 25, new Item[] { new Item(2351) },
				new Item(9377, 10)),

		IRON_SWORD(19, 25, new Item[] { new Item(2351) }, new Item(1279)),

		OFF_HAND_IRON_SWORD(19, 25, new Item[] { new Item(2351) }, new Item(
				25712)),

		IRON_DART_TIP(19, 25, new Item[] { new Item(2351) }, new Item(820, 10)),

		IRON_NAILS(19, 25, new Item[] { new Item(2351) }, new Item(4820, 15)),

		IRON_ARROWHEADS(20, 25, new Item[] { new Item(2351) }, new Item(40, 15)),

		IRON_SCIMITAR(20, 25, new Item[] { new Item(2351, 2) }, new Item(1323)),

		OFF_HAND_IRON_SCIMITAR(20, 25, new Item[] { new Item(2351, 2) },
				new Item(25745)),

		IRON_PICKAXE(20, 25, new Item[] { new Item(2351, 2) }, new Item(1267)),

		IRON_LONGSWORD(21, 25, new Item[] { new Item(2351, 2) }, new Item(1293)),

		OFF_HAND_IRON_LONGSWORD(21, 25, new Item[] { new Item(2351, 2) },
				new Item(25727)),

		IRON_KNIFE(22, 25, new Item[] { new Item(2351) }, new Item(863, 5)),

		OFF_HAND_IRON_KNIFE(22, 25, new Item[] { new Item(2351) }, new Item(
				25896, 5)),

		IRON_THROWINAXE(22, 25, new Item[] { new Item(2351) }, new Item(801, 5)),

		OFF_HANDIRON_THROWINAXE(22, 25, new Item[] { new Item(2351) },
				new Item(25904, 5)),

		IRON_FULL_HELM(22, 25, new Item[] { new Item(2351, 2) }, new Item(1153)),

		IRON_LIMBS(23, 25, new Item[] { new Item(2351, 1) }, new Item(9423)),

		IRON_SQ_SHIELD(23, 50, new Item[] { new Item(2351, 2) }, new Item(1175)),

		IRON_WARHAMMER(24, 75, new Item[] { new Item(2351, 3) }, new Item(1335)),

		OFF_HAND_IRON_WARHAMMER(24, 75, new Item[] { new Item(2351, 3) },
				new Item(25781)),

		IRON_BATTLEAXE(25, 75, new Item[] { new Item(2351, 3) }, new Item(1363)),

		OFF_HAND_IRON_BATTLEAXE(25, 75, new Item[] { new Item(2351, 3) },
				new Item(25763)),

		OIL_LANTERN_FRAME(26, 75, new Item[] { new Item(2351) }, new Item(4540)),

		IRON_CHAINBODY(26, 75, new Item[] { new Item(2351, 3) }, new Item(1101)),

		IRON_KITESHIELD(27, 75, new Item[] { new Item(2351, 3) },
				new Item(1191)),

		IRON_CLAW(28, 25, new Item[] { new Item(2351, 2) }, new Item(3096)),

		OFF_HAND_IRON_CLAW(28, 25, new Item[] { new Item(2351, 2) }, new Item(
				25935)),

		IRON_2H_SWORD(29, 75, new Item[] { new Item(2351, 3) }, new Item(1309)),

		IRON_PLATELEGS(31, 75, new Item[] { new Item(2351, 3) }, new Item(1067)),

		IRON_PLATESKIRT(31, 75, new Item[] { new Item(2351, 3) },
				new Item(1081)),

		IRON_PLATEBODY(33, 125, new Item[] { new Item(2351, 5) },
				new Item(1115)),

		/**
		 * Steel
		 */
		STEEL_DAGGER(30, 37.5, new Item[] { new Item(2353) }, new Item(1207)),

		Off_HAND_STEEL_Dagger(30, 75, new Item[] { new Item(2353) }, new Item(
				25969)),

		STEEL_HATCHET(31, 75, new Item[] { new Item(2353) }, new Item(1353)),

		STEEL_MACE(32, 75, new Item[] { new Item(2353) }, new Item(1424)),

		OFF_HAND_STEEL_MACE(32, 75, new Item[] { new Item(2353) }, new Item(
				25678)),

		STEEL_HELM(33, 75, new Item[] { new Item(2353) }, new Item(1141)),

		STEEL_BLOTS_UNF(33, 75, new Item[] { new Item(2353) }, new Item(9378,
				10)),

		STEEL_SWORD(34, 75, new Item[] { new Item(2353) }, new Item(1281)),

		OFF_HAND_STEEL_SWORD(34, 75, new Item[] { new Item(2353) }, new Item(
				25714)),

		STEEL_DART_TIP(34, 75, new Item[] { new Item(2353) }, new Item(821, 10)),

		STEEL_NAILS(34, 75, new Item[] { new Item(2353) }, new Item(1539, 15)),

		STEEL_ARROWHEADS(35, 75, new Item[] { new Item(2353) },
				new Item(41, 15)),

		CANNONBALL(35, 75.6, new Item[] { new Item(2353) }, new Item(2, 4)),

		STEEL_SCIMITAR(35, 75, new Item[] { new Item(2353, 2) }, new Item(1325)),

		OFF_HAND_STEEL_SCIMITAR(35, 75, new Item[] { new Item(2353, 2) },
				new Item(25747)),

		STEEL_PICKAXE(35, 75, new Item[] { new Item(2353, 2) }, new Item(1269)),

		STEEL_LIMBS(36, 75, new Item[] { new Item(2353, 1) }, new Item(9425)),

		STEEL_LONGSWORD(36, 75, new Item[] { new Item(2353, 2) },
				new Item(1295)),

		OFF_HAND_STEEL_LONGSWORD(36, 75, new Item[] { new Item(2353, 2) },
				new Item(25729)),

		STEEL_KNIFE(37, 75, new Item[] { new Item(2353) }, new Item(865, 5)),

		OFF_HAND_STEEL_KNIFE(37, 75, new Item[] { new Item(2353) }, new Item(
				25898, 5)),

		STEEL_THROWINAXE(37, 75, new Item[] { new Item(2353) },
				new Item(802, 5)),

		OFF_HANDSTEEL_THROWINAXE(37, 75, new Item[] { new Item(2353) },
				new Item(25905, 5)),

		STEEL_FULL_HELM(37, 75, new Item[] { new Item(2353, 2) },
				new Item(1157)),

		STEEL_SQ_SHIELD(38, 75, new Item[] { new Item(2353, 2) },
				new Item(1177)),

		STEEL_WARHAMMER(39, 112.5, new Item[] { new Item(2353, 3) }, new Item(
				1339)),

		OFF_HAND_STEEL_WARHAMMER(39, 112.5, new Item[] { new Item(2353, 3) },
				new Item(25783)),

		STEEL_BATTLEAXE(40, 112.5, new Item[] { new Item(2353, 3) }, new Item(
				1365)),

		OFF_HAND_STEEL_BATTLEAXE(40, 112.5, new Item[] { new Item(2353, 3) },
				new Item(25765)),

		STEEL_CHAINBODY(41, 112.5, new Item[] { new Item(2353, 3) }, new Item(
				1105)),

		STEEL_KITESHIELD(42, 112.5, new Item[] { new Item(2353, 3) }, new Item(
				1193)),

		STEEL_CLAW(43, 75, new Item[] { new Item(2353, 2) }, new Item(3097)),

		OFF_HAND_STEEL_CLAW(43, 75, new Item[] { new Item(2353, 2) }, new Item(
				25937)),

		STEEL_2H_SWORD(44, 112.5, new Item[] { new Item(2353, 3) }, new Item(
				1311)),

		STEEL_PLATELEGS(46, 112.5, new Item[] { new Item(2353, 3) }, new Item(
				1069)),

		STEEL_PLATESKIRT(46, 112.5, new Item[] { new Item(2353, 3) }, new Item(
				1083)),

		STEEL_PLATEBODY(48, 187.5, new Item[] { new Item(2353, 5) }, new Item(
				1119)),

		// Mithril
		MITHRIL_DAGGER(50, 50, new Item[] { new Item(2359) }, new Item(1209)),

		Off_HAND_MITHRIL_Dagger(50, 50, new Item[] { new Item(2359) },
				new Item(25700)),

		MITHRIL_HATCHET(51, 50, new Item[] { new Item(2359) }, new Item(1355)),

		MITHRIL_MACE(52, 50, new Item[] { new Item(2359
				) }, new Item(1428)),

		OFF_HAND_MITHRIL_MACE(52, 50, new Item[] { new Item(2359) }, new Item(
				25682)),

		MITHRIL_HELM(53, 50, new Item[] { new Item(2359) }, new Item(1143)),

		MITHRIL_BLOTS_UNF(53, 50, new Item[] { new Item(2359) }, new Item(9379,
				10)),

		MITHRIL_SWORD(54, 50, new Item[] { new Item(2359) }, new Item(1285)),

		OFF_HAND_MITHRIL_SWORD(54, 50, new Item[] { new Item(2359) }, new Item(
				25718)),

		MITHRIL_DART_TIP(54, 50, new Item[] { new Item(2359) }, new Item(822,
				10)),

		MITHRIL_NAILS(54, 50, new Item[] { new Item(2359) }, new Item(4822, 15)),

		MITHRIL_ARROWHEADS(55, 50, new Item[] { new Item(2359) }, new Item(42,
				15)),

		MITHRIL_SCIMITAR(55, 100, new Item[] { new Item(2359, 2) }, new Item(
				1329)),

		OFF_HAND_MITHRIL_SCIMITAR(55, 100, new Item[] { new Item(2359, 2) },
				new Item(25751)),

		MITHRIL_PICKAXE(55, 100, new Item[] { new Item(2359, 2) }, new Item(
				1273)),

		MITHRIL_LIMBS(56, 50, new Item[] { new Item(2359, 1) }, new Item(9427)),

		MITHRIL_LONGSWORD(56, 100, new Item[] { new Item(2359, 2) }, new Item(
				1299)),

		OFF_HAND_MITHRIL_LONGSWORD(56, 100, new Item[] { new Item(2359, 2) },
				new Item(25733)),

		MITHRIL_KNIFE(57, 50, new Item[] { new Item(2359) }, new Item(866, 5)),

		OFF_HAND_MITHRIL_KNIFE(57, 50, new Item[] { new Item(2359) }, new Item(
				25899, 5)),

		MITHRIL_THROWINAXE(57, 50, new Item[] { new Item(2359) }, new Item(806,
				5)),

		OFF_HANDMITHRIL_THROWINAXE(57, 50, new Item[] { new Item(2359) },
				new Item(25906, 5)),

		MITHRIL_FULL_HELM(57, 100, new Item[] { new Item(2359, 2) }, new Item(
				1159)),

		MITHRIL_SQ_SHIELD(58, 100, new Item[] { new Item(2359, 2) }, new Item(
				1181)),

		MITHRIL_GRAPPLE_TIP(59, 50, new Item[] { new Item(2359, 1) },
				new Item(9416)),

		MITHRIL_WARHAMMER(59, 150, new Item[] { new Item(2359, 3) },
				new Item(1343)),

		OFF_HAND_MITHRIL_WARHAMMER(59, 150, new Item[] { new Item(2359, 3) },
				new Item(25787)),

		MITHRIL_BATTLEAXE(60, 150, new Item[] { new Item(2359, 3) },
				new Item(1369)),

		OFF_HAND_MITHRIL_BATTLEAXE(60, 150, new Item[] { new Item(2359, 3) },
				new Item(25769)),

		MITHRIL_CHAINBODY(61, 150, new Item[] { new Item(2359, 3) },
				new Item(1109)),

		MITHRIL_KITESHIELD(62, 150, new Item[] { new Item(2359, 3) },
				new Item(1197)),

		MITHRIL_CLAW(63, 100, new Item[] { new Item(2359, 2) }, new Item(3099)),

		OFF_HAND_MITHRIL_CLAW(63, 100, new Item[] { new Item(2359, 2) },
				new Item(25941)),

		MITHRIL_2H_SWORD(64, 150, new Item[] { new Item(2359, 3) }, new Item(
				1315)),

		MITHRIL_PLATELEGS(66, 150, new Item[] { new Item(2359, 3) }, new Item(
				1071)),

		MITHRIL_PLATESKIRT(66, 150, new Item[] { new Item(2359, 3) }, new Item(
				1085)),

		MITHRIL_PLATEBODY(68, 250, new Item[] { new Item(2359, 5) }, new Item(
				1121)),

		/**
		 * Adamant
		 */

		ADAMANT_DAGGER(70, 62.5, new Item[] { new Item(2361) }, new Item(1211)),

		OFF_HAND_ADAMANT_DAGGER(70, 62.5, new Item[] { new Item(2361) },
				new Item(25702)),

		ADAMANT_HATCHET(71, 62.5, new Item[] { new Item(2361) }, new Item(1357)),

		ADAMANT_MACE(72, 62.5, new Item[] { new Item(2361) }, new Item(1430)),

		OFF_HAND_ADAMANT_MACE(73, 62.5, new Item[] { new Item(2361) },
				new Item(25684)),

		ADAMANT_HELM(73, 62.5, new Item[] { new Item(2361) }, new Item(1145)),

		ADAMANT_BOLTS(74, 62.5, new Item[] { new Item(2361) }, new Item(9380,
				10)),

		ADAMANT_SWORD(74, 62.5, new Item[] { new Item(2361) }, new Item(1287)),

		OFF_HAND_ADAMANT_SWORD(74, 62.5, new Item[] { new Item(2361) },
				new Item(25720)),

		ADAMANT_DART_TIP(74, 62.5, new Item[] { new Item(2361) }, new Item(823)),

		ADAMANT_NAILS(74, 62.5, new Item[] { new Item(2361) }, new Item(4823,
				15)),

		ADAMANT_ARROWHEADS(75, 62.5, new Item[] { new Item(2361) }, new Item(
				43, 15)),

		ADAMANT_SCIMITAR(75, 125, new Item[] { new Item(2361, 2) }, new Item(
				1331)),

		OFF_HAND_ADAMANT_SCIMITAR(75, 125, new Item[] { new Item(2361, 2) },
				new Item(25753)),

		ADAMANT_PICKAXE(75, 125, new Item[] { new Item(2361, 2) }, new Item(
				1271)),

		ADAMANT_LIMBS(76, 62.5, new Item[] { new Item(2361) }, new Item(9429)),

		ADAMANT_LONGSWORD(76, 125, new Item[] { new Item(2361, 2) }, new Item(
				1301)),

		OFF_HAND_ADAMANT_LONGSWORD(76, 125, new Item[] { new Item(2361, 2) },
				new Item(25735)),

		ADAMANT_KNIFE(77, 62.5, new Item[] { new Item(2361) }, new Item(867)),

		OFF_HAND_ADAMANT_KNIFE(77, 62.5, new Item[] { new Item(2361) },
				new Item(25900)),

		ADAMANT_THOWING_AXE(77, 62.5, new Item[] { new Item(2361) }, new Item(
				804, 5)),

		OFF_HAND_ADAMANT_THOWING_AXE(77, 62.5, new Item[] { new Item(2361) },
				new Item(25907, 5)),

		ADAMANT_FULL_HELM(77, 62.5, new Item[] { new Item(2361, 2) }, new Item(
				1161)),

		ADAMANT_SQ_SHIELD(78, 125, new Item[] { new Item(2361, 2) }, new Item(
				1183)),

		ADAMANT_WARHAMMER(79, 187.5, new Item[] { new Item(2361, 3) },
				new Item(1345)),

		OFF_HAND_ADAMANT_WARHAMMER(79, 187.5, new Item[] { new Item(2361, 3) },
				new Item(25789)),

		ADAMANT_BATTLEAXE(80, 187.5, new Item[] { new Item(2361, 3) },
				new Item(1371)),

		OFF_HAND_ADAMANT_BATTLEAXE(80, 187.5, new Item[] { new Item(2361, 3) },
				new Item(25771)),

		ADAMANT_CHAINBODY(81, 187.5, new Item[] { new Item(2361, 3) },
				new Item(1111)),

		ADAMANT_KITESHIELD(82, 187.5, new Item[] { new Item(2361, 3) },
				new Item(1199)),

		ADAMANT_CLAW(83, 125, new Item[] { new Item(2361, 2) }, new Item(3100)),

		OFF_HAND_ADAMANT_CLAW(83, 125, new Item[] { new Item(2361, 2) },
				new Item(25943)),

		ADAMANT_2H_SWORD(84, 187.5, new Item[] { new Item(2361, 3) }, new Item(
				1317)),

		ADAMANT_PLATELEGS(86, 187.5, new Item[] { new Item(2361, 3) },
				new Item(1073)),

		ADAMANT_PLATESKIRT(86, 187.5, new Item[] { new Item(2361, 3) },
				new Item(1091)),

		ADAMANT_PLATEBODY(88, 312.5, new Item[] { new Item(2361, 5) },
				new Item(1123)),

		/**
		 * Rune
		 */
		RUNE_DAGGER(85, 75, new Item[] { new Item(2363) }, new Item(1213)),

		OFF_HAND_RUNE_DAGGER(85, 75, new Item[] { new Item(2363) }, new Item(
				25704)),

		RUNE_HATCHET(86, 75, new Item[] { new Item(2363) }, new Item(1359)),

		RUNE_MACE(87, 75, new Item[] { new Item(2363) }, new Item(1432)),

		OFF_HAND_RUNE_MACE(87, 75, new Item[] { new Item(2363) }, new Item(
				25686)),

		RUNE_HELM(88, 75, new Item[] { new Item(2363) }, new Item(1147)),

		RUNE_BOLTS(88, 75, new Item[] { new Item(2363) }, new Item(9381, 10)),

		RUNE_SWORD(89, 75, new Item[] { new Item(2363) }, new Item(1289)),

		OFF_HAND_RUNE_SWORD(89, 75, new Item[] { new Item(2363) }, new Item(
				25722)),

		RUNE_DART_TIP(89, 75, new Item[] { new Item(2363) }, new Item(824)),

		RUNE_NAILS(89, 75, new Item[] { new Item(2363) }, new Item(4824, 15)),

		RUNE_ARROWHEADS(90, 75, new Item[] { new Item(2363) }, new Item(44, 15)),

		RUNE_SCIMITAR(90, 150, new Item[] { new Item(2363, 2) }, new Item(1333)),

		OFF_HAND_RUNE_SCIMITAR(90, 150, new Item[] { new Item(2363, 2) },
				new Item(25755)),

		RUNE_PICKAXE(90, 150, new Item[] { new Item(2363, 2) }, new Item(1275)),

		RUNE_LIMBS(91, 75, new Item[] { new Item(2363) }, new Item(9431)),

		RUNE_LONGSWORD(91, 150, new Item[] { new Item(2363, 2) },
				new Item(1303)),

		OFF_HAND_RUNE_LONGSWORD(91, 150, new Item[] { new Item(2363, 2) },
				new Item(25737)),

		RUNE_KNIFE(92, 75, new Item[] { new Item(2363) }, new Item(868)),

		OFF_HAND_RUNE_KNIFE(92, 75, new Item[] { new Item(2363) }, new Item(
				25901)),

		RUNE_THOWING_AXE(92, 75, new Item[] { new Item(2363) },
				new Item(805, 5)),

		OFF_HAND_RUNE_THOWING_AXE(92, 75, new Item[] { new Item(2363) },
				new Item(25908, 5)),

		RUNE_FULL_HELM(92, 75, new Item[] { new Item(2363, 2) }, new Item(1163)),

		RUNE_SQ_SHIELD(93, 150, new Item[] { new Item(2363, 2) },
				new Item(1185)),

		RUNE_WARHAMMER(94, 225, new Item[] { new Item(2363, 3) },
				new Item(1347)),

		OFF_HAND_RUNE_WARHAMMER(94, 225, new Item[] { new Item(2363, 3) },
				new Item(25791)),

		RUNE_BATTLEAXE(95, 225, new Item[] { new Item(2363, 3) },
				new Item(1373)),

		OFF_HAND_RUNE_BATTLEAXE(95, 225, new Item[] { new Item(2363, 3) },
				new Item(25773)),

		RUNE_CHAINBODY(96, 225, new Item[] { new Item(2363, 3) },
				new Item(1113)),

		RUNE_KITESHIELD(97, 225, new Item[] { new Item(2363, 3) }, new Item(
				1201)),

		RUNE_CLAW(98, 150, new Item[] { new Item(2363, 2) }, new Item(3101)),

		OFF_HAND_RUNE_CLAW(98, 150, new Item[] { new Item(2363, 2) }, new Item(
				25945)),

		RUNE_2H_SWORD(99, 225, new Item[] { new Item(2363, 3) }, new Item(1319)),

		RUNE_PLATELEGS(99, 225, new Item[] { new Item(2363, 3) },
				new Item(1079)),

		RUNE_PLATESKIRT(99, 225, new Item[] { new Item(2363, 3) }, new Item(
				1093)),

		RUNE_PLATEBODY(99, 375, new Item[] { new Item(2363, 5) },
				new Item(1127)),

		/**
		 * Gold
		 */

		GOLD_BOWL(50, 100, new Item[] { new Item(2357, 2) }, new Item(721)),

		GOLD_HELMET(50, 100, new Item[] { new Item(2357, 3) }, new Item(4567)),

		CANNON_BALL(50, 170, new Item[] { new Item(2357, 1) }, new Item(4579)),

		/**
		 * Novite
		 */

		NOVITE_ARROWHEADS(1, 10, new Item[] { new Item(17650, 1) }, new Item(
				17885, 20)),

		NOVITE_DAGGER(1, 10, new Item[] { new Item(17650, 1) }, new Item(16757)),

		OFF_HAND_NOVITE_DAGGER(1, 10, new Item[] { new Item(17650, 1) },
				new Item(27713)),

		NOVITE_BOOTS(1, 10.5, new Item[] { new Item(17650, 1) },
				new Item(16339)),

		NOVITE_GAUNTLETS(1, 10.5, new Item[] { new Item(17650, 1) }, new Item(
				16273)),

		NOVITE_HATCHET(2, 11, new Item[] { new Item(17650, 1) },
				new Item(16361)),

		NOVITE_PICKAXE(2, 11, new Item[] { new Item(17650, 1) },
				new Item(16295)),

		NOVITE_KNIFE(2, 11.5, new Item[] { new Item(17650, 1) }, new Item(
				27860, 10)),

		OFF_HAND_NOVITE_KNIFE(2, 11.5, new Item[] { new Item(17650, 1) },
				new Item(27713, 10)),

		NOVITE_WARHAMMER(3, 23, new Item[] { new Item(17650, 2) }, new Item(
				17019)),

		OFF_HAND_NOVITE_WARHAMMER(3, 23, new Item[] { new Item(17650, 2) },
				new Item(27665)),

		NOVITE_RAPIER(3, 23, new Item[] { new Item(17650, 2) }, new Item(16935)),

		OFF_HAND_NOVITE_RAPIER(3, 23, new Item[] { new Item(17650, 2) },
				new Item(27643)),

		NOVITE_LONGSWORD(4, 24, new Item[] { new Item(17650, 2) }, new Item(
				16383)),

		OFF_HAND_NOVITE_LONGSWORD(4, 24, new Item[] { new Item(17650, 2) },
				new Item(27893)),

		NOVITE_FULL_HELM(5, 25, new Item[] { new Item(17650, 2) }, new Item(
				16691)),

		NOVITE_BATTLEAXE(5, 25, new Item[] { new Item(17650, 2) }, new Item(
				15753)),

		OFF_HAND_NOVITE_BATTLEAXE(5, 25, new Item[] { new Item(17650, 2) },
				new Item(27915)),

		NOVITE_KITESHIELD(6, 39, new Item[] { new Item(17650, 3) }, new Item(
				17341)),

		NOVITE_CHAINBODY(6, 39, new Item[] { new Item(17650, 3) }, new Item(
				16713)),

		NOVITE_PLATELEGS(7, 40.5, new Item[] { new Item(17650, 3) }, new Item(
				16669)),

		NOVITE_PLATESKIRT(7, 40.5, new Item[] { new Item(17650, 3) }, new Item(
				16647)),

		NOVITE_SPEAR(7, 54, new Item[] { new Item(17650, 4) }, new Item(17063)),

		NOVITE_MAUL(8, 56, new Item[] { new Item(17650, 4) }, new Item(16405)),

		NOVITE_2H(8, 56, new Item[] { new Item(17650, 4) }, new Item(16889)),

		NOVITE_PLATEBODY(9, 72.5, new Item[] { new Item(17650, 5) }, new Item(
				17239)),

		/**
		 * Bathus
		 */

		BATHUS_ARROWHEADS(10, 19, new Item[] { new Item(17652, 1) }, new Item(
				17890, 20)),

		BATHUS_DAGGER(10, 19, new Item[] { new Item(17652, 1) },
				new Item(16765)),

		OFF_HAND_BATHUS_DAGGER(10, 19, new Item[] { new Item(17652, 1) },
				new Item(27976)),

		BATHUS_BOOTS(11, 19.5, new Item[] { new Item(17652, 1) }, new Item(
				16341)),

		BATHUS_GAUNTLETS(11, 19.5, new Item[] { new Item(17652, 1) }, new Item(
				16275)),

		BATHUS_HATCHET(12, 20, new Item[] { new Item(17652, 1) }, new Item(
				16363)),

		BATHUS_PICKAXE(12, 20, new Item[] { new Item(17652, 1) }, new Item(
				16297)),

		BATHUS_KNIFE(12, 20.5, new Item[] { new Item(17652, 1) }, new Item(
				27964, 10)),

		OFF_HAND_BATHUS_KNIFE(12, 20.5, new Item[] { new Item(17652, 1) },
				new Item(27714, 10)),

		BATHUS_WARHAMMER(13, 41, new Item[] { new Item(17652, 2) }, new Item(
				17021)),

		OFF_HAND_BATHUS_WARHAMMER(13, 41, new Item[] { new Item(17652, 2) },
				new Item(27668)),

		BATHUS_RAPIER(13, 41, new Item[] { new Item(17652, 2) },
				new Item(16937)),

		OFF_HAND_BATHUS_RAPIER(13, 41, new Item[] { new Item(17652, 2) },
				new Item(27645)),

		BATHUS_LONGSWORD(14, 42, new Item[] { new Item(17652, 2) }, new Item(
				16385)),

		OFF_HAND_BATHUS_LONGSWORD(14, 42, new Item[] { new Item(17652, 2) },
				new Item(27895)),

		BATHUS_FULL_HELM(15, 43, new Item[] { new Item(17652, 2) }, new Item(
				16693)),

		BATHUS_BATTLEAXE(15, 43, new Item[] { new Item(17652, 2) }, new Item(
				15755)),

		OFF_HAND_BATHUS_BATTLEAXE(15, 43, new Item[] { new Item(17652, 2) },
				new Item(27917)),

		BATHUS_KITESHIELD(16, 66, new Item[] { new Item(17652, 3) }, new Item(
				17343)),

		BATHUS_CHAINBODY(16, 66, new Item[] { new Item(17652, 3) }, new Item(
				16715)),

		BATHUS_PLATELEGS(17, 67.5, new Item[] { new Item(17652, 3) }, new Item(
				16671)),

		BATHUS_PLATESKIRT(17, 67.5, new Item[] { new Item(17652, 3) },
				new Item(16649)),

		BATHUS_SPEAR(17, 90, new Item[] { new Item(17652, 4) }, new Item(17071)),

		BATHUS_MAUL(18, 92, new Item[] { new Item(17652, 4) }, new Item(16407)),

		BATHUS_2H(18, 92, new Item[] { new Item(17652, 4) }, new Item(16891)),

		BATHUS_PLATEBODY(19, 117.5, new Item[] { new Item(17652, 5) },
				new Item(17241)),

		/**
		 * Marmaros
		 */

		MARMAROS_ARROWHEADS(20, 28, new Item[] { new Item(17654, 1) },
				new Item(17895, 20)),

		MARMAROS_DAGGER(20, 28, new Item[] { new Item(17654, 1) }, new Item(
				16773)),

		OFF_HAND_MARMAROS_DAGGER(20, 28, new Item[] { new Item(17654, 1) },
				new Item(27978)),

		MARMAROS_BOOTS(21, 28.5, new Item[] { new Item(17654, 1) }, new Item(
				16343)),

		MARMAROS_GAUNTLETS(21, 28.5, new Item[] { new Item(17654, 1) },
				new Item(16277)),

		MARMAROS_HATCHET(22, 29, new Item[] { new Item(17654, 1) }, new Item(
				16365)),

		MARMAROS_PICKAXE(22, 29, new Item[] { new Item(17654, 1) }, new Item(
				16299)),

		MARMAROS_KNIFE(22, 29.5, new Item[] { new Item(17654, 1) }, new Item(
				27965, 10)),

		OFF_HAND_MARMAROS_KNIFE(22, 29.5, new Item[] { new Item(17654, 1) },
				new Item(27715, 10)),

		MARMAROS_WARHAMMER(23, 59, new Item[] { new Item(17654, 2) }, new Item(
				17023)),

		OFF_HAND_MARMAROS_WARHAMMER(23, 59, new Item[] { new Item(17654, 2) },
				new Item(27669)),

		MARMAROS_RAPIER(23, 59, new Item[] { new Item(17654, 2) }, new Item(
				16939)),

		OFF_HAND_MARMAROS_RAPIER(23, 59, new Item[] { new Item(17654, 2) },
				new Item(27647)),

		MARMAROS_LONGSWORD(24, 60, new Item[] { new Item(17654, 2) }, new Item(
				16387)),

		OFF_HAND_MARMAROS_LONGSWORD(24, 60, new Item[] { new Item(17654, 2) },
				new Item(27897)),

		MARMAROS_FULL_HELM(25, 61, new Item[] { new Item(17654, 2) }, new Item(
				16695)),

		MARMAROS_BATTLEAXE(25, 61, new Item[] { new Item(17654, 2) }, new Item(
				15757)),

		OFF_HAND_MARMAROS_BATTLEAXE(25, 61, new Item[] { new Item(17654, 2) },
				new Item(27919)),

		MARMAROS_KITESHIELD(26, 93, new Item[] { new Item(17654, 3) },
				new Item(17345)),

		MARMAROS_CHAINBODY(26, 93, new Item[] { new Item(17654, 3) }, new Item(
				16717)),

		MARMAROS_PLATELEGS(27, 94.5, new Item[] { new Item(17654, 3) },
				new Item(16673)),

		MARMAROS_PLATESKIRT(27, 94.5, new Item[] { new Item(17654, 3) },
				new Item(16651)),

		MARMAROS_SPEAR(27, 126, new Item[] { new Item(17654, 4) }, new Item(
				17079)),

		MARMAROS_MAUL(28, 128, new Item[] { new Item(17654, 4) }, new Item(
				16409)),

		MARMAROS_2H(28, 128, new Item[] { new Item(17654, 4) }, new Item(16893)),

		MARMAROS_PLATEBODY(29, 162.5, new Item[] { new Item(17654, 5) },
				new Item(17243)),

		/**
		 * Kratonite
		 */

		KRATONITE_ARROWHEADS(30, 37, new Item[] { new Item(17656, 1) },
				new Item(17900, 20)),

		KRATONITE_DAGGER(30, 37, new Item[] { new Item(17656, 1) }, new Item(
				16781)),

		OFF_HAND_KRATONITE_DAGGER(30, 37, new Item[] { new Item(17656, 1) },
				new Item(27980)),

		KRATONITE_BOOTS(31, 37.5, new Item[] { new Item(17656, 1) }, new Item(
				16345)),

		KRATONITE_GAUNTLETS(31, 37.5, new Item[] { new Item(17656, 1) },
				new Item(16279)),

		KRATONITE_HATCHET(32, 38, new Item[] { new Item(17656, 1) }, new Item(
				16367)),

		KRATONITE_PICKAXE(32, 38, new Item[] { new Item(17656, 1) }, new Item(
				16301)),

		KRATONITE_KNIFE(32, 38.5, new Item[] { new Item(17656, 1) }, new Item(
				27966, 10)),

		OFF_HAND_KRATONITE_KNIFE(32, 38.5, new Item[] { new Item(17656, 1) },
				new Item(27716, 10)),

		KRATONITE_WARHAMMER(33, 77, new Item[] { new Item(17656, 2) },
				new Item(17025)),

		OFF_HAND_KRATONITE_WARHAMMER(33, 77, new Item[] { new Item(17656, 2) },
				new Item(27671)),

		KRATONITE_RAPIER(33, 77, new Item[] { new Item(17656, 2) }, new Item(
				16941)),

		OFF_HAND_KRATONITE_RAPIER(33, 77, new Item[] { new Item(17656, 2) },
				new Item(27649)),

		KRATONITE_LONGSWORD(34, 78, new Item[] { new Item(17656, 2) },
				new Item(16389)),

		OFF_HAND_KRATONITE_LONGSWORD(34, 78, new Item[] { new Item(17656, 2) },
				new Item(27899)),

		KRATONITE_FULL_HELM(35, 79, new Item[] { new Item(17656, 2) },
				new Item(16697)),

		KRATONITE_BATTLEAXE(35, 79, new Item[] { new Item(17656, 2) },
				new Item(15759)),

		OFF_HAND_KRATONITE_BATTLEAXE(35, 79, new Item[] { new Item(17656, 2) },
				new Item(27921)),

		KRATONITE_KITESHIELD(36, 120, new Item[] { new Item(17656, 3) },
				new Item(17347)),

		KRATONITE_CHAINBODY(36, 120, new Item[] { new Item(17656, 3) },
				new Item(16719)),

		KRATONITE_PLATELEGS(37, 121.5, new Item[] { new Item(17656, 3) },
				new Item(16675)),

		KRATONITE_PLATESKIRT(37, 121.5, new Item[] { new Item(17656, 3) },
				new Item(16653)),

		KRATONITE_SPEAR(37, 162, new Item[] { new Item(17656, 4) }, new Item(
				17087)),

		KRATONITE_MAUL(38, 164, new Item[] { new Item(17656, 4) }, new Item(
				16411)),

		KRATONITE_2H(38, 164, new Item[] { new Item(17656, 4) },
				new Item(16895)),

		KRATONITE_PLATEBODY(39, 207.5, new Item[] { new Item(17656, 5) },
				new Item(17245)),

		/**
		 * Fractite
		 */

		FRACTITE_ARROWHEADS(40, 46, new Item[] { new Item(17658, 1) },
				new Item(17905, 20)),

		FRACTITE_DAGGER(40, 46, new Item[] { new Item(17658, 1) }, new Item(
				16789)),

		OFF_HAND_FRACTITE_DAGGER(40, 46, new Item[] { new Item(17658, 1) },
				new Item(27982)),

		FRACTITE_BOOTS(41, 46.5, new Item[] { new Item(17658, 1) }, new Item(
				16347)),

		FRACTITE_GAUNTLETS(41, 46.5, new Item[] { new Item(17658, 1) },
				new Item(16281)),

		FRACTITE_HATCHET(42, 47, new Item[] { new Item(17658, 1) }, new Item(
				16369)),

		FRACTITE_PICKAXE(42, 47, new Item[] { new Item(17658, 1) }, new Item(
				16303)),

		FRACTITE_KNIFE(42, 47.5, new Item[] { new Item(17658, 1) }, new Item(
				27967, 10)),

		OFF_HAND_FRACTITE_KNIFE(42, 47.5, new Item[] { new Item(17658, 1) },
				new Item(27717, 10)),

		FRACTITE_WARHAMMER(43, 95, new Item[] { new Item(17658, 2) }, new Item(
				17027)),

		OFF_HAND_FRACTITE_WARHAMMER(43, 95, new Item[] { new Item(17658, 2) },
				new Item(27673)),

		FRACTITE_RAPIER(43, 95, new Item[] { new Item(17658, 2) }, new Item(
				16943)),

		OFF_HAND_FRACTITE_RAPIER(43, 95, new Item[] { new Item(17658, 2) },
				new Item(27651)),

		FRACTITE_LONGSWORD(44, 96, new Item[] { new Item(17658, 2) }, new Item(
				16391)),

		OFF_HAND_FRACTITE_LONGSWORD(44, 96, new Item[] { new Item(17658, 2) },
				new Item(27901)),

		FRACTITE_FULL_HELM(45, 97, new Item[] { new Item(17658, 2) }, new Item(
				16699)),

		FRACTITE_BATTLEAXE(45, 97, new Item[] { new Item(17658, 2) }, new Item(
				15761)),

		OFF_HAND_FRACTITE_BATTLEAXE(45, 97, new Item[] { new Item(17658, 2) },
				new Item(27923)),

		FRACTITE_KITESHIELD(46, 147, new Item[] { new Item(17658, 3) },
				new Item(17349)),

		FRACTITE_CHAINBODY(46, 147, new Item[] { new Item(17658, 3) },
				new Item(16721)),

		FRACTITE_PLATELEGS(47, 148.5, new Item[] { new Item(17658, 3) },
				new Item(16677)),

		FRACTITE_PLATESKIRT(47, 148.5, new Item[] { new Item(17658, 3) },
				new Item(16655)),

		FRACTITE_SPEAR(47, 198, new Item[] { new Item(17658, 4) }, new Item(
				17095)),

		FRACTITE_MAUL(48, 200, new Item[] { new Item(17658, 4) }, new Item(
				16413)),

		FRACTITE_2H(48, 200, new Item[] { new Item(17658, 4) }, new Item(16897)),

		FRACTITE_PLATEBODY(49, 252.5, new Item[] { new Item(17658, 5) },
				new Item(17247)),

		/**
		 * Zephyrium
		 */

		ZEPHYRIUM_ARROWHEADS(50, 55, new Item[] { new Item(17660, 1) },
				new Item(17910, 20)),

		ZEPHYRIUM_DAGGER(50, 55, new Item[] { new Item(17660, 1) }, new Item(
				16797)),

		OFF_HAND_ZEPHYRIUM_DAGGER(50, 55, new Item[] { new Item(17660, 1) },
				new Item(27984)),

		ZEPHYRIUM_BOOTS(51, 55.5, new Item[] { new Item(17660, 1) }, new Item(
				16349)),

		ZEPHYRIUM_GAUNTLETS(51, 55.5, new Item[] { new Item(17660, 1) },
				new Item(16283)),

		ZEPHYRIUM_HATCHET(52, 56, new Item[] { new Item(17660, 1) }, new Item(
				16371)),

		ZEPHYRIUM_PICKAXE(52, 56, new Item[] { new Item(17660, 1) }, new Item(
				16305)),

		ZEPHYRIUM_KNIFE(52, 56.5, new Item[] { new Item(17660, 1) }, new Item(
				27968, 10)),

		OFF_HAND_ZEPHYRIUM_KNIFE(52, 56.5, new Item[] { new Item(17660, 1) },
				new Item(27718, 10)),

		ZEPHYRIUM_WARHAMMER(53, 113, new Item[] { new Item(17660, 2) },
				new Item(17029)),

		OFF_HAND_ZEPHYRIUM_WARHAMMER(53, 113,
				new Item[] { new Item(17660, 2) }, new Item(27675)),

		ZEPHYRIUM_RAPIER(53, 113, new Item[] { new Item(17660, 2) }, new Item(
				16945)),

		OFF_HAND_ZEPHYRIUM_RAPIER(53, 113, new Item[] { new Item(17660, 2) },
				new Item(27653)),

		ZEPHYRIUM_LONGSWORD(54, 114, new Item[] { new Item(17660, 2) },
				new Item(16393)),

		OFF_HAND_ZEPHYRIUM_LONGSWORD(54, 114,
				new Item[] { new Item(17660, 2) }, new Item(27903)),

		ZEPHYRIUM_FULL_HELM(55, 115, new Item[] { new Item(17660, 2) },
				new Item(16701)),

		ZEPHYRIUM_BATTLEAXE(55, 115, new Item[] { new Item(17660, 2) },
				new Item(15763)),

		OFF_HAND_ZEPHYRIUM_BATTLEAXE(55, 115,
				new Item[] { new Item(17660, 2) }, new Item(27925)),

		ZEPHYRIUM_KITESHIELD(56, 174, new Item[] { new Item(17660, 3) },
				new Item(17351)),

		ZEPHYRIUM_CHAINBODY(56, 174, new Item[] { new Item(17660, 3) },
				new Item(16723)),

		ZEPHYRIUM_PLATELEGS(57, 175.5, new Item[] { new Item(17660, 3) },
				new Item(16679)),

		ZEPHYRIUM_PLATESKIRT(57, 175.5, new Item[] { new Item(17660, 3) },
				new Item(16657)),

		ZEPHYRIUM_SPEAR(57, 234, new Item[] { new Item(17660, 4) }, new Item(
				17103)),

		ZEPHYRIUM_MAUL(58, 236, new Item[] { new Item(17660, 4) }, new Item(
				16415)),

		ZEPHYRIUM_2H(58, 236, new Item[] { new Item(17660, 4) },
				new Item(16899)),

		ZEPHYRIUM_PLATEBODY(59, 297.5, new Item[] { new Item(17660, 5) },
				new Item(17249)),
		
		/**
		 * Argonite
		 */
		ARGONITE_ARROWHEADS(60, 64, new Item[] { new Item(17662, 1) },
				new Item(17915, 20)),

		ARGONITE_DAGGER(60, 64, new Item[] { new Item(17662, 1) }, new Item(
				16805)),

		OFFHAND_ARGONITE_DAGGER(60, 64, new Item[] { new Item(17662, 1) },
				new Item(27986)),

		ARGONITE_BOOTS(61, 64.5, new Item[] { new Item(17662, 1) }, new Item(
				16351)),

		ARGONITE_GAUNTLETS(61, 64.5, new Item[] { new Item(17662, 1) },
				new Item(16283)),

		ARGNONITE_HATCHET(62, 65, new Item[] { new Item(17662, 1) }, new Item(
				16373)),

		ARGONITE_PICKAXE(62, 65, new Item[] { new Item(17662, 1) }, new Item(
				16307)),

		ARGONITE_KNIFE(64, 65.5, new Item[] { new Item(17662, 1) }, new Item(
				27969, 10)),

		OFF_HAND_ARGONITE_KNIFE(64, 65.5, new Item[] { new Item(17662, 1) },
				new Item(27719, 10)),

		ARGONITE_WARHAMMER(63, 131, new Item[] { new Item(17662, 2) },
				new Item(17031)),

		OFF_HAND_ARGONITE_WARHAMMER(63, 131,
				new Item[] { new Item(17662, 2) }, new Item(27677)),

		ARGONITE_RAPIER(63, 131, new Item[] { new Item(17662, 2) }, new Item(
				16947)),

		OFF_HAND_ARGONITE_RAPIER(63, 131, new Item[] { new Item(17662, 2) },
				new Item(27655)),

		ARGONITE_LONGSWORD(64, 132, new Item[] { new Item(17662, 2) },
				new Item(16395)),

		OFF_HAND_ARGONITE_LONGSWORD(64, 132,
				new Item[] { new Item(17662, 2) }, new Item(27905)),

		ARGONITE_FULL_HELM(65, 133, new Item[] { new Item(17662, 2) },
				new Item(16703)),

		ARGONITE_BATTLEAXE(65, 133, new Item[] { new Item(17662, 2) },
				new Item(15765)),

		OFF_HAND_ARGONITE_BATTLEAXE(65, 133,
				new Item[] { new Item(17662, 2) }, new Item(27927)),

		ARGONITE_KITESHIELD(66, 201, new Item[] { new Item(17662, 3) },
				new Item(17353)),

		ARGONITE_CHAINBODY(66, 201, new Item[] { new Item(17662, 3) },
				new Item(16725)),

		ARGONITE_PLATELEGS(67, 202.5, new Item[] { new Item(17662, 3) },
				new Item(16681)),

		ARGONITE_PLATESKIRT(67, 202.5, new Item[] { new Item(17662, 3) },
				new Item(16659)),

		ARGONITE_SPEAR(67, 270, new Item[] { new Item(17662, 4) }, new Item(
				17111)),

		ARGONITE_MAUL(68, 272, new Item[] { new Item(17662, 4) }, new Item(
				16417)),

		ARGONITE_2H(68, 272, new Item[] { new Item(17662, 4) },
				new Item(16901)),

		ARGONITE_PLATEBODY(69, 342.5, new Item[] { new Item(17662, 5) },
				new Item(17251)),
		
		/**
		 * Katagon
		 */
		
		KATAGON_ARROWHEADS(70, 73, new Item[] { new Item(17664, 1) },
				new Item(17915, 20)),

		KATAGON_DAGGER(70, 73, new Item[] { new Item(17664, 1) }, new Item(
				16813)),

		OFFHAND_KATAGON_DAGGER(70, 7, new Item[] { new Item(17664, 1) },
				new Item(27988)),

		KATAGON_BOOTS(71, 73.5, new Item[] { new Item(17664, 1) }, new Item(
				16353)),

		KATAGON_GAUNTLETS(71, 73.5, new Item[] { new Item(17664, 1) },
				new Item(16287)),

		KATAGON_HATCHET(72, 74, new Item[] { new Item(17664, 1) }, new Item(
				16375)),

		KATAGON_PICKAXE(74, 74.5, new Item[] { new Item(17664, 1) }, new Item(
				16309)),

		KATAGON_KNIFE(74, 74.5, new Item[] { new Item(17664, 1) }, new Item(
				27970, 10)),

		OFF_HAND_KATAGON_KNIFE(74, 74.5, new Item[] { new Item(17664, 1) },
				new Item(27720, 10)),

		KATAGON_WARHAMMER(73, 149, new Item[] { new Item(17664, 2) },
				new Item(17033)),

		OFF_HAND_KATAGON_WARHAMMER(73, 149,
				new Item[] { new Item(17664, 2) }, new Item(27679)),

		KATAGON_RAPIER(73, 149, new Item[] { new Item(17664, 2) }, new Item(
				16949)),

		OFF_HAND_KATAGON_RAPIER(73, 149, new Item[] { new Item(17664, 2) },
				new Item(27657)),

		KATAGON_LONGSWORD(74, 150, new Item[] { new Item(17664, 2) },
				new Item(16397)),

		OFF_HAND_KATAGON_LONGSWORD(74, 150,
				new Item[] { new Item(17664, 2) }, new Item(27907)),

		KATAGON_FULL_HELM(75, 151, new Item[] { new Item(17664, 2) },
				new Item(16705)),

		KATAGON_BATTLEAXE(75, 151, new Item[] { new Item(17664, 2) },
				new Item(15767)),

		OFF_HAND_KATAGON_BATTLEAXE(75, 151,
				new Item[] { new Item(17664, 2) }, new Item(27929)),

		KATAGON_KITESHIELD(76, 219, new Item[] { new Item(17664, 3) },
				new Item(17355)),

		KATAGON_CHAINBODY(76, 219, new Item[] { new Item(17664, 3) },
				new Item(16727)),

		KATAGON_PLATELEGS(77, 229.5, new Item[] { new Item(17664, 3) },
				new Item(16683)),

		KATAGON_PLATESKIRT(77, 229.5, new Item[] { new Item(17664, 3) },
				new Item(16661)),

		KATAGON_SPEAR(77, 306, new Item[] { new Item(17664, 4) }, new Item(
				17119)),

		KATAGON_MAUL(78, 308, new Item[] { new Item(17664, 4) }, new Item(
				16419)),

		KATAGON_2H(78, 308, new Item[] { new Item(17664, 4) },
				new Item(16903)),

		KATAGON_PLATEBODY(78, 387.5, new Item[] { new Item(17664, 5) },
				new Item(17253)),
		
		/**
		 * Gorgonite
		 */
		
		GORGONITE_ARROWHEADS(80, 82, new Item[] { new Item(17666, 1) },
				new Item(17925, 20)),

		GORGONITE_DAGGER(80, 82, new Item[] { new Item(17666, 1) }, new Item(
				16821)),

		GORGONITE_KATAGON_DAGGER(80, 82, new Item[] { new Item(17666, 1) },
				new Item(27990)),

		GORGONITE_BOOTS(81, 82.5, new Item[] { new Item(17666, 1) }, new Item(
				16355)),

		GORGONITE_GAUNTLETS(81, 82.5, new Item[] { new Item(17666, 1) },
				new Item(16289)),

		GORGONITE_HATCHET(82, 83, new Item[] { new Item(17666, 1) }, new Item(
				16377)),

		GORGONITE_PICKAXE(82, 83, new Item[] { new Item(17666, 1) }, new Item(
				16311)),

		GORGONITE_KNIFE(84, 83.5, new Item[] { new Item(17666, 1) }, new Item(
				27971, 10)),

		GORGONITE_KATAGON_KNIFE(84, 83.5, new Item[] { new Item(17666, 1) },
				new Item(27721, 10)),

		GORGONITE_WARHAMMER(83, 167, new Item[] { new Item(17666, 2) },
				new Item(17035)),

		GORGONITE_KATAGON_WARHAMMER(83, 167,
				new Item[] { new Item(17666, 2) }, new Item(27681)),

		GORGONITE_RAPIER(83, 167, new Item[] { new Item(17666, 2) }, new Item(
				27659)),

		GORGONITE_KATAGON_RAPIER(83, 167, new Item[] { new Item(17666, 2) },
				new Item(27657)),

		GORGONITE_LONGSWORD(84, 168, new Item[] { new Item(17666, 2) },
				new Item(16399)),

		OFF_HAND_GORGONITE_LONGSWORD(84, 168,
				new Item[] { new Item(17666, 2) }, new Item(27909)),

		GORGONITE_FULL_HELM(85, 169, new Item[] { new Item(17666, 2) },
				new Item(16707)),

		GORGONITE_BATTLEAXE(85, 169, new Item[] { new Item(17666, 2) },
				new Item(15769)),

		GORGONITE_KATAGON_BATTLEAXE(85, 169,
				new Item[] { new Item(17666, 2) }, new Item(27931)),

		GORGONITE_KITESHIELD(86, 255, new Item[] { new Item(17666, 3) },
				new Item(16971)),

		GORGONITE_CHAINBODY(86, 255, new Item[] { new Item(17666, 3) },
				new Item(16729)),

		GORGONITE_PLATELEGS(87, 256.5, new Item[] { new Item(17666, 3) },
				new Item(16685)),

		GORGONITE_PLATESKIRT(87, 256.5, new Item[] { new Item(17666, 3) },
				new Item(16663)),

		GORGONITE_SPEAR(87, 342, new Item[] { new Item(17666, 4) }, new Item(
				17127)),

		GORGONITE_MAUL(88, 342, new Item[] { new Item(17666, 4) }, new Item(
				16421)),

		GORGONITE_2H(78, 342, new Item[] { new Item(17666, 4) },
				new Item(16905)),

		GORGONITE_PLATEBODY(89, 432.5, new Item[] { new Item(17666, 5) },
				new Item(17255)),
		/**
		 * Promethium 
	     */
		
		PROMETHIUM_ARROWHEADS(90, 91, new Item[] { new Item(17668, 1) },
				new Item(17915, 20)),

		PROMETHIUM_DAGGER(90, 91, new Item[] { new Item(17668, 1) }, new Item(
				16829)),

		OFFHAND_PROMETHIUM_DAGGER(90, 91, new Item[] { new Item(17668, 1) },
				new Item(27992)),

		PROMETHIUM_BOOTS(91, 91.5, new Item[] { new Item(17668, 1) }, new Item(
				16357)),

		PROMETHIUM_GAUNTLETS(91, 91.5, new Item[] { new Item(17668, 1) },
				new Item(16291)),

		PROMETHIUM_HATCHET(92, 92, new Item[] { new Item(17668, 1) }, new Item(
				16379)),

		PROMETHIUM_PICKAXE(92, 92, new Item[] { new Item(17668, 1) }, new Item(
				16313)),

		PROMETHIUM_KNIFE(94, 92.5, new Item[] { new Item(17668, 1) }, new Item(
				27972, 10)),

		OFFHAND_PROMETHIUM_KNIFE(94, 92.5, new Item[] { new Item(17668, 1) },
				new Item(27722, 10)),

		PROMETHIUM_WARHAMMER(93, 185, new Item[] { new Item(17668, 2) },
				new Item(17037)),

		OFFHAND_PROMETHIUM_WARHAMMER(93, 185,
				new Item[] { new Item(17668, 2) }, new Item(27683)),

		PROMETHIUM_RAPIER(93, 185, new Item[] { new Item(17668, 2) }, new Item(
				16953)),

		OFFHAND_PROMETHIUM_RAPIER(93, 185, new Item[] { new Item(17668, 2) },
				new Item(27661)),

		PROMETHIUM_LONGSWORD(94, 186, new Item[] { new Item(17668, 2) },
				new Item(16401)),

		OFF_HAND_PROMETHIUM_LONGSWORD(84, 186,
				new Item[] { new Item(17668, 2) }, new Item(27911)),

		PROMETHIUM_FULL_HELM(95, 187, new Item[] { new Item(17668, 2) },
				new Item(16709)),

		PROMETHIUM_BATTLEAXE(95, 187, new Item[] { new Item(17668, 2) },
				new Item(15771)),

		PROMETHIUM_KATAGON_BATTLEAXE(95, 185,
				new Item[] { new Item(17668, 2) }, new Item(27933)),

		PROMETHIUM_KITESHIELD(96, 282, new Item[] { new Item(17668, 3) },
				new Item(17359)),

		PROMETHIUM_CHAINBODY(96, 282, new Item[] { new Item(17668, 3) },
				new Item(16731)),

		PROMETHIUM_PLATELEGS(97, 283.5, new Item[] { new Item(17668, 3) },
				new Item(16687)),

		PROMETHIUM_PLATESKIRT(97, 283.5, new Item[] { new Item(17668, 3) },
				new Item(16665)),

		PROMETHIUM_SPEAR(97, 378, new Item[] { new Item(17668, 4) }, new Item(
				17135)),

		PROMETHIUM_MAUL(98, 380, new Item[] { new Item(17668, 4) }, new Item(
				16423)),

		PROMETHIUM_2H(98, 380, new Item[] { new Item(17668, 4) },
				new Item(16907)),

		PROMETHIUM_PLATEBODY(99, 477.5, new Item[] { new Item(17668, 5) },
				new Item(17257)),
		
		/**
		 * Malevolent
		 */
		
		MALEVOLENT_HELM(91, 500, new Item[] { new Item(30027, 14), new Item(30028, 1) },
				new Item(30005)),
		MALEVOLENT_CUIRASS(93, 1500, new Item[] { new Item(30027, 42), new Item (30028, 3) },
				new Item(30008)),
		MALEVOLENT_GREAVES(92, 1000, new Item[] { new Item(30027, 28) , new Item (30028, 2)},
				new Item(30011));


		public static ForgingBar getBarByProduce(int id) {
			for (ForgingBar bar : ForgingBar.values()) {
				if (bar.getProducedItem().getId() == id)
					return bar;
			}
			return null;
		}

		public static ForgingBar getBar(int id) {
			for (ForgingBar bar : ForgingBar.values()) {
				for (Item item : bar.getItemsRequired())
					if (item.getId() == id)
						return bar;
			}
			return null;
		}

		public static ForgingBar getBar(Player player) {
			for (ForgingBar bar : ForgingBar.values()) {
				for (Item item : bar.getItemsRequired())
					if (!player.getInventory().containsItems(
							new Item(item.getId())))
						return bar;
			}
			return null;
		}

		private int levelRequired;
		private double experience;
		private Item[] barsRequired;
		private Item producedItem;

		ForgingBar(int levelRequired, double experience,
				Item[] itemsRequired, Item producedItem) {
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.barsRequired = itemsRequired;
			this.producedItem = producedItem;
		}

		public Item[] getItemsRequired() {
			return barsRequired;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public Item getProducedItem() {
			return producedItem;
		}

		public double getExperience() {
			return experience;
		}

	}

	public ForgingBar bar;
	public WorldObject object;
	public int ticks;
	private int xpMultiplier = 1;

	public Smithing(ForgingBar bar, WorldObject object, int ticks) {
		this.object = object;
		this.bar = bar;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (bar == null || player == null || object == null) {
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					bar.getItemsRequired()[0].getId(),
					bar.getItemsRequired()[0].getAmount())) {
				player.getPackets().sendGameMessage(
						"You need "
								+ bar.getItemsRequired()[0].getAmount()
								+ " "
								+ bar.getItemsRequired()[0].getDefinitions()
										.getName()
								+ "'s to create a "
								+ bar.getProducedItem().getDefinitions()
										.getName() + ".");
				player.getInterfaceManager().removeInterface(1251);
				return false;
			}
		}
		if (!player.getSkills().hasLevel(Skills.SMITHING, bar.getLevelRequired())){
			player.getInterfaceManager().removeInterface(1251);
			return false;
		}	
		player.getPackets().sendGameMessage(
				"You place the required bars and attempt to create a "
						+ bar.getProducedItem().getDefinitions().getName()
						+ ".", true);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (bar == null || player == null || object == null) {
			return false;
		}
		if (bar.getItemsRequired().length > 1) {
			if (!player.getInventory().containsItemToolBelt(
					bar.getItemsRequired()[0].getId(),
					bar.getItemsRequired()[0].getAmount())) {
				player.getPackets().sendGameMessage(
						"You need "
								+ bar.getItemsRequired()[0].getAmount()
								+ " "
								+ bar.getItemsRequired()[0].getDefinitions()
										.getName()
								+ "'s to create a "
								+ bar.getProducedItem().getDefinitions()
										.getName() + ".");
				return false;
			}
		}
		for (Item required : bar.getItemsRequired())
			if (!player.getInventory().containsItem(required.getId(), required.getAmount()))
				return false;
		if (!player.getSkills().hasLevel(Skills.SMITHING, bar.getLevelRequired()))
			return false;
		player.faceObject(object);
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		player.setNextAnimation(new Animation(22143));
		int maxQuantity = player.getVarsManager().getBitValue(1002);
		double xp = bar.getExperience();
		player.getSkills().addXp(Skills.SMITHING, xp);
		for (Item required : bar.getItemsRequired()) {
			if (required.getId() == 4 || required.getId() == 2976
					|| required.getId() == 1594 || required.getId() == 1599
					|| required.getId() == 5523)
				continue;
			player.getInventory().deleteItem(required.getId(), required.getAmount());
		}
		if (maxQuantity > 1){
		SkillsDialogue.sendProgressBar(player, bar.getProducedItem().getId(), maxQuantity, (int) xp);
		player.getPackets().sendCSVarInteger(2229, maxQuantity - xpMultiplier);
		xpMultiplier++;
		}
		int amount = bar.getProducedItem().getAmount();
		player.getInventory().addItem(bar.getProducedItem().getId(), amount);
		if (player.getClanManager() != null && player.getClanManager().getClan() != null)
			player.getClanManager().getClan().increaseGatheredResources(amount);
		player.getGamePointManager().increaseGamePoints((int) (bar.getExperience() / 2));
		player.getPackets().sendGameMessage(
				"You have successfully created a "
						+ bar.getProducedItem().getDefinitions().getName()
						+ ".", true);
		if (player.getDailyTask() != null)
			player.getDailyTask().incrementTask(player, 3,
					bar.getProducedItem().getId(), Skills.SMITHING);
		if (ticks > 0) {
			return 3;
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}
}