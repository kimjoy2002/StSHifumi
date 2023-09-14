package BlueArchive_Hifumi.relics;


import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.Reisa;
import BlueArchive_Hifumi.effects.ObtainRelicEffect;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;
import static BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic.obtainCommonPeroroGoods;

public class HifumiBaseRelicPlus extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("HifumiBaseRelicPlus");

    public static final int AMOUNT = 5;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HifumiBaseRelicPlus.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HifumiBaseRelicPlus.png"));

    public HifumiBaseRelicPlus() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + AMOUNT + this.DESCRIPTIONS[1];
    }


    public void onEnterRoom(AbstractRoom room) {
        if (!this.usedUp) {
            this.flash();
            setCounter(counter+1);
        }
    }

    public void setCounter(int setCounter) {
        if(setCounter>AMOUNT) {
            AbstractDungeon.topLevelEffectsQueue.add(new ObtainRelicEffect());
            setCounter = 1;
            stopPulse();
        } else if(setCounter == AMOUNT) {
            beginPulse();
        }

        this.counter = setCounter;
        if (setCounter == -2) {
            this.usedUp();
            this.counter = -2;
        }

    }



    public void obtain() {
        setCounter(1);
        if (AbstractDungeon.player.hasRelic(HifumiBaseRelic.ID)) {
            for(int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (((AbstractRelic)AbstractDungeon.player.relics.get(i)).relicId.equals(HifumiBaseRelic.ID)) {
                    this.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(HifumiBaseRelic.ID);
    }


}
