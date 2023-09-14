package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.UsePeroroGoodsAction;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Collections;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class TwinPeroroRelic extends CustomRelic implements PeroroGoodsRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("TwinPeroroRelic");


    private boolean firstTurn = true;

    public int temp_count = 0;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("twin_peroro_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("twin_peroro_relic.png"));

    public TwinPeroroRelic() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.CLINK);
    }

    public int getIndex(){
        return 9;
    }
    public void onVictory() {
        grayscale = false;
    }
    public void updateDescription() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public int getPeroroGoodsNum() {
        int count = 0;
        for(AbstractRelic relic: AbstractDungeon.player.relics) {
            if(relic instanceof PeroroGoodsRelic && relic != this) {
                if(((AbstractRelic)relic).grayscale == false && ((AbstractRelic)relic).counter >0 )
                    count++;
            }
        }
        return count;
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }

    public ArrayList<AbstractRelic> getPeroroGoodsList() {
        ArrayList<AbstractRelic> randomRelics = new ArrayList<AbstractRelic>();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if(relic instanceof PeroroGoodsRelic && !(relic instanceof TwinPeroroRelic) && relic.counter > 0 && relic.grayscale == false) {
                randomRelics.add(relic);
            }
        }
        return randomRelics;
    }

    public void atTurnStartPostDraw() {
        if (this.firstTurn) {
            if(getPeroroGoodsNum() < 3) {
                this.grayscale = true;
            }
            this.firstTurn = false;
        }
    }

    public static void checkTwinPeroro() {
        if(AbstractDungeon.player.hasRelic(TwinPeroroRelic.ID)) {
            TwinPeroroRelic relic_ = (TwinPeroroRelic) AbstractDungeon.player.getRelic(TwinPeroroRelic.ID);
            if(relic_.getPeroroGoodsNum() < 3) {
                relic_.grayscale = true;
            }
        }
    }
    public void use() {
        ArrayList<AbstractRelic> relics = getPeroroGoodsList();
        Collections.shuffle(relics, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
        if(relics.size() > 0) {
            AbstractDungeon.actionManager.addToBottom(new UsePeroroGoodsAction(relics.get(0), 0));
            relics.remove(0);
            if(relics.size() > 0) {
                AbstractDungeon.actionManager.addToBottom(new UsePeroroGoodsAction(relics.get(0), 0));
            }
        }
    }

    @Override
    public int getMagic(int add){
        return 2;
    };

    @Override
    public void addTempCounter(int counter) {
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
