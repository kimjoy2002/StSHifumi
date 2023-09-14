package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.PeroroGoods;
import BlueArchive_Hifumi.relics.peroro.CommonPeroroBlockRelic;
import BlueArchive_Hifumi.relics.peroro.CommonPeroroDamageRelic;
import BlueArchive_Hifumi.relics.peroro.CommonPeroroMultiDamageRelic;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class ObtainPeroroGoodsAction extends AbstractGameAction {

    private AbstractPlayer p;
    public ObtainPeroroGoodsAction() {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    private boolean chooseGoods() {
        ArrayList<AbstractCard> optionChoices = new ArrayList();
        optionChoices.add(new PeroroGoods(new CommonPeroroBlockRelic(), 0, -1));
        optionChoices.add(new PeroroGoods(new CommonPeroroDamageRelic(), 0, -1));
        optionChoices.add(new PeroroGoods(new CommonPeroroMultiDamageRelic(), 0, -1));

        InputHelper.moveCursorToNeutralPosition();

        this.addToBot(new ChooseOneAction(optionChoices));
        return true;
    }


    @Override
    public void update() {
        chooseGoods();
        this.isDone = true;
    }
}
