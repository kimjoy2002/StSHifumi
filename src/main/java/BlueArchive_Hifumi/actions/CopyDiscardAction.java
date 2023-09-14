package BlueArchive_Hifumi.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class CopyDiscardAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int amount;
    public CopyDiscardAction(int amount) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.amount = amount;
    }


    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup ablePile = new CardGroup(UNSPECIFIED);
            for(AbstractCard card : this.player.discardPile.group) {
                ablePile.addToTop(card);
            }

            if (this.player.discardPile.size() > 0) {
                if (this.player.discardPile.size() == 1) {
                    AbstractCard c = this.player.discardPile.getTopCard();

                    this.addToTop(new MakeTempCardInDiscardAction(c.makeStatEquivalentCopy(), amount));

                    this.isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(ablePile, 1, TEXT[0], false);
                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            Iterator var1;
            AbstractCard c;
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();


                while (var1.hasNext()) {
                    c = (AbstractCard) var1.next();
                    this.addToTop(new MakeTempCardInDiscardAction(c.makeStatEquivalentCopy(), amount));
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:CopyDiscardAction");
        TEXT = uiStrings.TEXT;
    }
}