package BlueArchive_Hifumi.events;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.Wanted;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

import static BlueArchive_Hifumi.DefaultMod.makeEventPath;

public class PeroroShopEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeID("PeroroShopEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("PeroroShopEvent.png");

    private int screenNum = 0;

    private int hp_cost;
    private int card_cost;

    public PeroroShopEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        if (AbstractDungeon.ascensionLevel >= 15) {
            card_cost = 250;
            hp_cost = 12;
        } else {
            card_cost = 200;
            hp_cost = 8;
        }
        if(AbstractDungeon.player != null) {
            hp_cost = AbstractDungeon.player.maxHealth*hp_cost/100;
        } else {
            hp_cost = 80*hp_cost/100;
        }

        this.imageEventText.setDialogOption(OPTIONS[0] + hp_cost + OPTIONS[1]);
        if (AbstractDungeon.player.gold >= card_cost) {
            this.imageEventText.setDialogOption(OPTIONS[2] + card_cost + OPTIONS[3], AbstractDungeon.player.gold < card_cost);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[6] + card_cost + OPTIONS[7], AbstractDungeon.player.gold < card_cost);
        }
        imageEventText.setDialogOption(OPTIONS[4], new Wanted());
        imageEventText.setDialogOption(OPTIONS[5]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0: {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, hp_cost));
                        AbstractRelic relic = PeroroGoodsRelic.getRandomPeroroGoods(AbstractRelic.RelicTier.COMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                        logMetricObtainRelicAndDamage("PeroroShopEvent", "Part Time", relic.makeCopy(), hp_cost);
                        screenNum = 1;
                        break;
                    }
                    case 1: {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(card_cost);
                        AbstractRelic relic = PeroroGoodsRelic.getRandomPeroroGoods(AbstractRelic.RelicTier.UNCOMMON);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                        logMetricObtainRelicAtCost("PeroroShopEvent", "Buy", relic.makeCopy(), card_cost);

                        screenNum = 1;
                        break;
                    }
                    case 2: {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Wanted(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        AbstractRelic relic = PeroroGoodsRelic.getRandomPeroroGoods(AbstractRelic.RelicTier.RARE);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                        logMetricObtainCardAndRelic("PeroroShopEvent", "Robbery!", new Wanted(), relic.makeCopy());

                        screenNum = 1;
                        break;
                    }
                    case 3:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                switch (i) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }

    public void update() {
        super.update();
    }
}
