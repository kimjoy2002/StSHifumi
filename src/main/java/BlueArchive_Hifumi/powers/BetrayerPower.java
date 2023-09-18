package BlueArchive_Hifumi.powers;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.BetrayerAction;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hifumi.DefaultMod.makePowerPath;

public class BetrayerPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("BetrayerPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("BetrayerPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("BetrayerPower32.png"));

    public BetrayerPower(final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.source = AbstractDungeon.player;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        if(AbstractDungeon.player instanceof Hifumi) {
            AbstractDungeon.player.state.setAnimation(0, "base_animation_paust", true);
        }
        updateDescription();
    }

    public void onVictory() {
        if(AbstractDungeon.player instanceof Hifumi) {
            AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, "offHead", false);
            AbstractDungeon.player.state.addAnimation(0, "base_animation", true, e.getEndTime());
        }
    }

    public void onRemove() {
        if(AbstractDungeon.player instanceof Hifumi) {
            AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, "offHead", false);
            AbstractDungeon.player.state.addAnimation(0, "base_animation", true, e.getEndTime());
        }
    }

    public void atStartOfTurnPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new BetrayerAction(amount));
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BetrayerPower(amount);
    }
}
