package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.ObtainPeroroGoodsAction;
import BlueArchive_Hifumi.relics.peroro.CommonPeroroBlockRelic;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static BlueArchive_Hifumi.DefaultMod.*;

public class HifumiBaseRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("HifumiBaseRelic");

    public static final int AMOUNT = 1;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HifumiBaseRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HifumiBaseRelic.png"));

    public HifumiBaseRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    public void atTurnStart() {
        if(!usedUp) {
            AbstractDungeon.actionManager.addToBottom(new ObtainPeroroGoodsAction());
            this.flash();
            this.setCounter(-2);
        }

    }

    public void setCounter(int setCounter) {
        this.counter = setCounter;
        if (setCounter == -2) {
            this.usedUp();
            this.counter = -2;
        }

    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
