package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.patches.EnumPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.FtueTip;

import java.util.Iterator;

public class TeaTimeAction extends AbstractGameAction {
    private boolean shuffled = false;
    private boolean vfxDone = false;
    private int count = 0;

    public TeaTimeAction() {
        this.setValues((AbstractCreature)null, (AbstractCreature)null, 0);
        this.actionType = ActionType.SHUFFLE;

        Iterator var1 = AbstractDungeon.player.relics.iterator();

        while(var1.hasNext()) {
            AbstractRelic r = (AbstractRelic)var1.next();
            r.onShuffle();
        }

    }

    public void update() {
        if (!this.shuffled) {
            this.shuffled = true;
            //AbstractDungeon.player.discardPile.shuffle(AbstractDungeon.shuffleRng);
            amount = 0;
        }

        if (!this.vfxDone) {
            Iterator<AbstractCard> c = AbstractDungeon.player.discardPile.group.iterator();
            for(int i = 0; i < amount; i++) {
                if(c.hasNext()){
                    c.next();
                }
            }


            if (c.hasNext()) {
                AbstractCard e = (AbstractCard)c.next();
                if(e.hasTag(EnumPatch.BURIED) || e.hasTag(EnumPatch.LEARNED)) {
                    amount++;
                    return;
                }
                ++this.count;

                c.remove();
                if (this.count < 11) {
                    AbstractDungeon.getCurrRoom().souls.shuffle(e, false);
                } else {
                    AbstractDungeon.getCurrRoom().souls.shuffle(e, true);
                }

                return;
            }
            this.vfxDone = true;
        }

        AbstractDungeon.player.drawPile.shuffle(AbstractDungeon.shuffleRng);
        this.isDone = true;
    }
}