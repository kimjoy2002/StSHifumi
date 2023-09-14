package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.powers.IncineratorPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class IncineratorAction extends AbstractGameAction {

    public IncineratorAction(int amount) {
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.DAMAGE;
    }

    public void update() {
        int count = 0;
        for (AbstractCard c: AbstractDungeon.player.discardPile.group) {
            if(c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS) {
                count++;
            }
        }
        if(count > 0) {
            if(AbstractDungeon.player.hasRelic(IncineratorPower.POWER_ID)) {
                AbstractDungeon.player.getRelic(IncineratorPower.POWER_ID).flash();
            }
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature) null, DamageInfo.createDamageMatrix(count * amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
        }
        this.isDone = true;
    }
}