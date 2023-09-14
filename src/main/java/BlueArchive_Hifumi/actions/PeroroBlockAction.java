package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.powers.PeroroHologramPower;
import BlueArchive_Hifumi.powers.PerorodzillaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class PeroroBlockAction extends AbstractGameAction {
    boolean block;
    int stack;
    public PeroroBlockAction(int amt, int stack, boolean block) {
        this.actionType = ActionType.WAIT;
        this.amount = amt;
        this.stack = stack;
        this.block = block;
    }

    public void update() {
        int value = AbstractDungeon.player.hasPower(block?PeroroHologramPower.POWER_ID: PerorodzillaPower.POWER_ID)?stack:amount;

        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                block?new PeroroHologramPower(value):new PerorodzillaPower(value), value));


        this.isDone = true;
    }
}