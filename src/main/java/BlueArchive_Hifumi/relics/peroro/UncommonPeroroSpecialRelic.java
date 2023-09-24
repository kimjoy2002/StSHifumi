package BlueArchive_Hifumi.relics.peroro;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.effects.LostRelicEffect;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class UncommonPeroroSpecialRelic extends CustomRelic implements PeroroGoodsRelic {
    public static final String ID = DefaultMod.makeID("UncommonPeroroSpecialRelic");

    public static final int AMOUNT = 3;
    public static final int AMOUNT_PLUS = 1;
    public int val = AMOUNT;
    public int temp_count = 0;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("uncommon_peroro_special_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("uncommon_peroro_special_relic.png"));


    boolean isTemp = false;
    public void setTemp(){isTemp = true;}
    public UncommonPeroroSpecialRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
        setCounter(1);
    }
    public int getIndex(){
        return 5;
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
    public void updateDescription() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
    public void onVictory() {
        grayscale = false;
        setCounter(counter+temp_count);
        temp_count = 0;
        if(isTemp) {
            AbstractDungeon.effectList.add(new LostRelicEffect(relicId));
        }
    }

    @Override
    public void addTempCounter(int counter) {
        temp_count-=counter;;
        setCounter(this.counter+counter);
    }
    public void use() {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(val));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + val + this.DESCRIPTIONS[1] + AMOUNT_PLUS + this.DESCRIPTIONS[2];
    }
    public boolean canSpawn() {return false;}
}
