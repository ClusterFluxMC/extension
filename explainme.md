# Welcome to Extension

## A Vanilla client/server compatible mod in the vein of Essentials

### \+ A [Gamerule Helper API](https://github.com/ClusterFluxMC/extension/blob/main/Gamerule-Helper-API.md)

## Placeholder API:
- Namespace for all Extension placeholders is `ext`
- Nicknames accessible by the path `nickname` (In place of player name)
- All Extension gamerules have their value in the same path as their name, but as all lowercase

## Gamerules:
### villagerTradeLock
- whether villager trades instantly restock

### disableMutualExclusiveEnchantments
- disables mutually exclusive enchantments

### itemCooldown
- whether items have a cooldown

### enderPearlCooldown
- whether the ender pearl has a cooldown

### chorusFruitCooldown
- whether the chorus fruit has a cooldown

### shieldCooldown
- whether the shield has a cooldown

### attackCooldown
- whether there's an attack cooldown

### fallingBlocks
- whether blocks fall

### entitiesTrampleCrops
- whether entities trample crops

### farmlandWateringRadius
- the radius of farmland to check for water

### creeperExplosions
- toggles creeper explosions

### soupAxolotls
- when enabled, axolotls cycle through the variants when named "soup" or "Soup"

### rightClickHarvest
- harvest crops with right click

### infinityArrowCount
- amount of arrows needed for infinity to work

### netherWater
- allows water to be placed in nether

### alphaTnt
- Punch a Tnt to light it, punch it again to break it

### classicFluid
- Fluid is all source blocks

### classicSponges
- Sponges don't let water within 2 blocks

## Commands:
### hotbar, hb
- change hotbar to selected inventory row 

### nickname, nick
- change nickname

### locateentity
- locate an entity

### music
- play a song

### fly
- allows admins to fly (perm lvl 4)

### tpa, tpahere, tpaccept
- the tpa commands you would see in Essentials
    - disabled by default, enable the gamerule tpaEnabled to enable
   
 
### home, sethome, delhome
- the home commands you would see in Essentials
    - disabled by default, enable the gamerule homeEnabled to enable
