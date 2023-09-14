package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.PeroroGoods;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class PeroroFestivalAction extends AbstractGameAction {

    private AbstractPlayer p;

    public PeroroFestivalAction() {
        this.amount = 1;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }



    @Override
    public void update() {
        CardGroup tempGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        ArrayList<AbstractCard> optionChoices = new ArrayList();
        for(AbstractRelic relic : AbstractDungeon.player.relics) {
            if(relic instanceof PeroroGoodsRelic && relic.counter > 0 && relic.grayscale == false) {
                tempGroup.addToRandomSpot(new PeroroGoods(relic, 0, 1));
            }
        }

        for(AbstractCard choice : tempGroup.group) {
            choice.onChoseThisOption();
        }

        this.isDone = true;
    }
}
