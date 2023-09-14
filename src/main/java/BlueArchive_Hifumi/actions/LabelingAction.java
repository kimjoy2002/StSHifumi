package BlueArchive_Hifumi.actions;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static BlueArchive_Hifumi.actions.CollectAction.afterCollect;
import static BlueArchive_Hifumi.cards.SealedStorage.PER_BLOCK;

public class LabelingAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;

    public LabelingAction(int numberOfCards) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = true;
    }



    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.discardPile.size() > 1 && this.numberOfCards > 0) {
                AbstractDungeon.gridSelectScreen.open(this.player.discardPile, this.numberOfCards, true, TEXT[0]);
                this.tickDuration();
            } else {
                this.isDone = true;
            }
        } else {
            Iterator var1;
            AbstractCard c;

            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

                Collections.shuffle(AbstractDungeon.gridSelectScreen.selectedCards, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));

                var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while (var1.hasNext()) {
                    c = (AbstractCard) var1.next();
                    c.lighten(false);
                    c.unhover();
                    c.applyPowers();
                    this.player.discardPile.removeCard(c);
                    this.player.discardPile.addToBottom(c);

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
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:LabelingAction");
        TEXT = uiStrings.TEXT;
    }
}