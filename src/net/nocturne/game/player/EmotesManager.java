package net.nocturne.game.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import net.nocturne.cache.loaders.NPCDefinitions;
import net.nocturne.executor.GameExecutorManager;
import net.nocturne.game.*;
import net.nocturne.game.npc.NPC;
import net.nocturne.game.tasks.WorldTask;
import net.nocturne.game.tasks.WorldTasksManager;
import net.nocturne.network.decoders.WorldPacketsDecoder;
import net.nocturne.utils.Utils;

public final class EmotesManager implements Serializable {

	private static final long serialVersionUID = 8489480378717534336L;

	private ArrayList<Integer> unlockedEmotes;
	private transient Player player;
	private transient long nextEmoteEnd;

	public EmotesManager() {
		unlockedEmotes = new ArrayList<Integer>();
		for (int emoteId = 2; emoteId < 48; emoteId++)
			unlockedEmotes.add(emoteId);
		unlockedEmotes.add(39); // skillcape
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void unlockEmote(int id) {
		if (unlockedEmotes.contains(id))
			return;
		if (unlockedEmotes.add(id))
			refreshListConfigs();
	}

	public void useBookEmote(int slotId, int packetId) {
		if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"You can't perform while you're under combat.");
			return;
		} else if (isDoingEmote()) {
			player.getPackets().sendGameMessage(
					"You're already doing an emote!");
			return;
		}
		player.stopAll();
		player.getTreasureTrailsManager().useEmote(slotId + 2);
		switch (slotId) {
		case 0:// Yes
			player.setNextAnimation(new Animation(21979));
			break;
		case 1:// No
			player.setNextAnimation(new Animation(21980));
			break;
		case 2:// Bow
			player.setNextAnimation(new Animation(21982));
			break;
		case 3:// Angry
			player.setNextAnimation(new Animation(21984));// old one 859
			break;
		case 4:// Think
			player.setNextAnimation(new Animation(21983));
			break;
		case 5:// Wave
			player.setNextAnimation(new Animation(21985));
			break;
		case 6:// Shrug
			player.setNextAnimation(new Animation(21986));
			break;
		case 7:// Cheer
			player.setNextAnimation(new Animation(21987));
			break;
		case 8:// Beckon
			player.setNextAnimation(new Animation(21988));// old 864
			break;
		case 9:// Laugh
			player.setNextAnimation(new Animation(21989));
			break;
		case 10:// Jump For Joy
			player.setNextAnimation(new Animation(21990));
			break;
		case 11:// Yawn
			player.setNextAnimation(new Animation(2111));
			break;
		case 12:// Dance
			player.setNextAnimation(new Animation(866));
			break;
		case 13:// Jig
			player.setNextAnimation(new Animation(2106));
			break;
		case 14:// Twirl
			player.setNextAnimation(new Animation(2107));
			break;
		case 15:// Headbang
			player.setNextAnimation(new Animation(2108));
			break;
		case 16:// Cry
			player.setNextAnimation(new Animation(21992));
			break;
		case 17:// Blow Kiss
			player.setNextAnimation(new Animation(21995));
			player.setNextGraphics(new Graphics(4418));
			break;
		case 18:// Panic
			player.setNextAnimation(new Animation(21995));
			break;
		case 19:// RaspBerry
			player.setNextAnimation(new Animation(22000));
			break;
		case 20:// Clap
			player.setNextAnimation(new Animation(21997));
			break;
		case 21:// Salute
			player.setNextAnimation(new Animation(21993));
			break;
		case 22:// Goblin Bow
			player.setNextAnimation(new Animation(2127));
			break;
		case 23:// Goblin Salute
			player.setNextAnimation(new Animation(2128));
			break;
		case 24:// Glass Box
			player.setNextAnimation(new Animation(1131));
			break;
		case 25:// Climb Rope
			player.setNextAnimation(new Animation(1130));
			break;
		case 26:// Lean
			player.setNextAnimation(new Animation(1129));
			break;
		case 27:// Glass Wall
			player.setNextAnimation(new Animation(1128));
			break;
		case 28:// Idea
			player.setNextAnimation(new Animation(4276));
			player.setNextGraphics(new Graphics(712));
			break;
		case 29:// Stomp
			player.setNextAnimation(new Animation(4278));
			player.setNextGraphics(new Graphics(713));
			break;
		case 30:// Flap
			player.setNextAnimation(new Animation(4280));
			break;
		case 31:// Slap Head
			player.setNextAnimation(new Animation(4275));
			break;
		case 32:// Zombie Walk
			player.setNextAnimation(new Animation(3544));
			break;
		case 33:// Zombie Dance
			player.setNextAnimation(new Animation(3543));
			break;
		case 34:// Zombie Hand
			player.setNextAnimation(new Animation(7272));
			player.setNextGraphics(new Graphics(1244));
			break;
		case 35:// Scared
			player.setNextAnimation(new Animation(2836));
			break;
		case 36:// Bunny-Hop
			player.setNextAnimation(new Animation(6111));
			break;
		case 37:// Skill Capes
			final int capeId = player.getEquipment().getCapeId();
			switch (capeId) {
			case 9747:
			case 9748:
			case 10639: // Attack cape
				player.setNextAnimation(new Animation(4959));
				player.setNextGraphics(new Graphics(823));
				break;
			case 9753:
			case 9754:
			case 10641: // Defence cape
				player.setNextAnimation(new Animation(4961));
				player.setNextGraphics(new Graphics(824));
				break;
			case 9750:
			case 9751:
			case 10640: // Strength cape
				player.setNextAnimation(new Animation(4981));
				player.setNextGraphics(new Graphics(828));
				break;
			case 9768:
			case 9769:
			case 10647: // Hitpoints cape
				player.setNextAnimation(new Animation(14242));
				player.setNextGraphics(new Graphics(2745));
				break;
			case 9756:
			case 9757:
			case 10642: // Ranged cape
				player.setNextAnimation(new Animation(4973));
				player.setNextGraphics(new Graphics(832));
				break;
			case 9762:
			case 9763:
			case 10644: // Magic cape
				player.setNextAnimation(new Animation(4939));
				player.setNextGraphics(new Graphics(813));
				break;
			case 9759:
			case 9760:
			case 10643: // Prayer cape
				player.setNextAnimation(new Animation(4979));
				player.setNextGraphics(new Graphics(829));
				break;
			case 9801:
			case 9802:
			case 10658: // Cooking cape
				player.setNextAnimation(new Animation(4955));
				player.setNextGraphics(new Graphics(821));
				break;
			case 9807:
			case 9808:
			case 10660: // Woodcutting cape
				player.setNextAnimation(new Animation(4957));
				player.setNextGraphics(new Graphics(822));
				break;
			case 9783:
			case 9784:
			case 10652: // Fletching cape
				player.setNextAnimation(new Animation(4937));
				player.setNextGraphics(new Graphics(812));
				break;
			case 9798:
			case 9799:
			case 10657: // Fishing cape
				player.setNextAnimation(new Animation(4951));
				player.setNextGraphics(new Graphics(819));
				break;
			case 9804:
			case 9805:
			case 10659: // Firemaking cape
				player.setNextAnimation(new Animation(4975));
				player.setNextGraphics(new Graphics(831));
				break;
			case 9780:
			case 9781:
			case 10651: // Crafting cape
				player.setNextAnimation(new Animation(4949));
				player.setNextGraphics(new Graphics(818));
				break;
			case 9795:
			case 9796:
			case 10656: // Smithing cape
				player.setNextAnimation(new Animation(4943));
				player.setNextGraphics(new Graphics(815));
				break;
			case 9792:
			case 9793:
			case 10655: // Mining cape
				player.setNextAnimation(new Animation(4941));
				player.setNextGraphics(new Graphics(814));
				break;
			case 9774:
			case 9775:
			case 10649: // Herblore cape
				player.setNextAnimation(new Animation(4969));
				player.setNextGraphics(new Graphics(835));
				break;
			case 9771:
			case 9772:
			case 10648: // Agility cape
				player.setNextAnimation(new Animation(4977));
				player.setNextGraphics(new Graphics(830));
				break;
			case 9777:
			case 9778:
			case 10650: // Thieving cape
				player.setNextAnimation(new Animation(4965));
				player.setNextGraphics(new Graphics(826));
				break;
			case 9786:
			case 9787:
			case 10653: // Slayer cape
				player.setNextAnimation(new Animation(4967));
				player.setNextGraphics(new Graphics(1656));
				break;
			case 9810:
			case 9811:
			case 10611: // Farming cape
				player.setNextAnimation(new Animation(4963));
				player.setNextGraphics(new Graphics(825));
				break;
			case 9765:
			case 9766:
			case 10645: // Runecrafting cape
				player.setNextAnimation(new Animation(4947));
				player.setNextGraphics(new Graphics(817));
				break;
			case 9789:
			case 9790:
			case 10654: // Construction cape
				player.setNextAnimation(new Animation(4953));
				player.setNextGraphics(new Graphics(820));
				break;
			case 12169:
			case 12170:
			case 12524: // Summoning cape
				player.setNextAnimation(new Animation(8525));
				player.setNextGraphics(new Graphics(1515));
				break;
			case 9948:
			case 9949:
			case 10646: // Hunter cape
				player.setNextAnimation(new Animation(5158));
				player.setNextGraphics(new Graphics(907));
				break;
			case 9813:
			case 10662: // Quest cape
				player.setNextAnimation(new Animation(4945));
				player.setNextGraphics(new Graphics(816));
				break;
			case 18508:
			case 18509: // Dungeoneering cape
				final int rand = (int) (Math.random() * (2 + 1));
				player.setNextAnimation(new Animation(13190));
				player.setNextGraphics(new Graphics(2442));
				WorldTasksManager.schedule(new WorldTask() {
					int step;

					@Override
					public void run() {
						if (step == 1) {
							player.getAppearence().transformIntoNPC(
									(rand == 0 ? 11227 : (rand == 1 ? 11228
											: 11229)));
							player.setNextAnimation(new Animation(
									((rand > 0 ? 13192 : (rand == 1 ? 13193
											: 13194)))));
						}
						if (step == 3) {
							player.getAppearence().transformIntoNPC(-1);
							stop();
						}
						step++;
					}
				}, 0, 1);
				break;
			case 29185: // Divination cape
			case 29186:
			case 31284:
				player.setNextAnimation(new Animation(21241));
				player.setNextGraphics(new Graphics(4254));
				break;
			case 36356: // Invention cape
				player.setNextAnimation(new Animation(27988));
				player.setNextGraphics(new Graphics(6001));
				break;
			case 32052: // Expert capes
			case 32053:
			case 32054:
			case 32055:
				player.getDialogueManager().startDialogue("SimpleMessage",
						"Not letting you do this, bitch - miles M");
				break;
			case 19709:
			case 19710:
				nextEmoteEnd = Utils.currentTimeMillis() + 6000;
				WorldTasksManager.schedule(new WorldTask() {
					int step;

					@Override
					public void run() {
						if (step == 0) {
							player.getAppearence().transformIntoNPC(11229);
							World.sendGraphics(
									player,
									new Graphics(2777),
									new WorldTile(player.getX() - 1, player
											.getY() - 1, player.getPlane()));
							player.setNextFaceWorldTile(new WorldTile(player
									.getX() - 1, player.getY() - 1, player
									.getPlane()));
							player.setNextAnimation(new Animation(14608));
							World.sendGraphics(player, new Graphics(2781),
									new WorldTile(player.getX(), player.getY(),
											player.getPlane()));
						} else if (step == 1) {
							player.getAppearence().transformIntoNPC(11228);
							World.sendGraphics(
									player,
									new Graphics(2778),
									new WorldTile(player.getX() + 1, player
											.getY() - 1, player.getPlane()));
							player.setNextFaceWorldTile(new WorldTile(player
									.getX(), player.getY() - 1, player
									.getPlane()));
							player.setNextAnimation(new Animation(14609));
							player.setNextGraphics(new Graphics(2782));
						} else if (step == 2) {
							player.getAppearence().transformIntoNPC(11227);
							World.sendGraphics(
									player,
									new Graphics(2779),
									new WorldTile(player.getX(),
											player.getY() - 1, player
													.getPlane()));
							World.sendGraphics(
									player,
									new Graphics(2780),
									new WorldTile(player.getX(),
											player.getY() + 1, player
													.getPlane()));
							player.setNextAnimation(new Animation(14610));
							GameExecutorManager.slowExecutor.schedule(
									new Runnable() {
										@Override
										public void run() {
											player.setNextGraphics(new Graphics(
													2783));
										}
									}, 1500, TimeUnit.MILLISECONDS);
						} else if (step == 4) {
							player.getAppearence().transformIntoNPC(-1);
							stop();
						}
						step++;
					}
				}, 0, 1);
				break;
			case 24709: // Veteran cape 10years
				if (!World.isTileFree(player.getPlane(), player.getX(),
						player.getY(), 3)) {
					player.getPackets()
							.sendGameMessage(
									"You need clear space outside in order to perform this emote.");
					break;
				} else if (player.getControllerManager().getController() != null) {
					player.getPackets().sendGameMessage(
							"You can't do this here.");
					break;
				}
				player.setNextAnimation(new Animation(17118));
				player.setNextGraphics(new Graphics(3227));
				break;
			case 20763: // Veteran cape
				if (!World.isTileFree(player.getPlane(), player.getX(),
						player.getY(), 3)) {
					player.getPackets()
							.sendGameMessage(
									"You need clear space outside in order to perform this emote.");
					break;
				} else if (player.getControllerManager().getController() != null) {
					player.getPackets().sendGameMessage(
							"You can't do this here.");
					break;
				}
				player.setNextAnimation(new Animation(352));
				player.setNextGraphics(new Graphics(1446));
				break;
			case 20765: // Classic cape
				if (player.getControllerManager().getController() != null) {
					player.getPackets().sendGameMessage(
							"You cannot do this here!");
					break;
				}
				int random = Utils.random(2);
				player.setNextAnimation(new Animation(122));
				player.setNextGraphics(new Graphics(random == 0 ? 1471 : 1466));
				break;
			case 20767: // Max cape
				if (player.getControllerManager().getController() != null) {
					player.getPackets().sendGameMessage(
							"You can't do this here.");
					break;
				}
				int size = NPCDefinitions.getNPCDefinitions(1224).size;
				WorldTile spawnTile = new WorldTile(new WorldTile(
						player.getX() + 1, player.getY(), player.getPlane()));
				if (!World.isTileFree(spawnTile.getPlane(), spawnTile.getX(),
						spawnTile.getY(), size)) {
					spawnTile = null;
					int[][] dirs = Utils.getCoordOffsetsNear(size);
					for (int dir = 0; dir < dirs[0].length; dir++) {
						final WorldTile tile = new WorldTile(new WorldTile(
								player.getX() + dirs[0][dir], player.getY()
										+ dirs[1][dir], player.getPlane()));
						if (World.isTileFree(tile.getPlane(), tile.getX(),
								tile.getY(), size)) {
							spawnTile = tile;
							break;
						}
					}
				}
				if (spawnTile == null) {
					player.getPackets()
							.sendGameMessage(
									"You need clear space outside in order to perform this emote.");
					break;
				}
				nextEmoteEnd = Utils.currentTimeMillis() + (25 * 600);
				final WorldTile npcTile = spawnTile;
				WorldTasksManager.schedule(new WorldTask() {
					private int step;
					private NPC npc;

					@Override
					public void run() { // TODO fix delays
						if (step == 0) {
							npc = new NPC(1224, npcTile, -1, true);
							npc.setNextAnimation(new Animation(1434));
							npc.setNextGraphics(new Graphics(1482));
							player.setNextAnimation(new Animation(1179));
							npc.setNextFaceEntity(player);
							player.setNextFaceEntity(npc);
						} else if (step == 2) {
							npc.setNextAnimation(new Animation(1436));
							npc.setNextGraphics(new Graphics(1486));
							player.setNextAnimation(new Animation(1180));
						} else if (step == 3) {
							npc.setNextGraphics(new Graphics(1498));
							player.setNextAnimation(new Animation(1181));
						} else if (step == 4) {
							player.setNextAnimation(new Animation(1182));
						} else if (step == 5) {
							npc.setNextAnimation(new Animation(1448));
							player.setNextAnimation(new Animation(1250));
						} else if (step == 6) {
							player.setNextAnimation(new Animation(1251));
							player.setNextGraphics(new Graphics(1499));
							npc.setNextAnimation(new Animation(1454));
							npc.setNextGraphics(new Graphics(1504));
						} else if (step == 11) {
							player.setNextAnimation(new Animation(1291));
							player.setNextGraphics(new Graphics(1686));
							player.setNextGraphics(new Graphics(1598));
							npc.setNextAnimation(new Animation(1440));
						} else if (step == 16) {
							player.setNextFaceEntity(null);
							npc.finish();
							stop();
						}
						step++;
					}

				}, 0, 1);
				break;
			case 20769:
			case 20771: // Completionist cape
			case 32152:
			case 32153:
				if (!World.isTileFree(player.getPlane(), player.getX(),
						player.getY(), 3)) {
					player.getPackets()
							.sendGameMessage(
									"You need clear space outside in order to perform this emote.");
					break;
				} else if (player.getControllerManager().getController() != null) {
					player.getPackets().sendGameMessage(
							"You can't do this here.");
					break;
				}
				nextEmoteEnd = Utils.currentTimeMillis() + (20 * 600);
				WorldTasksManager.schedule(new WorldTask() {
					private int step;

					@Override
					public void run() {
						if (step == 0) {
							player.setNextAnimation(new Animation(356));
							player.setNextGraphics(new Graphics(307));
						} else if (step == 2) {
							player.getAppearence().transformIntoNPC(
									capeId == 20769 ? 1830 : 3372);
							player.setNextAnimation(new Animation(1174));
							player.setNextGraphics(new Graphics(1443));
						} else if (step == 4) {
							player.getPackets().sendCameraShake(3, 25, 50, 25,
									50);
						} else if (step == 5) {
							player.getPackets().sendStopCameraShake();
						} else if (step == 8) {
							player.getAppearence().transformIntoNPC(-1);
							player.setNextAnimation(new Animation(1175));
							stop();
						}
						step++;
					}

				}, 0, 1);
				break;
			default:
				player.getPackets()
						.sendGameMessage(
								"You need to be wearing a skillcape in order to perform this emote.");
				break;
			}
			break;
		case 38:// Snow-man Dance
			player.setNextAnimation(new Animation(7531));
			break;
		case 39:// Air Guitar
			player.setNextAnimation(new Animation(21998));
			player.setNextGraphics(new Graphics(4417));
			break;
		case 40:// Safety First
			player.setNextAnimation(new Animation(8770));
			player.setNextGraphics(new Graphics(1553));
			break;
		case 41:// Explore
			player.setNextAnimation(new Animation(9990));
			player.setNextGraphics(new Graphics(1734));
			break;
		case 42:// Trick
			player.setNextAnimation(new Animation(10530));
			player.setNextGraphics(new Graphics(1864));
			break;
		case 43:// Freeze
			player.setNextAnimation(new Animation(11044));
			player.setNextGraphics(new Graphics(1973));
			break;
		case 44:// Give Thanks
			// TODO give thanks
			break;
		case 45:// Eggy Days
			player.setNextAnimation(new Animation(11542));
			player.setNextGraphics(new Graphics(2037));
			break;
		case 46:// Dramatic Point
			player.setNextAnimation(new Animation(12658));
			player.setNextGraphics(new Graphics(780));
			break;
		case 47:// Faint
			player.setNextAnimation(new Animation(14165));
			break;
		case 48:// Puppet Master
			player.setNextAnimation(new Animation(14869));
			player.setNextGraphics(new Graphics(2837));
			break;
		case 49:// Task Master
			player.setNextAnimation(new Animation(!player.getAppearence()
					.isMale() ? 15034 : 15033));
			player.setNextGraphics(new Graphics(2930));
			break;
		case 50:// TODO Add tick processing
			player.setNextAnimation(new Animation(15104));
			player.setNextAnimation(new Animation(15106));
			player.setNextAnimation(new Animation(15108));
			player.setNextAnimation(new Animation(15105));
			break;
		case 51:// Cat fight
			player.setNextAnimation(new Animation(2252));
			break;
		case 52:// talk to the hand
			player.setNextAnimation(new Animation(2416));
			break;
		case 53:// Shake Hands
			player.setNextAnimation(new Animation(2303));
			break;
		case 54:// High Five
			player.setNextAnimation(new Animation(2312));
			break;
		case 55:// Face-palm
			player.setNextAnimation(new Animation(2254));
			break;
		case 56:// Surrender
			player.setNextAnimation(new Animation(2360));
			break;
		case 57:// Levitate
			player.setNextAnimation(new Animation(2327));
			break;
		case 58:// Muscle-man Pose
			player.setNextAnimation(new Animation(2566));
			break;
		case 59:// Rofl
			player.setNextAnimation(new Animation(2359));
			break;
		case 60:// Breathe Fire
			player.setNextAnimation(new Animation(2238));
			player.setNextGraphics(new Graphics(358));
			break;
		case 61:// Storm
			player.setNextAnimation(new Animation(2563));
			player.setNextGraphics(new Graphics(365));
			break;
		case 62:// Snow
			player.setNextAnimation(new Animation(15357));
			player.setNextGraphics(new Graphics(1391));
			break;
		case 63:// Invoke Spring
			player.setNextAnimation(new Animation(15357));
			player.setNextGraphics(new Graphics(1391));
			break;
		case 64:// Head in sand
			player.setNextAnimation(new Animation(12926));
			player.setNextGraphics(new Graphics(1761));
			break;
		case 65:// Hula-hoop
			player.setNextAnimation(new Animation(12928));
			break;
		case 66:// Disappear
			player.setNextAnimation(new Animation(12929));
			player.setNextGraphics(new Graphics(1760));
			break;
		case 67:// Ghost
			player.setNextAnimation(new Animation(12932));
			player.setNextGraphics(new Graphics(1762));
			break;
		case 68:// Bring it!
			player.setNextAnimation(new Animation(12934));
			break;
		case 69:// Palm-fist
			player.setNextAnimation(new Animation(12931));
			break;
		case 93:// Living on Borrowed Time
			player.setNextAnimation(new Animation(13965));
			player.setNextGraphics(new Graphics(1766));
			player.setNextGraphics(new Graphics(4056));
			break;
		case 94:// Troubadour dance
			player.setNextAnimation(new Animation(15424));
			break;
		case 95:// Evil Laugh
			player.setNextAnimation(new Animation(!player.getAppearence()
					.isMale() ? 15536 : 15535));
			break;
		case 96:// Golf Clap
			player.setNextAnimation(new Animation(15520));
			break;
		case 97:// LOLcano
			player.setNextAnimation(new Animation(!player.getAppearence()
					.isMale() ? 15533 : 15532));
			player.setNextGraphics(new Graphics(2191));
			break;
		case 98:// Infernal Power
			player.setNextAnimation(new Animation(15529));
			player.setNextGraphics(new Graphics(2197));
			break;
		case 99:// Divine Power
			player.setNextAnimation(new Animation(15524));
			player.setNextGraphics(new Graphics(2195));
			break;
		case 100:// Your Dead
			player.setNextAnimation(new Animation(14195));
			break;
		case 101:// Scream
			player.setNextAnimation(new Animation(!player.getAppearence()
					.isMale() ? 15527 : 15526));
			break;
		case 102:// Tornado
			player.setNextAnimation(new Animation(15530));
			player.setNextGraphics(new Graphics(2196));
			break;
		case 103:// Chaotic Cookery
			player.setNextAnimation(new Animation(15604));
			player.setNextGraphics(new Graphics(2239));
			break;
		case 104:// ROFLcopter
			player.setNextAnimation(new Animation(!player.getAppearence()
					.isMale() ? 16374 : 16373));
			player.setNextGraphics(new Graphics(3010));
			break;
		case 105:// Nature Might
			player.setNextAnimation(new Animation(16376));
			player.setNextGraphics(new Graphics(3011));
			break;
		case 106:// Inner Power
			player.setNextAnimation(new Animation(16382));
			player.setNextGraphics(new Graphics(3014));
			break;
		case 107:// Werewolf Transformation
			player.setNextAnimation(new Animation(16380));
			player.setNextGraphics(new Graphics(3013));
			player.setNextGraphics(new Graphics(3016));
			break;
		case 108:// Celebrate
			player.setNextAnimation(new Animation(16913));
			player.setNextGraphics(new Graphics(3175));
			break;
		case 109:// break; Dance
			player.setNextAnimation(new Animation(17079));
			break;
		case 110:// Mag Transformation
			player.setNextAnimation(new Animation(17103));
			player.setNextGraphics(new Graphics(3222));
			break;
		case 111:// break;wind
			player.setNextAnimation(new Animation(17076));
			player.setNextGraphics(new Graphics(3226));
			break;
		case 112:// backflip
			player.setNextAnimation(new Animation(17101));
			player.setNextGraphics(new Graphics(3221));
			break;
		case 113:// gravedigger
			player.setNextAnimation(new Animation(17077));
			player.setNextGraphics(new Graphics(3219));
			break;
		case 114:// frog transform
			player.setNextAnimation(new Animation(17080));
			player.setNextGraphics(new Graphics(3220));
			break;
		case 115:// mexican wave
			player.setNextAnimation(new Animation(17163));
			break;
		case 116:// sports man
			player.setNextAnimation(new Animation(17166));
			break;
		case 118:// kick sand
			player.setNextAnimation(new Animation(17186));
			player.setNextGraphics(new Graphics(3252));
			break;
		case 119:// crab transform
			player.setNextAnimation(new Animation(!player.getAppearence()
					.isMale() ? 17213 : 17212));
			player.setNextGraphics(new Graphics(3257));
			break;
		case 120:// truster stomp
			player.setNextAnimation(new Animation(17801));
			player.setNextGraphics(new Graphics(3446));
			break;
		case 121:// robot dance
			player.setNextAnimation(new Animation(17799));
			player.setNextGraphics(new Graphics(3445));
			break;
		case 126:// Butterfly Dervish
			player.setNextAnimation(new Animation(20009));
			player.setNextGraphics(new Graphics(3916));
			player.setNextGraphics(new Graphics(3917));
			break;
		case 169:// Runescape through the ages
			player.setNextAnimation(new Animation(23248));
			player.setNextGraphics(new Graphics(4745));
			break;
		case 171: // Cute Bunny
			player.setNextAnimation(new Animation(23288));
			player.setNextGraphics(new Graphics(4779));
			break;
		case 172: // Sneaky Bunny
			player.setNextAnimation(new Animation(23290));
			player.setNextGraphics(new Graphics(4780));
			player.setNextGraphics(new Graphics(4781));
			break;
		case 173:// Demonic Rock Off
			player.setNextAnimation(new Animation(23857));
			player.setNextGraphics(new Graphics(4945));
			break;
		case 174:// Shadow to Praetor
			player.setNextAnimation(new Animation(24492));
			player.setNextGraphics(new Graphics(5110));
			break;
		case 175:// Praetor to Shadow
			player.setNextAnimation(new Animation(24492));
			player.setNextGraphics(new Graphics(5109));
			break;
		case 177:// Proto Pack
			player.setNextAnimation(new Animation(24712));
			player.setNextGraphics(new Graphics(5185));
			player.setNextGraphics(new Graphics(5186));
			break;
		case 179:// Pulled Away
			player.setNextAnimation(new Animation(24853));
			player.setNextGraphics(new Graphics(5227));
			player.setNextGraphics(new Graphics(5228));
			break;
		case 180:// Hefin Lotus
			player.setNextAnimation(new Animation(25009));
			break;
		case 181:// Hefin Bow
			player.setNextAnimation(new Animation(25008));
			break;
		case 182:// Hefin Ward
			player.setNextAnimation(new Animation(25010));
			break;
		case 183:// Hefin Crane
			player.setNextAnimation(new Animation(25006));
			break;
		case 184:// Cracker Pull
			player.setNextAnimation(new Animation(25325));
			player.setNextGraphics(new Graphics(5293));
			break;
		default:
			player.getPackets().sendGameMessage(
					"Animation at slot " + slotId + " has not been added yet.");
			break;
		}
		if (!isDoingEmote())
			setNextEmoteEnd();
	}

	public void setNextEmoteEnd() {
		nextEmoteEnd = player.getLastAnimationEnd() - 600;
	}

	public void setNextEmoteEnd(long delay) {
		nextEmoteEnd = Utils.currentTimeMillis() + delay;
	}

	void init() {
		refreshListConfigs();
	}

	private void refreshListConfigs() {
		if (unlockedEmotes.contains(24) && unlockedEmotes.contains(25))
			player.getVarsManager().sendVar(465, 7); // goblin quest emotes
		int value1 = 0;
		if (unlockedEmotes.contains(32))
			value1 += 1;
		if (unlockedEmotes.contains(30))
			value1 += 2;
		if (unlockedEmotes.contains(33))
			value1 += 4;
		if (unlockedEmotes.contains(31))
			value1 += 8;
		if (value1 > 0)
			player.getVarsManager().sendVar(802, value1); // stronghold of
		// security emotes
		if (unlockedEmotes.contains(36))
			player.getVarsManager().sendVar(1085, 249852); // hallowen hand
		// emote
		int value2 = 0;
		if (unlockedEmotes.contains(29))
			value2 += 1;
		if (unlockedEmotes.contains(26))
			value2 += 2;
		if (unlockedEmotes.contains(27))
			value2 += 4;
		if (unlockedEmotes.contains(28))
			value2 += 8;
		if (unlockedEmotes.contains(37))
			value2 += 16;
		if (unlockedEmotes.contains(35))
			value2 += 32;
		if (unlockedEmotes.contains(34))
			value2 += 64;
		if (unlockedEmotes.contains(38))
			value2 += 128;
		if (unlockedEmotes.contains(39))
			value2 += 256;
		if (unlockedEmotes.contains(40))
			value2 += 512;
		if (unlockedEmotes.contains(41))
			value2 += 1024;
		if (unlockedEmotes.contains(42))
			value2 += 2048;
		if (unlockedEmotes.contains(43))
			value2 += 4096;
		if (unlockedEmotes.contains(44))
			value2 += 8192;
		if (unlockedEmotes.contains(46))
			value2 += 16384;
		if (unlockedEmotes.contains(45))
			value2 += 32768;
		if (value2 > 0)
			player.getVarsManager().sendVar(313, value2); //
		player.getVarsManager().sendVar(313, 1);
		if (unlockedEmotes.contains(47))
			player.getVarsManager().sendVar(818, 1);
		player.getVarsManager().sendVar(465, 7);
		player.getVarsManager().sendVar(802, -1);
		player.getVarsManager().sendVar(1085, 249852);
		player.getVarsManager().sendVar(313, -1);
		player.getVarsManager().sendVar(2033, 1043648799);
		player.getVarsManager().sendVar(2032, 7341);
		player.getVarsManager().sendVar(1921, -893736236);
		player.getVarsManager().sendVar(1404, 123728213);
		player.getVarsManager().sendVar(2169, -1);
		player.getVarsManager().sendVar(2230, -1);
		player.getVarsManager().sendVar(1597, -1);
		player.getVarsManager().sendVar(1842, -1);
		player.getVarsManager().sendVar(2432, -1);
		player.getVarsManager().sendVar(1958, 534);
		player.getVarsManager().sendVar(2405, -1);
		player.getVarsManager().sendVar(2458, -1);
		for (int i = 0; i < 17; i++)
			player.getVarsManager().sendVarBit(1171 + i, 1);
		for (int i = 0; i < 31; i++)
			player.getVarsManager().sendVarBit(20214 + i, 1);
		for (int i = 0; i < 4; i++)
			player.getVarsManager().sendVarBit(25838 + i, 1);
	}

	public long getNextEmoteEnd() {
		return nextEmoteEnd;
	}

	public boolean isDoingEmote() {
		return nextEmoteEnd >= Utils.currentTimeMillis();
	}

	void unlockEmotesBook() {
		player.getPackets().sendUnlockIComponentOptionSlots(590, 8, 0, 196, 0,
				1);
		player.getPackets().sendUnlockIComponentOptionSlots(590, 13, 0, 11, 2);
		player.getPackets().sendExecuteScript(4717, 38666248, 38666247,
				38666249, 3874);
		player.getPackets().sendExecuteScript(4717, 94240781, 94240780,
				94240782, 3874);
		player.getPackets().sendExecuteScript(8862, 9, 1);
	}
}