package BlueArchive_Hifumi.powers;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.CollectSelectAction;
import BlueArchive_Hifumi.actions.IncineratorAction;
import BlueArchive_Hifumi.actions.ItazuraAction;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_Hifumi.DefaultMod.makePowerPath;
import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class ItazuraStraightPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public AbstractCard card;

    public static final String POWER_ID = DefaultMod.makeID("ItazuraStraightPower");
    public static int guid = 0;
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ItazuraStraightPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ItazuraStraightPower32.png"));

    public ItazuraStraightPower(final AbstractCard card) {
        name = NAME;
        ID = POWER_ID + (guid++);

        this.owner = AbstractDungeon.player;
        this.source = AbstractDungeon.player;
        this.card = card;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void atStartOfTurnPostDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new CollectSelectAction(card));
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ItazuraStraightPower(card);
    }
}
