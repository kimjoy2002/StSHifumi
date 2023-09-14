package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cardmods.CostMod;
import BlueArchive_Hifumi.cards.LuckyPunch;
import BlueArchive_Hifumi.patches.LuckyPunchPatch;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class LuckyPunchAction extends AbstractGameAction {
    AbstractCard card;
    public LuckyPunchAction(AbstractCard card) {
        this.amount = amount;
        this.card = card;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {

        for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid == card.uuid && c instanceof LuckyPunch) {
                c.misc = 1;
                CardModifierManager.removeModifiersById(c, CostMod.ID, true);
                c.initializeDescription();
            }
        }
        this.isDone = true;
    }
}
