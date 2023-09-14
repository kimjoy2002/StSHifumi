package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cardmods.BuriedMod;
import BlueArchive_Hifumi.cardmods.NonCollectMod;
import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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
import static BlueArchive_Hifumi.cards.SealedStorage.PER_BLOCK;
import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class SpadeworkAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private int count;
    private boolean optional;
    private boolean noncollectable;

    public SpadeworkAction(int numberOfCards, boolean optional, boolean noncollectable) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.noncollectable = noncollectable;
        this.count = 0;
    }

    public SpadeworkAction(int numberOfCards) {
        this(numberOfCards, false, false);
    }


    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup ablePile = new CardGroup(UNSPECIFIED);
            for(AbstractCard card : this.player.discardPile.group) {
                if(noncollectable) {
                    if(!(card.hasTag(EnumPatch.BURIED)) || !(card.hasTag(EnumPatch.NONCOLLECTABLE))) {
                        ablePile.addToTop(card);
                    }
                } else {
                    if(!(card.hasTag(EnumPatch.BURIED))) {
                        ablePile.addToTop(card);
                    }
                }

            }

            if (!ablePile.isEmpty() && this.numberOfCards > 0) {
                if (ablePile.size() <= this.numberOfCards && !this.optional) {
                    Iterator var5 = ablePile.group.iterator();

                    AbstractCard c;
                    while (var5.hasNext()) {
                        c = (AbstractCard) var5.next();
                        if(noncollectable)
                            CardModifierManager.addModifier(c, new NonCollectMod());
                        CardModifierManager.addModifier(c, new BuriedMod());
                    }

                    this.isDone = true;
                } else {
                    if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(ablePile, this.numberOfCards, true, TEXT[2]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(ablePile, this.numberOfCards, TEXT[2], false);
                    }

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
                    if(noncollectable)
                        CardModifierManager.addModifier(c, new NonCollectMod());
                    CardModifierManager.addModifier(c, new BuriedMod());
                    c.lighten(false);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:SpadeworkAction");
        TEXT = uiStrings.TEXT;
    }
}