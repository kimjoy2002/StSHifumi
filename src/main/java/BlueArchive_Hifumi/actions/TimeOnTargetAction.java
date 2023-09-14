package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.TimeOnTarget;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TimeOnTargetAction extends AbstractGameAction {
    public TimeOnTargetAction() {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if(c instanceof TimeOnTarget) {
                boolean temp = c.dontTriggerOnUseCard;
                c.dontTriggerOnUseCard = true;
                c.applyPowers();
                c.dontTriggerOnUseCard = temp;
                c.use(AbstractDungeon.player, null);
            }
        }
        this.isDone = true;
    }
}
