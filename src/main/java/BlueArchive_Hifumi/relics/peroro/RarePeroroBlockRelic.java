package BlueArchive_Hifumi.relics.peroro;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.effects.LostRelicEffect;
import BlueArchive_Hifumi.relics.TwinPeroroRelic;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class RarePeroroBlockRelic extends CustomRelic implements PeroroGoodsRelic {
    public static final String ID = DefaultMod.makeID("RarePeroroBlockRelic");

    public static final int AMOUNT = 2;
    public int val = AMOUNT;
    public int temp_count = 0;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("rare_peroro_block_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("rare_peroro_block_relic.png"));


    boolean isTemp = false;
    public void setTemp(){isTemp = true;}
    public RarePeroroBlockRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
        setCounter(1);
    }

    public int getIndex(){
        return 6;
    }
    public void setCounter(int counter) {
        this.counter = counter;
        val = AMOUNT;
        if(counter == 0) {
            grayscale = true;
            TwinPeroroRelic.checkTwinPeroro();
        }
        updateDescription();
    }
    @Override
    public int getMagic(int add) {
        return val;
    };

    public void updateDescription() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
    public void atPreBattle() {
        if(counter > 0) {
            grayscale = false;
        }
        temp_count = 0;
    }

    @Override
    public void addTempCounter(int counter) {
        temp_count-=counter;;
        setCounter(this.counter+counter);
    }
    public void onVictory() {
        grayscale = false;
        setCounter(counter+temp_count);
        temp_count = 0;
        if(isTemp) {
            AbstractDungeon.effectList.add(new LostRelicEffect(relicId));
        }
    }

    public void use() {
        if(counter > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePlayerPower(AbstractDungeon.player, val), val));
            setCounter(counter-1);
            temp_count++;
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + val + this.DESCRIPTIONS[1];
    }
    public boolean canSpawn() {return false;}
}
