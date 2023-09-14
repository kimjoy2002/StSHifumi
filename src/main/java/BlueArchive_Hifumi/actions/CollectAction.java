package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.CollectCard;
import BlueArchive_Hifumi.cards.PerfectCollector;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.powers.CollectPower;
import BlueArchive_Hifumi.powers.PerorodzillaPower;
import BlueArchive_Hifumi.relics.CollectRelic;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

import static BlueArchive_Hifumi.cards.SealedStorage.PER_BLOCK;
import static BlueArchive_Hifumi.patches.GameActionManagerPatch.collectedCardSet;
import static java.lang.Math.max;

public class CollectAction extends AbstractGameAction {
    private AbstractPlayer p;

    private int count = 0;
    private int originalAmount = 0;
    private int remainDraw = 0;
    private boolean toDeck;
    private boolean upgrade;

    public CollectAction(int amount, boolean toDeck, boolean upgrade) {
        this.amount = amount;
        this.originalAmount = amount;
        this.toDeck = toDeck;
        this.upgrade = upgrade;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.DRAW;
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FASTER;
        }
    }
    public CollectAction(int amount, boolean toDeck) {
        this(amount, toDeck, false);
    }

    static public void afterCollect(AbstractCard c, boolean upgrade) {
        if(upgrade) {
            if (c.canUpgrade() && c.type != AbstractCard.CardType.STATUS) {
                c.upgrade();
                c.superFlash();
                c.applyPowers();
            }
        }

        collectedCardSet.add(c.cardID);
        if (c instanceof CollectCard) {
            ((CollectCard) c).triggerWhenCollect();
        }

        Iterator var4 = AbstractDungeon.player.powers.iterator();
        while (var4.hasNext()) {
            AbstractPower p = (AbstractPower) var4.next();
            if (p instanceof CollectPower) {
                ((CollectPower) p).onCardCollect(c);
            }
        }

        var4 = AbstractDungeon.player.relics.iterator();
        while (var4.hasNext()) {
            AbstractRelic r = (AbstractRelic) var4.next();
            if (r instanceof CollectRelic) {
                ((CollectRelic) r).onCardCollect(c);
            }
        }

        for(AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if(card instanceof PerfectCollector) {
                ((PerfectCollector)card).makeDescription();
            }
        }
        for(AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if(card instanceof PerfectCollector) {
                ((PerfectCollector)card).makeDescription();
            }
        }
    }
    @Override
    public void update() {
        int deckSize = AbstractDungeon.player.drawPile.size();

        CardGroup collectableGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c: AbstractDungeon.player.discardPile.group) {
            if(!c.hasTag(EnumPatch.NONCOLLECTABLE)) {
                collectableGroup.addToTop(c);
            }
        }

        int discardSize = collectableGroup.size();

        if (!SoulGroup.isActive()) {
            if(toDeck) {
                if (discardSize == 0) {
                    this.isDone = true;
                    return;
                }
                if (amount > discardSize) {
                    amount = discardSize;
                }

                if (amount < 1) {
                    this.isDone = true;
                    return;
                }

            } else {
                if (deckSize + discardSize == 0) {
                    this.isDone = true;
                    return;
                }

                if (AbstractDungeon.player.hand.size() + amount > 10) {
                    AbstractDungeon.player.createHandIsFullDialog();
                    amount = max(0, (10 - AbstractDungeon.player.hand.size()));
                }

                if (amount < 1) {
                    this.isDone = true;
                    return;
                }

                if (amount > discardSize) {
                    remainDraw += amount - discardSize;
                    amount = discardSize;
                }

            }

            duration -= Gdx.graphics.getDeltaTime();
            if (amount != 0 && duration < 0.0F) {
                if (Settings.FAST_MODE) {
                    this.duration = Settings.ACTION_DUR_XFAST;
                } else {
                    this.duration = Settings.ACTION_DUR_FASTER;
                }

                --this.amount;
                count++;
                AbstractCard c = collectableGroup.getBottomCard();
                c.current_x = CardGroup.DISCARD_PILE_X;
                c.current_y = CardGroup.DISCARD_PILE_Y;
                c.setAngle(0.0F, true);
                c.lighten(false);
                c.drawScale = 0.12F;
                c.targetDrawScale = 0.75F;

                if(toDeck) {
                    AbstractDungeon.player.drawPile.addToBottom(c);
                } else {
                    AbstractDungeon.player.hand.addToHand(c);
                }
                AbstractDungeon.player.discardPile.removeCard(c);
                afterCollect(c, upgrade);

                AbstractDungeon.player.hand.refreshHandLayout();
            }

            if(amount == 0){
                if(remainDraw > 0 && !toDeck) {
                    this.addToTop(new CollectDrawAction(p, remainDraw, upgrade));
                }
                if(toDeck) {
                    AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, p, PER_BLOCK*count));
                }


                Iterator var4 = AbstractDungeon.player.powers.iterator();
                while (var4.hasNext()) {
                   AbstractPower p = (AbstractPower) var4.next();
                   if (p instanceof CollectPower) {
                       ((CollectPower) p).triggerWhenCollect(originalAmount);
                   }
                }
                this.isDone = true;
            }
        }
    }
}
