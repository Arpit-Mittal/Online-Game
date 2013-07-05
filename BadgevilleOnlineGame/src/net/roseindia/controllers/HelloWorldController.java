package net.roseindia.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.roseindia.form.Team;
import net.roseindia.form.Teams;
import net.roseindia.form.playerUserInformation;
import net.roseindia.form.players;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.json.JSONException;
import org.json.JSONObject;

@Controller
public class HelloWorldController {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private static String extractPlayerInformation(String playerId) {
		String playerInfo = "";
		String playerDisplayName[] = null, siteUrl[] = null, userEmail[] = null, pointsAll[] = null;
		String storingPlayerJson = null;
		try {
			JSONObject json = readJsonFromUrl("http://sandbox.badgeville.com/api/berlin/17744f9bf6fd75ea8cb93f005452d27d/players/"
					+ playerId + ".json");
			storingPlayerJson = json.toString();
			playerDisplayName = storingPlayerJson.split("\"display_name\":\"");
			playerInfo += playerDisplayName[1].substring(0,
					playerDisplayName[1].indexOf("\"")) + "|";
			storingPlayerJson = json.toString();
			siteUrl = storingPlayerJson.split("\"site_url\":\"");
			playerInfo += siteUrl[1].substring(0, siteUrl[1].indexOf("\""))
					+ "|";
			storingPlayerJson = json.toString();
			userEmail = storingPlayerJson.split("\"user_email\":\"");
			playerInfo += userEmail[1].substring(0, userEmail[1].indexOf("\""))
					+ "|";
			storingPlayerJson = json.toString();
			pointsAll = storingPlayerJson.split("\"points_all\":");
			playerInfo += pointsAll[1].substring(0, pointsAll[1].indexOf(","));
		} catch (Exception e) {
			System.out.println("Exception Occured" + e);
		}
		return (playerInfo);
	}

	static ArrayList<String> teamNamesAfter = new ArrayList<String>();
	static ArrayList<String> teamIdAfter = new ArrayList<String>();
	static ArrayList<String> playerNames = new ArrayList<String>();
	static ArrayList<String> multiplePlayerIds = new ArrayList<String>();
	static ArrayList<String> multiplePlayerInformation = new ArrayList<String>();

	// Json Parsing Extracting Team Members Name using Split function
	// "display_name":" corresponding to all sites
	public static void listingTeamAllSitesIncludingIndividualMembers(
			JSONObject json) {
		String teamNames[];
		String teamId[];
		String multiplePlayer[];
		String players, playersId, playerInformation;
		String multiplePlayerId[];

		int i = 0;
		int numberTeams = 0;
		try {
			teamNames = json.toString().split("\"display_name\":\"");
			teamId = json.toString().split("user_email\":\"");
			for (i = 1; i < teamNames.length; i++) {
				teamNamesAfter.add(numberTeams,
						teamNames[i].substring(0, teamNames[i].indexOf("\"")));
				teamIdAfter.add(numberTeams,
						teamId[i].substring(0, teamId[i].indexOf("@")));
				numberTeams++;
			}
			for (i = 0; i < teamNamesAfter.size(); i++) {
				try {
					JSONObject teamMemberJson = readJsonFromUrl("http://sandbox.badgeville.com/api/berlin/17744f9bf6fd75ea8cb93f005452d27d/teams/"
							+ teamIdAfter.get(i) + "/players.json");
					multiplePlayer = teamMemberJson.toString().split(
							"\"display_name\":\"");
					multiplePlayerId = teamMemberJson.toString().split(
							"\"id\":\"");

					players = "";
					playersId = "";
					playerInformation = "";
					for (int j = 1; j < multiplePlayer.length; j++) {
						if (j != multiplePlayer.length - 1) {
							players += multiplePlayer[j].substring(0,
									multiplePlayer[j].indexOf("\"")) + ",";

						} else {
							players += multiplePlayer[j].substring(0,
									multiplePlayer[j].indexOf("\""));

						}
					}

					for (int k = 1; k < multiplePlayerId.length; k++) {
						if (k != multiplePlayerId.length - 1) {
							playersId += multiplePlayerId[k].substring(0,
									multiplePlayerId[k].indexOf("\"")) + ",";
							playerInformation += extractPlayerInformation(multiplePlayerId[k]
									.substring(0,
											multiplePlayerId[k].indexOf("\"")));
							playerInformation += ",";
						} else {
							playersId += multiplePlayerId[k].substring(0,
									multiplePlayerId[k].indexOf("\""));
							playerInformation += extractPlayerInformation(multiplePlayerId[k]
									.substring(0,
											multiplePlayerId[k].indexOf("\"")));
						}
					}
					playerNames.add(i, players);
					multiplePlayerInformation.add(i, playerInformation);
					multiplePlayerIds.add(i, playersId);
				}

				catch (Exception e) {
					System.out.println("Exception occured in Main:-" + e);
				}
			}

		} catch (Exception e) {
			System.out
					.println("Exception occured in listingTeamAllSitesIncludingIndividualMembers:-"
							+ e);
		}

		System.out
				.println("Player Names:Player Information:PlayersId:-Team Names:TeamId");

		for (i = 0; i < playerNames.size(); i++) {
			System.out.println(playerNames.get(i) + ":");
			System.out.println(multiplePlayerInformation.get(i) + ":");
			System.out.println(multiplePlayerIds.get(i) + ":");
			System.out.println(teamNamesAfter.get(i) + ":");// Team Names
			System.out.println(teamIdAfter.get(i) + ":");
		}
	}

	Teams teamForm = new Teams();

	@RequestMapping("/helloworld")
	public ModelAndView helloWord() {
		try {
			JSONObject json = readJsonFromUrl("http://sandbox.badgeville.com/api/berlin/17744f9bf6fd75ea8cb93f005452d27d/teams.json");
			listingTeamAllSitesIncludingIndividualMembers(json);
			List<Team> teamsInfo = new ArrayList<Team>();

			for (int i = 0; i < playerNames.size(); i++) {
				teamsInfo.add(new Team(teamNamesAfter.get(i), teamIdAfter
						.get(i), playerNames.get(i), multiplePlayerIds.get(i),
						multiplePlayerInformation.get(i), false, "Enter"));
			}

			teamForm.setTeamsInfo(teamsInfo);
			return new ModelAndView("helloworld", "teamForm", teamForm);
		} catch (Exception e) {
			return new ModelAndView("helloworld", "teamForm", teamForm);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("teamForm") Teams teamForm) {
		List<Team> userInfo = teamForm.getTeamsInfo();
		String playerInfo;
		String userEnteredPlayerName = null;

		
		List<playerUserInformation> playerInformation = new ArrayList<playerUserInformation>();
		players multiplePlayer = new players();

		if (null != userInfo && userInfo.size() > 0) {

			for (Team team : userInfo) {
				if (team.isPlayingFlag()) {
					playerInfo = team.getPlayersInfo();
					userEnteredPlayerName = team.getUserInputPlayerName();
					if (!playerInfo.contains(","))// only 1 player
					{
						playerInfo = playerInfo.replace('|', '-');
						String[] splits = playerInfo.split("-");
						playerInformation.add(new playerUserInformation(
								splits[1], splits[2], splits[0]));
						multiplePlayer.setPlayerInfo(playerInformation);
					} else if (playerInfo.contains(","))// Multiple players
					{
						playerInformation.add(new playerUserInformation(
								userEnteredPlayerName, playerInfo,
								userEnteredPlayerName));
						multiplePlayer.setPlayerInfo(playerInformation);
					}

				}

			}
		}
		return new ModelAndView("playerInfo", "multiplePlayer", multiplePlayer);
	}
}
