package com.github.olivermakescode.extension;

import eu.pb4.placeholders.PlaceholderAPI;
import eu.pb4.placeholders.PlaceholderResult;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

public class IntRuleHelper implements GameRuleInterface {
    private int value;
    private GameRules.Key<GameRules.IntRule> rule;

    public IntRuleHelper(String name, int defaultValue) {
        this.value = defaultValue;
        this.rule = GameRuleRegistry.register(name, GameRules.Category.MISC, GameRuleFactory.createIntRule(defaultValue));
        PlaceholderAPI.register(new Identifier("ext",name.toLowerCase()), ctx -> PlaceholderResult.value(String.valueOf(this.getValue())));
    }

    public IntRuleHelper(String name, int defaultValue, int min) {
        this.value = defaultValue;
        this.rule = GameRuleRegistry.register(name, GameRules.Category.MISC, GameRuleFactory.createIntRule(defaultValue, min));
        PlaceholderAPI.register(new Identifier("ext",name.toLowerCase()), ctx -> PlaceholderResult.value(String.valueOf(this.getValue())));
    }

    public IntRuleHelper(String name, int defaultValue, int min, int max) {
        this.value = defaultValue;
        this.rule = GameRuleRegistry.register(name, GameRules.Category.MISC, GameRuleFactory.createIntRule(defaultValue, min, max));
        PlaceholderAPI.register(new Identifier("ext",name.toLowerCase()), ctx -> PlaceholderResult.value(String.valueOf(this.getValue())));
    }

    @Override
    public void updateValue() throws NullPointerException {
        assert GameruleHelper.server != null;
        this.value = GameruleHelper.server.getGameRules().getInt(rule);
    }

    public int getValue() {
        if (GameruleHelper.server != null)
            this.updateValue();
        return this.value;
    }
}
