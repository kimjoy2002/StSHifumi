package BlueArchive_Hifumi.relics.peroro;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class RarePeroroEnergyRelic extends CustomRelic implements PeroroGoodsRelic {
    public static final String ID = DefaultMod.makeID("RarePeroroEnergyRelic");

    public static final int AMOUNT = 3;
    public static final int AMOUNT_PLUS = 1;
    public int val = AMOUNT;
    public int temp_count = 0;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("rare_peroro_energy_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("rare_peroro_energy_relic.png"));


    public RarePeroroEnergyRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
        setCounter(1);
    }
    public int getIndex(){
        return 8;
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
    }

    public void use() {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(val));
    }
    @Override
    public void addTempCounter(int counter) {
        temp_count-=counter;;
        setCounter(this.counter+counter);
    }

    public String getUpdatedDescription() {
        String temp = this.DESCRIPTIONS[0];
        for (int i = 0; i < val; i++) {
            temp +=this.DESCRIPTIONS[1];
        }
        temp +=this.DESCRIPTIONS[2];
        return temp;
    }
    public boolean canSpawn() {return false;}
}
