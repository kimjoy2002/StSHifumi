package BlueArchive_Hifumi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class UpgradeCanonAction extends AbstractGameAction {
    private AbstractMonster m;

    public UpgradeCanonAction(int weakAmt, AbstractMonster m) {
        this.actionType = ActionType.WAIT;
        this.amount = weakAmt;
        this.m = m;
    }

    public void update() {
        if (this.m != null) {
            if(this.m.getIntentBaseDmg() >= 0) {
                this.addToTop(new ApplyPowerAction(this.m, AbstractDungeon.player, new WeakPower(this.m, this.amount, false), this.amount));
            } else {
                this.addToTop(new ApplyPowerAction(this.m, AbstractDungeon.player, new VulnerablePower(this.m, this.amount, false), this.amount));
            }

        }

        this.isDone = true;
    }
}