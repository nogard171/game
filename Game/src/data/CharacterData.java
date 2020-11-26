package data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import classes.Skill;

public class CharacterData {
	public static Point index = new Point(0, 0);

	public static String name = "Nogard171";
	public static int health = 100;
	public static int maxHealth = 100;
	public static int stamina = 100;
	public static int maxStamina = 100;
	public static int mana = 100;
	public static int maxMana = 100;
	public static long xp = 0;
	public static long nextXp = 80;
	public static int level = 1;

	public static int damage = 10;
	public static int defense = 10;
	public static int agility = 10;
	public static int intellect = 10;
	public static int regen = 10;
	public static int vitality = 10;

	public static int gold = 0;
	public static int silver = 0;
	public static int copper = 0;

	public static ArrayList<String> obtainedSkills = new ArrayList<String>();
	public static HashMap<String, Skill> skills = new HashMap<String, Skill>();
	public static ArrayList<String> obtainedRecipes = new ArrayList<String>();

	public static void addAttribute(String name, int amount) {
		switch (name) {
		case "damage":
			damage += amount;
			break;
		case "defense":
			defense += amount;
			break;
		case "agility":
			agility += amount;
			break;
		case "intellect":
			intellect += amount;
			break;
		case "regen":
			regen += amount;
			break;
		case "vitality":
			vitality += amount;
			break;
		case "gold":
			gold += amount;
			break;
		case "silver":
			silver += amount;
			break;
		case "copper":
			copper += amount;
			break;
		}
	}
}