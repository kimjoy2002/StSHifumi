package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.PeroroCoverAction;
import BlueArchive_Hifumi.actions.UsePeroroGoodsAction;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class FigureStorageRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("FigureStorageRelic");


    private boolean firstTurn = true;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("figure_storage.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("figure_storage.png"));

    public FigureStorageRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }


    public void atTurnStartPostDraw() {
        if (this.firstTurn) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new UsePeroroGoodsAction(AbstractRelic.RelicTier.SPECIAL,-1, true,1));
            this.firstTurn = false;
            this.grayscale = true;
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + 1 + this.DESCRIPTIONS[1];
    }
}
