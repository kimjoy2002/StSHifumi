package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hifumi.actions.CollectAction.afterCollect;
import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class OurStoryAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;

    public OurStoryAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }



    public void update() {
        if (this.duration == this.startDuration) {

            if (!player.discardPile.isEmpty()) {
                if (player.discardPile.size() <= 1) {

                    CardGroup return_group = new CardGroup(UNSPECIFIED);
                    for(AbstractCard card : this.player.discardPile.group) {
                        return_group.addToRandomSpot(card);
                    }

                    for (AbstractCard card : return_group.group) {
                        if (this.player.hand.size() < 10) {
                            this.player.hand.addToHand(card);
                            this.player.discardPile.removeCard(card);
                        }

                        card.lighten(false);
                        card.unhover();
                        card.applyPowers();
                    }

                    this.isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(this.player.discardPile, 1, TEXT[0], false);

                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                AbstractCard selectCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

                CardGroup return_group = new CardGroup(UNSPECIFIED);
                for(AbstractCard card : this.player.discardPile.group) {
                    if(card.cardID == selectCard.cardID) {
                        return_group.addToRandomSpot(card);
                    }
                }

                for (AbstractCard card : return_group.group) {
                    if (this.player.hand.size() < 10) {
                        this.player.hand.addToHand(card);
                        this.player.discardPile.removeCard(card);
                    }

                    card.lighten(false);
                    card.unhover();
                    card.applyPowers();
                }

                for (Iterator<AbstractCard> var1 = this.player.discardPile.group.iterator(); var1.hasNext(); ) {
                    AbstractCard c = (AbstractCard) var1.next();
                    c.unhover();
                    c.target_x = (float) CardGroup.DISCARD_PILE_X;
                    c.target_y = 0.0F;
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:OurStoryAction");
        TEXT = uiStrings.TEXT;
    }
}