package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.PeroroTheForbiddenOne;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class ExodiaAction extends AbstractGameAction {
    private int draw;

    public ExodiaAction(int damage, int draw) {
        this.amount = damage;
        this.draw = draw;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.DAMAGE;
    }

    public void update() {
        AbstractCard[] cardsToMove = new AbstractCard[5];

        for(int i = 0; i < 5; i++)
            cardsToMove[i] = null;


        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if(card instanceof PeroroTheForbiddenOne) {
                PeroroTheForbiddenOne pcard = (PeroroTheForbiddenOne)card;
                if(pcard.forbiddenType >= 0 && pcard.forbiddenType < 5) {
                    cardsToMove[pcard.forbiddenType] = pcard;
                }
            }
        }

        for(int i = 0; i < 5; i++) {
            if(cardsToMove[i] == null) {
                this.isDone = true;
                return;
            }
        }

        for(int i = 0; i < 5; i++) {
            this.addToTop(new ExhaustSpecificCardAction(cardsToMove[i], AbstractDungeon.player.hand, true));
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature) null, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(draw));
        this.isDone = true;
    }
}