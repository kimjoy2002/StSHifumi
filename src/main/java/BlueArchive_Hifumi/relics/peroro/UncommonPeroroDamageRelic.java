package BlueArchive_Hifumi.relics.peroro;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.AttackRandomAction;
import BlueArchive_Hifumi.actions.LowHPDamageAction;
import BlueArchive_Hifumi.actions.UpdatePeroroDamageAction;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class UncommonPeroroDamageRelic extends CustomRelic implements PeroroGoodsRelic {
    public static final String ID = DefaultMod.makeID("UncommonPeroroDamageRelic");

    public static final int AMOUNT = 12;
    public static final int PLUS = 6;
    public static final int AMOUNT_PLUS = 6;
    public int val = AMOUNT;
    public int val2 = PLUS;
    public int current_damage = val;
    public int temp_count = 0;
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("uncommon_peroro_damage_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("uncommon_peroro_damage_relic.png"));


    public UncommonPeroroDamageRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
        setCounter(1);
    }
    public int getIndex(){
        return 4;
    }

    public void setCounter(int counter) {
        this.counter = counter;
        val = AMOUNT;
        val2 = PLUS + AMOUNT_PLUS * (counter-1);
        current_damage = val;
        updateDescription();
    }

    @Override
    public int getMagic(int add){
        return current_damage;
    };

    @Override
    public int getMagic2(int add){
        return val2 + AMOUNT_PLUS * add;
    };
    @Override
    public void addTempCounter(int counter) {
        temp_count-=counter;
        setCounter(this.counter+counter);
    }
    public void updateDescription() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public void atPreBattle() {
        current_damage = val;
        updateDescription();
    }

    public void onVictory() {
        grayscale = false;
        current_damage = val;
        setCounter(counter+temp_count);
        temp_count = 0;
        updateDescription();
    }

    public void use() {
        AbstractDungeon.actionManager.addToBottom(new AttackRandomAction(new DamageInfo(AbstractDungeon.player, current_damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(new UpdatePeroroDamageAction(this));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + current_damage + this.DESCRIPTIONS[1] + val2 + this.DESCRIPTIONS[2] + AMOUNT_PLUS + this.DESCRIPTIONS[3];
    }
    public boolean canSpawn() {return false;}
}
