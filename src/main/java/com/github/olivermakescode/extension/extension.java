package com.github.olivermakescode.extension;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.PlaceholderResult;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class extension implements ModInitializer {
	public static BoolRuleHelper villagerTradeLock;
	public static BoolRuleHelper disableMutual;
	public static BoolRuleHelper itemCooldown;
	public static BoolRuleHelper ePearlEnable;
	public static BoolRuleHelper chorusEnable;
	public static BoolRuleHelper shieldEnable;
	public static BoolRuleHelper attackCool;
	public static BoolRuleHelper fallingBlocks;
	public static BoolRuleHelper entitiesTrampleCrops;
	public static BoolRuleHelper creeperExplosions;
	public static IntRuleHelper cropWaterRadius;
	public static BoolRuleHelper soupAxolotls;
	public static IntRuleHelper infinityArrowCount;
	public static BoolRuleHelper netherWater;

	@Override
	public void onInitialize() {
		GameruleHelper.start();

		villagerTradeLock = (BoolRuleHelper) GameruleHelper.register("villagerTradeLock",true);
		disableMutual = (BoolRuleHelper) GameruleHelper.register("disableMutualExclusiveEnchantments",false);
		itemCooldown = (BoolRuleHelper) GameruleHelper.register("itemCooldown",true);
		ePearlEnable = (BoolRuleHelper) GameruleHelper.register("enderPearlCooldown",true);
		chorusEnable = (BoolRuleHelper) GameruleHelper.register("chorusFruitCooldown", true);
		shieldEnable = (BoolRuleHelper) GameruleHelper.register("shieldCooldown", true);
		attackCool = (BoolRuleHelper) GameruleHelper.register("attackCooldown", true);
		fallingBlocks = (BoolRuleHelper) GameruleHelper.register("fallingBlocks", true);
		entitiesTrampleCrops = (BoolRuleHelper) GameruleHelper.register("entitiesTrampleCrops", true);
		cropWaterRadius = (IntRuleHelper) GameruleHelper.register("farmlandWateringRadius", 4, 1, 32);
		creeperExplosions = (BoolRuleHelper) GameruleHelper.register("creeperExplosions", true);
		soupAxolotls = (BoolRuleHelper) GameruleHelper.register("soupAxolotls", true);
		infinityArrowCount = (IntRuleHelper) GameruleHelper.register("infinityArrowCount",1,0,64);
		netherWater = (BoolRuleHelper) GameruleHelper.register("netherWater", false);
		/* //Gamerule display for StyledPlayerList
	"<color:#00ff00>Extension Gamerules:</color>",
    "<color:#88ff88>Villager Trading Lock: <color:#8888ff>%ext:villagertradelock%</color>",
	"No Mutual Exclusive: <color:#8888ff>%ext:disablemutualexclusiveenchantments%</color>",
	"Item Cooldown: <color:#8888ff>%ext:itemcooldown%</color>",
	"Ender Pearl Cooldown: <color:#8888ff>%ext:enderpearlcooldown%</color>",
	"Chorus Fruit Cooldown: <color:#8888ff>%ext:chorusfruitcooldown%</color>",
	"Shield Cooldown: <color:#8888ff>%ext:shieldcooldown%</color>",
	"Attack Cooldown: <color:#8888ff>%ext:attackcooldown%</color>",
	"Falling Blocks: <color:#8888ff>%ext:fallingblocks%</color>",
	"Entities Trample Crops: <color:#8888ff>%ext:entitiestramplecrops%</color>",
	"Farmland Watering Radius: <color:#ff6666>%ext:farmlandwateringradius%</color>",
	"Creeper Explosions: <color:#8888ff>%ext:creeperexplosions%</color>",
	"Soup Axolotls: <color:#8888ff>%ext:soupaxolotls%</color>",
	"Infinity Arrow Count: <color:#ff6666>%ext:infinityarrowcount%</color>",
	"Water Placable In Nether: <color:#8888ff>%ext:netherwater%</color></color>",
	"TPA: <color:#8888ff>%ext:tpaenabled%</color>",
	"Homes: <color:#8888ff>%ext:homesenabled%</color></color>"
		*/

		swapRow.register();
		locateEntityCommand.register();
		musicCommand.register();
		RightClickHarvest.register();
		FlyCommand.register();
		NicknameCommand.register();
		HomeCommand.register();
		TpaCommand.register();
	}
}
