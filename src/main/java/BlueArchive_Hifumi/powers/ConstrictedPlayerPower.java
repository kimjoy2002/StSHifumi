package BlueArchive_Hifumi.powers;

import BlueArchive_Hifumi.DefaultMod;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ConstrictedPlayerPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID("Constricted");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AbstractCreature source;

    public ConstrictedPlayerPower(AbstractCreature target, AbstractCreature source, int fadeAmt) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = target;
        this.source = source;
        this.amount = fadeAmt;
        this.updateDescription();
        this.loadRegion("constricted");
        this.type = PowerType.DEBUFF;
        this.priority = 105;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONSTRICTED", 0.05F);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
    @Override
    public void atStartOfTurn() {
        this.flashWithoutSound();
        this.playApplyPowerSfx();
        this.addToBot(new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS)));
    }

}
