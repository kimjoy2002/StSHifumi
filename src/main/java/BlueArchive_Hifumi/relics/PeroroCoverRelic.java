package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.PeroroCoverAction;
import BlueArchive_Hifumi.cards.ChainExplosion;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class PeroroCoverRelic extends CustomRelic {

    private boolean firstTurn = true;
    // ID, images, text.
    public static final String ID = DefaultMod.makeID("PeroroCoverRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("peroro_cover.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("peroro_cover.png"));

    public PeroroCoverRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.SOLID);
    }


    public void atPreBattle() {
        this.firstTurn = true;
    }



    public void atTurnStartPostDraw() {
        if (this.firstTurn) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new PeroroCoverAction(1));
            this.firstTurn = false;
            this.grayscale = true;
        }
    }
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
