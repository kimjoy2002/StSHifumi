package BlueArchive_Hifumi.relics.peroro;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.PeroroBlockAction;
import BlueArchive_Hifumi.effects.LostRelicEffect;
import BlueArchive_Hifumi.powers.PeroroHologramPower;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.MetallicizePower;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class UncommonPeroroBlockRelic extends CustomRelic implements PeroroGoodsRelic {
    public static final String ID = DefaultMod.makeID("UncommonPeroroBlockRelic");

    public static final int AMOUNT = 6;
    public static final int AMOUNT_PLUS = 2;
    public int val = AMOUNT;
    public int temp_count = 0;


    public int currentPower =AMOUNT;

    boolean isTemp = false;
    public void setTemp(){isTemp = true;}



    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("uncommon_peroro_block_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("uncommon_peroro_block_relic.png"));


    public UncommonPeroroBlockRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
        setCounter(1);
    }
    public int getIndex(){
        return 3;
    }

    public void setCounter(int counter) {
        this.counter = counter;
        val = AMOUNT + AMOUNT_PLUS * (counter-1);
        updateDescription();
    }

    @Override
    public int getMagic(int add){
        return val + AMOUNT_PLUS * add;
    };
    @Override
    public int getMagic2(int add){
        return AMOUNT_PLUS;
    };

    public void updateDescription() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void addTempCounter(int counter) {
        temp_count-=counter;;
        setCounter(this.counter+counter);
    }
    public void atPreBattle() {
        stopPulse();
    }

    public void onVictory() {
        grayscale = false;
        stopPulse();
        setCounter(counter+temp_count);
        temp_count = 0;
        if(isTemp) {
            AbstractDungeon.effectList.add(new LostRelicEffect(relicId));
        }
    }

    public void use() {
        AbstractDungeon.actionManager.addToBottom(new PeroroBlockAction(val,AMOUNT_PLUS,true));
    }


    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + val + this.DESCRIPTIONS[1] + AMOUNT_PLUS + this.DESCRIPTIONS[2];
    }
    public boolean canSpawn() {return false;}
}
