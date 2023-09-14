package BlueArchive_Hifumi.events;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.*;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NlothsGift;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.*;

import static BlueArchive_Hifumi.DefaultMod.makeEventPath;

public class SpotCheckEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeID("SpotCheckEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("SpotCheckEvent.png");


    private int gold_cost;
    private int screenNum = 0;
    private AbstractRelic choice1;
    public SpotCheckEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        if (AbstractDungeon.ascensionLevel >= 15) {
            gold_cost = 100;
        } else {
            gold_cost = 75;
        }

        if (AbstractDungeon.player.gold >= gold_cost) {
            this.imageEventText.setDialogOption(OPTIONS[0] + gold_cost + OPTIONS[1], AbstractDungeon.player.gold < gold_cost, new Wanted());
        } else {
            this.imageEventText.setDialogOption(OPTIONS[2] + gold_cost + OPTIONS[3], AbstractDungeon.player.gold < gold_cost, new Wanted());
        }
        ArrayList<AbstractRelic> relics = new ArrayList();
        relics.addAll(AbstractDungeon.player.relics);
        Collections.shuffle(relics, new Random(AbstractDungeon.miscRng.randomLong()));
        if(relics.size() > 0) {
            this.choice1 = (AbstractRelic) relics.get(0);
            this.imageEventText.setDialogOption(OPTIONS[4] + this.choice1.name + OPTIONS[5], new Wanted());
        } else {
            this.imageEventText.setDialogOption(OPTIONS[4] + OPTIONS[5], true, new Wanted());
        }
        this.imageEventText.setDialogOption(OPTIONS[6]);
    }

    public void update() {
        super.update();
    }


    private void removeWanted() {
        for(int i = AbstractDungeon.player.masterDeck.group.size() - 1; i >= 0; --i) {
            if (((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).cardID == Wanted.ID && !((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).inBottleFlame && !((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).inBottleLightning) {
                AbstractDungeon.effectList.add(new PurgeCardEffect((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)));
                AbstractDungeon.player.masterDeck.removeCard((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i));
            }
        }
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0: {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.loseGold(gold_cost);
                        removeWanted();
                        AbstractEvent.logMetricLoseGold("SpotCheckEvent", "Bribe", gold_cost);
                        screenNum = 1;
                        break;
                    }
                    case 1: {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractEvent.logMetricGainGoldAndLoseRelic("SpotCheckEvent", "Bribe",this.choice1, 0);
                        removeWanted();
                        AbstractDungeon.player.loseRelic(this.choice1.relicId);
                        screenNum = 1;
                        break;
                    }
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[7]);
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

}
