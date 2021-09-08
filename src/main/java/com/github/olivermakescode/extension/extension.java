package com.github.olivermakescode.extension;

import net.fabricmc.api.ModInitializer;

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
	public static BoolRuleHelper alphaSponges;
	public static BoolRuleHelper alphaFluid;
	public static BoolRuleHelper alphaTnt;
	public static BoolRuleHelper wololo;

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
		alphaSponges = (BoolRuleHelper) GameruleHelper.register("classicSponges", false);
		alphaFluid = (BoolRuleHelper) GameruleHelper.register("classicFluid", false);
		alphaTnt = (BoolRuleHelper) GameruleHelper.register("alphaTnt", false);
		wololo = (BoolRuleHelper) GameruleHelper.register("wololo", true);

		locateEntityCommand.register();
		RightClickHarvest.register();
		NicknameCommand.register();
		musicCommand.register();
		HomeCommand.register();
		LoreCommand.register();
		FlyCommand.register();
		TpaCommand.register();
		swapRow.register();
	}
}
