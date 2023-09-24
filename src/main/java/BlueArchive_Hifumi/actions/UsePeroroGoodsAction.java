package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.PeroroGoods;
import BlueArchive_Hifumi.relics.TwinPeroroRelic;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import BlueArchive_Hifumi.relics.peroro.UncommonPeroroBlockRelic;
import basemod.devcommands.relic.RelicList;
import basemod.devcommands.relic.RelicPool;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static java.lang.Math.max;

public class UsePeroroGoodsAction extends AbstractGameAction {

    private AbstractPlayer p;
    boolean isRandom;
    int add;
    boolean all = false;
    int plus_use = 0;
    AbstractRelic.RelicTier tier;

    AbstractRelic specificRelic;

    public UsePeroroGoodsAction(boolean random) {
        this(AbstractRelic.RelicTier.SPECIAL, 1, random, 0);
    }
    public UsePeroroGoodsAction(int amount, boolean random) {
        this(AbstractRelic.RelicTier.SPECIAL, amount, random, 0);
    }
    public UsePeroroGoodsAction(int amount) {
        this(AbstractRelic.RelicTier.SPECIAL, amount, false, 0);
    }
    public UsePeroroGoodsAction(AbstractRelic.RelicTier tier, int amount, boolean random, int add) {
        this.amount = amount;
        this.isRandom = random;
        this.tier = tier;
        this.add = add;
        this.specificRelic = null;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public UsePeroroGoodsAction(AbstractRelic.RelicTier tier, int amount, boolean random, int add, int plus_use) {
        this.amount = amount;
        this.isRandom = random;
        this.tier = tier;
        this.add = add;
        this.specificRelic = null;
        this.all = true;
        this.plus_use = plus_use;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public UsePeroroGoodsAction(AbstractRelic specificRelic, int add) {
        this.amount = 1;
        this.isRandom = false;
        this.specificRelic = specificRelic;
        this.tier = AbstractRelic.RelicTier.SPECIAL;
        this.add = add;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    private boolean chooseGoods() {
        ArrayList<AbstractCard> optionChoices = new ArrayList();
        if(all) {
            for (AbstractRelic relic : PeroroGoodsRelic.getRandomPeroroGoodList()) {
                if(tier == AbstractRelic.RelicTier.SPECIAL || relic.tier == tier) {
                    if(AbstractDungeon.player.hasRelic(relic.relicId)) {
                        optionChoices.add(new PeroroGoods(AbstractDungeon.player.getRelic(relic.relicId), add, amount, plus_use));
                    } else {
                        ((PeroroGoodsRelic)relic).setTemp();
                        optionChoices.add(new PeroroGoods(relic, add, amount, plus_use));
                    }
                }
            }
        } else {
            for(AbstractRelic relic : AbstractDungeon.player.relics) {
                if(relic instanceof PeroroGoodsRelic && (!(relic instanceof TwinPeroroRelic) || add == 0) && (relic instanceof TwinPeroroRelic || relic.counter > 0) && relic.grayscale == false) {
                    if(tier == AbstractRelic.RelicTier.SPECIAL || relic.tier == tier) {
                        optionChoices.add(new PeroroGoods(relic, add, amount));
                    }
                }
            }
        }

        if(optionChoices.size() == 1) {
            optionChoices.get(0).onChoseThisOption();
            return true;
        }

        if(optionChoices.isEmpty())
            return false;
        InputHelper.moveCursorToNeutralPosition();

        this.addToBot(new ChooseOneAction(optionChoices));
        return true;
    }




    @Override
    public void update() {
        if(specificRelic != null) {
            specificRelic.flash();
            if(add > 0) {
                ((PeroroGoodsRelic)specificRelic).addTempCounter(add);
            }

            ((PeroroGoodsRelic)specificRelic).use();

            if(add == -1) {
                specificRelic.grayscale = true;
                TwinPeroroRelic.checkTwinPeroro();
            }
            this.isDone = true;
            return;
        }
        else if(isRandom) {
            ArrayList<AbstractRelic> randomRelics = new ArrayList<AbstractRelic>();
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if(relic instanceof PeroroGoodsRelic && (!(relic instanceof TwinPeroroRelic) || add == 0) && (relic instanceof TwinPeroroRelic || relic.counter > 0)  && relic.grayscale == false) {
                    if(tier == AbstractRelic.RelicTier.SPECIAL || relic.tier == tier) {
                        randomRelics.add(relic);
                    }
                }
            }
            if(randomRelics.isEmpty()) {
                this.isDone = true;
                return;
            }

            if(amount == -1){
                AbstractRelic pRelic = randomRelics.get(AbstractDungeon.cardRandomRng.random(randomRelics.size() - 1));
                pRelic.flash();
                if(add > 0) {
                    ((PeroroGoodsRelic)pRelic).addTempCounter(add);
                }

                if(add == -1) {
                    pRelic.grayscale = true;
                    TwinPeroroRelic.checkTwinPeroro();
                }
            } else {

                for(int i = 0; i < amount; i++) {
                    AbstractRelic pRelic = randomRelics.get(AbstractDungeon.cardRandomRng.random(randomRelics.size() - 1));
                    pRelic.flash();
                    if(add > 0) {
                        ((PeroroGoodsRelic)pRelic).addTempCounter(add);
                    }

                    ((PeroroGoodsRelic)pRelic).use();

                    if(add == -1) {
                        pRelic.grayscale = true;
                        TwinPeroroRelic.checkTwinPeroro();
                    }
                }
            }

        }
        else {
            chooseGoods();
        }
        this.isDone = true;
    }
}
