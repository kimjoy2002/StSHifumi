package BlueArchive_Hifumi.screens;

import BlueArchive_Hifumi.cardmods.EvoveMod;
import BlueArchive_Hifumi.cardmods.LearnedMod;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.patches.ShopPatch;
import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomScreen;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import static BlueArchive_Hifumi.cardmods.EvoveMod.EvoveEnum.*;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen.COMBAT_REWARD;

public class EvoveScreen extends CustomScreen {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    protected float duration;
    public boolean isDone;
    public static int misc = 0;
    public static AbstractCard selected = null;
    public static class Enum
    {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen EVOVE_SCREEN;
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen()
    {
        return Enum.EVOVE_SCREEN;
    }

    // Note that this can be private and take any parameters you want.
    // When you call openCustomScreen it finds the first method named "open"
    // and calls it with whatever arguments were passed to it.
    private void open()
    {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE)
            AbstractDungeon.previousScreen = AbstractDungeon.screen;

        reopen();
    }

    public EvoveScreen() {
        super();
        this.duration = Settings.ACTION_DUR_MED;
    }
    @Override
    public void reopen()
    {
        AbstractDungeon.screen = curScreen();
        AbstractDungeon.isScreenUp = true;
    }

    @Override
    public void close() {

    }
    public void init() {
        this.duration = Settings.ACTION_DUR_MED;
    }

    public AbstractCard getCard() {
        for(AbstractCard card_ : AbstractDungeon.player.masterDeck.group) {
            if(card_.misc == misc) {
                return card_;
            }
        }
        return null;
    }

    public ArrayList<EvoveMod.EvoveEnum> getAbleList(AbstractCard original) {
        ArrayList<EvoveMod.EvoveEnum> list_ = new ArrayList<>();
        ArrayList<AbstractCardModifier> modList =  CardModifierManager.modifiers(original);
        int weakLevel = 0;
        int vulunLevel = 0;
        int multiLevel = 0;
        int allLevel = 0;
        int costLevel = 0;
        int canonLevel = 0;
        int exhaustLevel = 0;
        int drawLevel = 0;
        int collectLevel = 0;
        int retainLevel = 0;
        int curseLevel = 0;
        int buriedLevel = 0;


        for(AbstractCardModifier m : modList) {
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_VULUN)
                vulunLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_WEAK)
                weakLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_MULTI)
                multiLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_ALL_ENEMY)
                allLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_EXHAUST)
                exhaustLevel++;
            if(m instanceof EvoveMod && (((EvoveMod) m).type == COST_DOWN || ((EvoveMod) m).type == COST_UP || ((EvoveMod) m).type == ADD_CANNON ))
                costLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_CANNON)
                canonLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_DRAW)
                drawLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_COLLECT)
                collectLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_RETAIN)
                retainLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_CURSE)
                curseLevel++;
            if(m instanceof EvoveMod && ((EvoveMod) m).type == ADD_BURIED)
                buriedLevel++;
        }

        for(EvoveMod.EvoveEnum e : EvoveMod.EvoveEnum.values()) {
            switch(e) {
                case ADD_DAMAGE:
                    break;//always
                case ADD_BLOCK:
                    list_.add(e);
                    break;
                case ADD_VULUN:
                case ADD_WEAK:
                    if(!(vulunLevel > 1 && weakLevel == 0 && e == ADD_WEAK) &&
                       !(vulunLevel == 0 && weakLevel > 1 && e == ADD_VULUN)) {
                        list_.add(e);
                    }
                    break;
                case ADD_MULTI:
                    if(allLevel == 0 && multiLevel <= 1) {
                        list_.add(e);
                    }
                    break;
                case COST_DOWN:
                case COST_UP:
                    if(costLevel == 0) {
                        list_.add(e);
                    }
                    break;
                case ADD_CANNON:
                    if(costLevel == 0 && collectLevel == 0 && drawLevel == 0 && retainLevel == 0) {
                        list_.add(e);
                    }
                    break;
                case ADD_ALL_ENEMY:
                    if(allLevel == 0 && multiLevel == 0) {
                        list_.add(e);
                    }
                    break;
                case ADD_EXHAUST:
                    if(exhaustLevel == 0) {
                        list_.add(e);
                    }
                    break;
                case ADD_DRAW:
                    if(collectLevel == 0 && canonLevel == 0) {
                        list_.add(e);
                    }
                    break;
                case ADD_COLLECT:
                    if(drawLevel == 0 && canonLevel == 0) {
                        list_.add(e);
                    }
                    break;
                case ADD_RETAIN:
                    if(retainLevel == 0 && canonLevel == 0) {
                        list_.add(e);
                    }
                    break;
                case ADD_CURSE:
                    if(curseLevel <= 1) {
                        list_.add(e);
                    }
                    break;
                case ADD_BURIED:
                    if(buriedLevel == 0) {
                        list_.add(e);
                    }
                    break;
            }

        }
        return list_;
    }


    @Override
    public void update() {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            AbstractCard original = getCard();

            if(original == null) {
                this.isDone = true;
                return;
            }
            ArrayList<AbstractCard> choices = new ArrayList<>();
            ArrayList<EvoveMod.EvoveEnum> ableMods = getAbleList(original);

            Collections.shuffle(ableMods, new java.util.Random(AbstractDungeon.cardRng.randomLong()));

            for(int i = 0; i < 3; i ++)  {
                EvoveMod.EvoveEnum enums;
                if(i == 0) {
                    enums = ADD_DAMAGE;
                } else {
                    enums = ableMods.get(ableMods.size()-1);
                    ableMods.remove(ableMods.size()-1);
                }
                AbstractCard copy =  original.makeStatEquivalentCopy();
                copy.baseMagicNumber = copy.baseMagicNumber-1;
                copy.magicNumber = copy.magicNumber-1;
                CardModifierManager.addModifier(copy, new EvoveMod(enums));
                choices.add(copy);
            }
            Collections.shuffle(choices, new java.util.Random(AbstractDungeon.cardRng.randomLong()));

            AbstractDungeon.cardRewardScreen.chooseOneOpen(choices);

            ShopPatch.CardRewardFieldPatcher.forEvove.set(AbstractDungeon.cardRewardScreen, true);
            AbstractDungeon.previousScreen = Enum.EVOVE_SCREEN;
            this.tickDuration();
            this.isDone = false;
        } else {
            if (selected != null) {
                AbstractCard original = getCard();

                if(original == null) {
                    this.isDone = true;
                    return;
                }

                ArrayList<AbstractCardModifier> modList =  CardModifierManager.modifiers(selected);
                AbstractCardModifier mod = null;
                int i = modList.size()-1;
                while(i>=0 &&(mod == null || !(mod instanceof EvoveMod)) ) {
                    mod = modList.get(i--);
                }

                if(mod != null && mod instanceof EvoveMod) {
                    original.magicNumber--;
                    original.baseMagicNumber--;
                    CardModifierManager.addModifier(original, mod);

                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(original.makeStatEquivalentCopy()));
                }

                selected = null;
            }

            this.tickDuration();
        }
        if(isDone) {
            AbstractDungeon.previousScreen = COMBAT_REWARD;
            this.duration = Settings.ACTION_DUR_MED;
            AbstractDungeon.closeCurrentScreen();
        }

    }

    protected void tickDuration() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }


    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void openingSettings()
    {
        // Required if you want to reopen your screen when the settings screen closes
        AbstractDungeon.previousScreen = curScreen();
    }
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:AddLearnedAction");
        TEXT = uiStrings.TEXT;
    }
}