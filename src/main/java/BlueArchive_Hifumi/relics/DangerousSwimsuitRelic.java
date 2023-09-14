package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.CollectAction;
import BlueArchive_Hifumi.actions.PeroroCoverAction;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class DangerousSwimsuitRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("DangerousSwimsuitRelic");

    public static final int DRAW_AMOUNT = 2;
    public int turn = 1;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("dangerous_swimsuit_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("dangerous_swimsuit_relic.png"));

    public DangerousSwimsuitRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    public void atPreBattle() {
        this.turn = 1;
    }

    public void atTurnStartPostDraw() {
        if (turn++ == 2) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new CollectAction(DRAW_AMOUNT, false));
            this.grayscale = true;
        }
    }


    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + DRAW_AMOUNT + this.DESCRIPTIONS[1] ;
    }
}
