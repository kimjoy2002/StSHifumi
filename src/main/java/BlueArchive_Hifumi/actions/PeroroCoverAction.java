package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.patches.EnumPatch;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PeroroCoverAction extends AbstractGameAction {
    public PeroroCoverAction(int amount) {
        this.amount = amount;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public int getLearnedCard() {
        int count = 0;
        for(AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if(card.hasTag(EnumPatch.LEARNED)) {
                count++;
            }
        }
        return count;
    }
    @Override
    public void update() {
        int amount_ = getLearnedCard() * amount;
        if(amount_ > 0) {
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, amount_));
        }
        this.isDone = true;
    }
}
