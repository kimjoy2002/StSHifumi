package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cardmods.BuriedMod;
import BlueArchive_Hifumi.cardmods.NonCollectMod;
import BlueArchive_Hifumi.cardmods.WashMod;
import BlueArchive_Hifumi.patches.EnumPatch;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;
import java.util.List;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class CarWashAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private int count;
    private boolean optional;

    public CarWashAction(int numberOfCards, boolean optional) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.count = 0;
    }

    public CarWashAction(int numberOfCards) {
        this(numberOfCards, false);
    }


    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup ablePile = new CardGroup(UNSPECIFIED);
            for(AbstractCard card : this.player.discardPile.group) {
                if(card.cost != -2) {
                    ablePile.addToTop(card);
                }
            }

            if (!ablePile.isEmpty() && this.numberOfCards > 0) {
                if (ablePile.size() <= this.numberOfCards && !this.optional) {
                    Iterator var5 = ablePile.group.iterator();

                    AbstractCard c;
                    while (var5.hasNext()) {
                        c = (AbstractCard) var5.next();
                        int amount = 1;
                        List<AbstractCardModifier> wash_ = CardModifierManager.getModifiers(c, WashMod.ID);
                        if(wash_.size()>0 && wash_.get(0) instanceof WashMod) {
                            amount = ((WashMod)wash_.get(0)).amount + 1;
                            CardModifierManager.removeModifiersById(c, WashMod.ID, true);
                        }
                        CardModifierManager.addModifier(c, new WashMod(amount));
                    }

                    this.isDone = true;
                } else {
                    if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(ablePile, this.numberOfCards, true, TEXT[0]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(ablePile, this.numberOfCards, TEXT[0], false);
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
                    int amount = 1;
                    List<AbstractCardModifier> wash_ = CardModifierManager.getModifiers(c, WashMod.ID);
                    if(wash_.size()>0 && wash_.get(0) instanceof WashMod) {
                        amount = ((WashMod)wash_.get(0)).amount + 1;
                        CardModifierManager.removeModifiersById(c, WashMod.ID, true);
                    }
                    CardModifierManager.addModifier(c, new WashMod(amount));
                    c.lighten(false);
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:CarWashAction");
        TEXT = uiStrings.TEXT;
    }
}