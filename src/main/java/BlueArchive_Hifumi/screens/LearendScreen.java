package BlueArchive_Hifumi.screens;

import BlueArchive_Hifumi.cardmods.BuriedMod;
import BlueArchive_Hifumi.cardmods.LearnedMod;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.patches.ShopPatch;
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

import java.util.Iterator;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen.CARD_REWARD;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen.COMBAT_REWARD;

public class LearendScreen extends CustomScreen {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    protected float duration;
    public boolean isDone;
    public static class Enum
    {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen LEAREND_SCREEN;
    }

    public void init() {
        this.duration = Settings.ACTION_DUR_MED;
    }
    @Override
    public AbstractDungeon.CurrentScreen curScreen()
    {
        return Enum.LEAREND_SCREEN;
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

    public LearendScreen() {
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

    @Override
    public void update() {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var5 = AbstractDungeon.player.masterDeck.group.iterator();

            while(var5.hasNext()) {
                AbstractCard c = (AbstractCard)var5.next();

                if (!c.hasTag(EnumPatch.LEARNED)) {
                    tmp.addToBottom(c);
                }
            }

            if (tmp.size() == 0) {
                this.isDone = true;
            } else if (tmp.size() == 1) {
                card = tmp.getTopCard();

                CardModifierManager.addModifier(card, new LearnedMod());
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));

                this.isDone = true;
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, 1, TEXT[2], false);
                ShopPatch.GridFieldPatcher.forLearned.set(AbstractDungeon.gridSelectScreen, true);
                AbstractDungeon.previousScreen = Enum.LEAREND_SCREEN;
                this.tickDuration();
                this.isDone = false;
            }
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    card = (AbstractCard) var1.next();
                    CardModifierManager.addModifier(card, new LearnedMod());
                    AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
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