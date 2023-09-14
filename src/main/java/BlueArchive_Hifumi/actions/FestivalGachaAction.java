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
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.UNSPECIFIED;

public class FestivalGachaAction extends AbstractGameAction {
    private AbstractPlayer player;
    public FestivalGachaAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }


    public void update() {
        CardGroup reverse_ = new CardGroup(UNSPECIFIED);
        for(AbstractCard card : this.player.discardPile.group) {
            reverse_.addToTop(card);
        }
        for(AbstractCard card : reverse_.group) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card.makeStatEquivalentCopy()));
        }

        this.isDone = true;
    }
}