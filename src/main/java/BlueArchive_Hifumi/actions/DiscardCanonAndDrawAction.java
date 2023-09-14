package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.AbstractDefaultCard;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DiscardCanonAndDrawAction extends AbstractGameAction {
    private AbstractPlayer p;

    private boolean upgraded = false;

    public DiscardCanonAndDrawAction(boolean upgraded) {
        this.p = AbstractDungeon.player;
        this.upgraded = upgraded;
        this.setValues(this.p, this.p, 1);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for(AbstractCard card : p.hand.group) {
            if(card.selfRetain != true) {
                group.addToTop(card);
            }
        }
        for(AbstractCard card : group.group) {
            if(card.hasTag(EnumPatch.CANNON)) {
                card.dontTriggerOnUseCard = true;
                if(card instanceof AbstractDefaultCard)
                    ((AbstractDefaultCard)card).triggerEndturn = true;
                card.use(p, null);
                if(upgraded)
                    card.use(p, null);
                AbstractDungeon.actionManager.addToBottom(new UseCardAction(card));
            }
            AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(card));
        }
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(group.size()));
        this.isDone = true;
    }
}
