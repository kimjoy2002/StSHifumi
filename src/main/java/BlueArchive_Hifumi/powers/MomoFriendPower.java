package BlueArchive_Hifumi.powers;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.LabelingAction;
import BlueArchive_Hifumi.actions.UsePeroroGoodsAction;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static BlueArchive_Hifumi.DefaultMod.makePowerPath;

public class MomoFriendPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = DefaultMod.makeID("MomoFriendPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("MomoFriendPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("MomoFriendPower32.png"));

    public AbstractRelic relic;
    private static int momoIdOffset;
    public MomoFriendPower(AbstractRelic relic) {
        name = NAME;
        ID = POWER_ID + momoIdOffset;
        ++momoIdOffset;

        this.owner = AbstractDungeon.player;
        this.relic = relic;
        this.source = AbstractDungeon.player;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void atStartOfTurnPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new UsePeroroGoodsAction(relic, 0));
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + FontHelper.colorString(relic.name, "y") + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MomoFriendPower(relic);
    }
}
