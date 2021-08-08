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
		/*
		Villager Trading Lock: %ext:villagertradelock%\nNo Mutual Exclusive: %disablemutualexclusiveenchantments%\nItem Cooldown: %ext:itemcooldown%\nEnder Pearl Cooldown: %ext:enderpearlcooldown%\nChorus Fruit Cooldown: %ext:chorusfruitcooldown%\nShield Cooldown: %ext:shieldcooldown%\nAttack Cooldown: %ext:attackcooldown%\nFalling Blocks: %ext:fallingblocks%\nEntities Trample Crops: %ext:entitiesTrampleCrops%\nFarmland Watering Radius: %ext:farmlandwateringradius%\nCreeper Explosions: %ext:creeperexplosions%\nSoup Axolotls: %ext:soupaxolotls%\nInfinity Arrow Count: %ext:infinityarrowcount%
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
