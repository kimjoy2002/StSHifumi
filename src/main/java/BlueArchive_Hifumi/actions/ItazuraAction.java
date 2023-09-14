package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cardmods.BuriedMod;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.powers.ItazuraStraightPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

public class ItazuraAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;
    public ItazuraAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }


    public void update() {
        if (this.duration == this.startDuration) {
            if (!this.player.drawPile.isEmpty()) {
                AbstractCard c;
                Iterator var6;
                if (this.player.drawPile.size() == 1) {
                    AbstractCard cardsToMove = this.player.drawPile.getTopCard();

                    this.player.drawPile.moveToDiscardPile(cardsToMove);
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ItazuraStraightPower(cardsToMove)));

                    this.isDone = true;
                } else {
                    CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    var6 = this.player.drawPile.group.iterator();

                    while(var6.hasNext()) {
                        c = (AbstractCard)var6.next();
                        if(!c.hasTag(EnumPatch.NONCOLLECTABLE)) {
                            temp.addToTop(c);
                        }
                    }

                    temp.sortAlphabetically(true);
                    temp.sortByRarityPlusStatusCardType(false);
                    AbstractDungeon.gridSelectScreen.open(temp, 1, TEXT[0], false);
                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    this.player.drawPile.moveToDiscardPile(c);
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ItazuraStraightPower(c)));
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:ItazuraAction");
        TEXT = uiStrings.TEXT;
    }
}

