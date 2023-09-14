package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CannonAfterAction extends AbstractGameAction {
    AbstractCard card;
    AbstractCard.CardTarget target;
    public CannonAfterAction(AbstractCard card, AbstractCard.CardTarget target) {
        this.card = card;
        this.target = target;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        card.target = target;
        if(card instanceof AbstractDefaultCard)
            ((AbstractDefaultCard)card).triggerEndturn = false;
        this.isDone = true;
    }
}
