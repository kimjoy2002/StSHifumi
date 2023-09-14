package BlueArchive_Hifumi.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class BetrayerAction extends AbstractGameAction {
    public BetrayerAction(int amount) {
        this.amount = amount;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        int amount_ = amount;
        CardGroup reducePile = new CardGroup(UNSPECIFIED);
        CardGroup reduceSecondPile = new CardGroup(UNSPECIFIED);
        for(AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if(!card.freeToPlayOnce && card.cost > 0 && card.costForTurn > 0) {
                reducePile.addToRandomSpot(card);
            } else if(!card.freeToPlayOnce && card.cost > 0 && card.costForTurn == 0) {
                reduceSecondPile.addToRandomSpot(card);
            }
        }

        for(AbstractCard card : reducePile.group) {
            card.freeToPlayOnce = true;
            if(--amount_ <= 0) {
                this.isDone = true;
                return;
            }
        }
        for(AbstractCard card : reduceSecondPile.group) {
            card.freeToPlayOnce = true;
            if(--amount_ <= 0) {
                this.isDone = true;
                return;
            }
        }
        this.isDone = true;
    }


    private void findAndModifyCard(boolean better) {
        AbstractCard c = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
        if (better) {
            if (c.costForTurn > 0) {
                c.cost = 0;
                c.costForTurn = 0;
                c.isCostModified = true;
                c.superFlash(Color.GOLD.cpy());
            } else {
                this.findAndModifyCard(better);
            }
        } else if (c.cost > 0) {
            c.cost = 0;
            c.costForTurn = 0;
            c.isCostModified = true;
            c.superFlash(Color.GOLD.cpy());
        } else {
            this.findAndModifyCard(better);
        }
    }
}
