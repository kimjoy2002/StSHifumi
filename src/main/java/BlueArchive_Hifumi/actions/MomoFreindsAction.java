package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.PeroroGoods;
import BlueArchive_Hifumi.relics.TwinPeroroRelic;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class MomoFreindsAction extends AbstractGameAction {
    public MomoFreindsAction() {
        this.amount = amount;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        this.isDone = true;
        ArrayList<AbstractCard> optionChoices = new ArrayList();
        for(AbstractRelic relic : AbstractDungeon.player.relics) {
            if(relic instanceof PeroroGoodsRelic && (relic instanceof TwinPeroroRelic || relic.counter > 0) && relic.grayscale == false) {
                optionChoices.add(new PeroroGoods(relic, 0, -2));
            }
        }

        if(optionChoices.size() == 1) {
            optionChoices.get(0).onChoseThisOption();
            return;
        }

        if(optionChoices.isEmpty())
            return;
        InputHelper.moveCursorToNeutralPosition();

        this.addToBot(new ChooseOneAction(optionChoices));
    }
}
