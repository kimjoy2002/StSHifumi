package BlueArchive_Hifumi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class YouAreMineAction extends AbstractGameAction {
    AbstractMonster m;
    int multi_;
    public YouAreMineAction(AbstractMonster m, int multi_) {
        this.m = m;
        this.multi_ = multi_;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        if(m != null) {
            int amount = 0;
            for(AbstractPower p : m.powers)  {
                if(p.amount > 0 && p.type == AbstractPower.PowerType.DEBUFF) {
                    amount += p.amount;
                }
            }
            if(amount > 0) {
                this.addToBot(new LoseHPAction(m, AbstractDungeon.player, amount* multi_, AbstractGameAction.AttackEffect.POISON));
            }
        }

        this.isDone = true;
    }
}
