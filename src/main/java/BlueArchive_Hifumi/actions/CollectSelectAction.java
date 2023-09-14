package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.powers.CollectPower;
import BlueArchive_Hifumi.relics.CollectRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hifumi.actions.CollectAction.afterCollect;
import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;
import static java.lang.Math.max;

public class CollectSelectAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;
    AbstractCard specific;

    public CollectSelectAction(int numberOfCards, boolean optional) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.specific = null;
    }

    public CollectSelectAction(int numberOfCards) {
        this(numberOfCards, false);
    }

    public CollectSelectAction(AbstractCard specific) {

        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = 0;
        this.optional = false;
        this.specific = specific;
    }

    public void update() {
        if (this.duration == this.startDuration && specific == null) {
            CardGroup collectPile = new CardGroup(UNSPECIFIED);
            for(AbstractCard card : this.player.discardPile.group) {
                if(!card.hasTag(EnumPatch.NONCOLLECTABLE)) {
                    collectPile.addToTop(card);
                }
            }


            if (!collectPile.isEmpty() && this.numberOfCards > 0) {
                if (collectPile.size() <= this.numberOfCards && !this.optional) {
                    ArrayList<AbstractCard> cardsToMove = new ArrayList();
                    Iterator var5 = this.player.discardPile.group.iterator();

                    AbstractCard c;
                    while (var5.hasNext()) {
                        c = (AbstractCard) var5.next();
                        if(!c.hasTag(EnumPatch.NONCOLLECTABLE)) {
                            cardsToMove.add(c);
                        }
                    }

                    var5 = cardsToMove.iterator();

                    while (var5.hasNext()) {
                        c = (AbstractCard) var5.next();
                        if (this.player.hand.size() < 10) {
                            this.player.hand.addToHand(c);
                            this.player.discardPile.removeCard(c);
                            afterCollect(c, false);
                        }

                        c.lighten(false);
                        c.applyPowers();
                    }

                    this.isDone = true;
                } else {
                    if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(collectPile, this.numberOfCards, true, TEXT[0]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(collectPile, this.numberOfCards, TEXT[0], false);
                    }

                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            Iterator var1;
            AbstractCard c;

            if(specific != null) {
                if(!this.player.discardPile.contains(specific)) {
                    this.isDone = true;
                    return;
                }

                if (this.player.hand.size() < 10) {
                    this.player.hand.addToHand(specific);
                    this.player.discardPile.removeCard(specific);
                    afterCollect(specific, false);
                }

                specific.lighten(false);
                specific.unhover();
                specific.applyPowers();
                AbstractDungeon.player.hand.refreshHandLayout();
                specific = null;
            }

            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while (var1.hasNext()) {
                    c = (AbstractCard) var1.next();
                    if (this.player.hand.size() < 10) {
                        this.player.hand.addToHand(c);
                        this.player.discardPile.removeCard(c);
                        afterCollect(c, false);
                    }

                    c.lighten(false);
                    c.unhover();
                    c.applyPowers();
                }

                for (var1 = this.player.discardPile.group.iterator(); var1.hasNext(); c.target_y = 0.0F) {
                    c = (AbstractCard) var1.next();
                    c.unhover();
                    c.target_x = (float) CardGroup.DISCARD_PILE_X;
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
            if (this.isDone) {
                var1 = this.player.hand.group.iterator();

                while (var1.hasNext()) {
                    c = (AbstractCard) var1.next();
                    c.applyPowers();
                }
            }

        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:CollectSelectAction");
        TEXT = uiStrings.TEXT;
    }
}