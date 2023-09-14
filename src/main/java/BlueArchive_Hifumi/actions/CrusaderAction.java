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

public class CrusaderAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private int power;
    private boolean optional;

    public CrusaderAction(int numberOfCards, int power, boolean optional) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.power = power;
        this.optional = optional;
    }

    public CrusaderAction(int numberOfCards, int power) {
        this(numberOfCards, power, false);
    }

    public void addCannonPower(AbstractCard card){
        if(card.baseDamage > 0)
            card.baseDamage += power;
        if(card.baseBlock > 0)
            card.baseBlock += power;
    }

    public void update() {
        CardGroup collectPile = new CardGroup(UNSPECIFIED);
        for (AbstractCard card : this.player.discardPile.group) {
            if (!card.hasTag(EnumPatch.NONCOLLECTABLE)) {
                collectPile.addToTop(card);
            }
        }


        if (!collectPile.isEmpty() && this.numberOfCards > 0) {
            ArrayList<AbstractCard> cardsToMove = new ArrayList();
            Iterator var5 = this.player.discardPile.group.iterator();

            AbstractCard c;
            while (var5.hasNext()) {
                c = (AbstractCard) var5.next();
                if (c.hasTag(EnumPatch.CANNON)) {
                    cardsToMove.add(c);
                    if (--numberOfCards == 0) {
                        break;
                    }
                }
            }

            var5 = cardsToMove.iterator();

            while (var5.hasNext()) {
                c = (AbstractCard) var5.next();
                if (this.player.hand.size() < 10) {
                    this.player.hand.addToHand(c);
                    this.player.discardPile.removeCard(c);
                    addCannonPower(c);
                }

                c.lighten(false);
                c.applyPowers();
            }

            this.isDone = true;
        } else {
            this.isDone = true;
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:CollectSelectAction");
        TEXT = uiStrings.TEXT;
    }
}