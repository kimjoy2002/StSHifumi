package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cardmods.NonCollectMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class AddModiferAction extends AbstractGameAction {
    AbstractCard c;
    AbstractCardModifier m;

    public AddModiferAction(AbstractCard c, AbstractCardModifier m) {
        this.actionType = ActionType.WAIT;
        this.c = c;
        this.m = m;
    }

    public void update() {
        CardModifierManager.addModifier(c, m);
        this.isDone = true;
    }
}