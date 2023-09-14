package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.Reisa;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class RuinConstructionAction extends AbstractGameAction {
    private float startingDuration;
    int[] multiDamage;
    DamageInfo.DamageType type;

    public RuinConstructionAction(int[] multiDamage, DamageInfo.DamageType type) {
        this.multiDamage = multiDamage;
        this.type = type;
        this.actionType = ActionType.WAIT;
        this.attackEffect = AttackEffect.FIRE;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update() {
        CardGroup group_ = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if(card.hasTag(EnumPatch.BURIED)) {
                group_.addToTop(card);
            }
        }

        int count = group_.size();

        int i;
        for(i = 0; i < count; ++i) {
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction((AbstractCreature) null, multiDamage, type, AbstractGameAction.AttackEffect.FIRE, true));
        }

        for(AbstractCard card : group_.group) {
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
            this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile, true));
        }

        this.isDone = true;
    }
}

