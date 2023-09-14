package BlueArchive_Hifumi.events;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.*;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static BlueArchive_Hifumi.DefaultMod.makeEventPath;

public class SupplementaryLessonsDepartmentEvent extends AbstractImageEvent {

    public static final String ID = DefaultMod.makeID("SupplementaryLessonsDepartmentEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("SupplementaryLessonsDepartmentEvent.png");
    public static final String IMG2 = makeEventPath("SupplementaryLessonsDepartmentEvent2.png");

    ArrayList<AbstractCard> previewCards1 = new ArrayList();
    ArrayList<AbstractCard> previewCards2 = new ArrayList();
    int count = 0;
    int previewCards1Count = 0;
    int previewCards2Count = 0;
    boolean endSelect = false;

    private int screenNum = 0;
    public SupplementaryLessonsDepartmentEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.imageEventText.setDialogOption(OPTIONS[0], new HolyHandGrenade());
        previewCards1.add(new HolyHandGrenade());
        previewCards1.add(new CastOff());
        previewCards1.add(new SagittaMortis());
        previewCards1.add(new Doubt());
        AbstractCard c = new MaskedSwinsuitGang();
        c.upgrade();
        this.imageEventText.setDialogOption(OPTIONS[1], c);
        previewCards2.add(c.makeStatEquivalentCopy());
        previewCards2.add(new Wanted());
        this.imageEventText.setDialogOption(OPTIONS[2], new FunnyFriendFantasy());
        this.imageEventText.setDialogOption(OPTIONS[3]);
    }

    public void update() {
        super.update();
        count++;
        if(!endSelect && count >= 100) {
            if(this.imageEventText.optionList.size() == 4) {
                if(previewCards1.size() > 0) {
                    previewCards1Count++;
                    if(previewCards1Count >= previewCards1.size())
                        previewCards1Count = 0;
                    LargeDialogOptionButton button_ = this.imageEventText.optionList.get(0);
                    ReflectionHacks.setPrivate(button_, LargeDialogOptionButton.class, "cardToPreview", previewCards1.get(previewCards1Count));
                }
                if(previewCards2.size() > 0) {
                    previewCards2Count++;
                    if(previewCards2Count >= previewCards2.size())
                        previewCards2Count = 0;
                    LargeDialogOptionButton button_ = this.imageEventText.optionList.get(1);
                    ReflectionHacks.setPrivate(button_, LargeDialogOptionButton.class, "cardToPreview", previewCards2.get(previewCards2Count));
                }
            }
            count = 0;
        }

    }


    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0: {
                        endSelect = true;
                        this.imageEventText.loadImage(IMG2);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractCard card = null;
                        Collections.shuffle(previewCards1, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
                        while (previewCards1.size() > 0) {
                            card = previewCards1.remove(0);

                            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();
                            while (var2.hasNext()) {
                                AbstractCard c = (AbstractCard) var2.next();
                                if (c.cardID == card.cardID) {
                                    card = null;
                                    break;
                                }
                            }
                            if(card != null && card.cardID == Doubt.ID) {
                                card = null;
                            }
                            if(card != null) {
                                previewCards1.clear();
                            }
                        }

                        ArrayList<String> obtainCards = new ArrayList<>();
                        if(card != null) {
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                            obtainCards.add(card.cardID);
                        }
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Doubt(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        obtainCards.add(Doubt.ID);
                        logMetricObtainCards("PeroroShopEvent", "Accept", obtainCards);
                        screenNum = 1;
                        break;
                    }
                    case 1: {
                        endSelect = true;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        ArrayList<String> obtainCards = new ArrayList<>();
                        AbstractCard c = new MaskedSwinsuitGang();
                        c.upgrade();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        obtainCards.add(MaskedSwinsuitGang.ID);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Wanted(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        obtainCards.add(Wanted.ID);
                        logMetricObtainCards("PeroroShopEvent", "Confess", obtainCards);

                        screenNum = 1;
                        break;
                    }
                    case 2: {
                        endSelect = true;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new FunnyFriendFantasy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        logMetricObtainCard("PeroroShopEvent", "Taunt", new FunnyFriendFantasy());
                        screenNum = 1;
                        break;
                    }
                    case 3:
                        endSelect = true;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
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
