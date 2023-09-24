package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.CollectCard;
import BlueArchive_Hifumi.cards.TeaTime;
import BlueArchive_Hifumi.powers.ItazuraStraightPower;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.DiscardPilePanel;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static BlueArchive_Hifumi.patches.GameActionManagerPatch.collectedCardSet;
import static BlueArchive_Hifumi.patches.PlayerPatch.hasPerfectCollecter;
import static java.lang.Math.min;

public class CollectUIPatch {

    private static final Logger logger = LogManager.getLogger(CollectUIPatch.class.getName());


    @SpirePatch(
            clz = DiscardPilePanel.class,
            method = "renderButton",
            paramtypez= {
                SpriteBatch.class
            }
    )
    public static class addToHandPatcher {


        static ArrayList<AbstractCard> expectCards = new ArrayList<AbstractCard>();
        static AbstractCard prevRenderCard = null;
        public static void createExpectCards(DiscardPilePanel __instance, int count, boolean teatime) {
            CardGroup group_;

            if(teatime) {
                group_ = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c: AbstractDungeon.player.discardPile.group) {
                    if((c.hasTag(EnumPatch.BURIED) || c.hasTag(EnumPatch.LEARNED) || GraveField.grave.get(c)) && !c.hasTag(EnumPatch.NONCOLLECTABLE)) {
                        group_.addToTop(c);
                    }
                }
            } else {
                group_ = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c: AbstractDungeon.player.discardPile.group) {
                    if(!c.hasTag(EnumPatch.NONCOLLECTABLE)) {
                        group_.addToTop(c);
                    }
                }
            }

            int size = group_.group.size();
            float x_ = __instance.current_x + 180.0F * Settings.scale - 64.0F + DefaultMod.collectOffsetX* Settings.scale;
            float y_ = __instance.current_y + 70.0F * Settings.scale - 64.0F + DefaultMod.collectOffsetY* Settings.scale;
            x_ += 64.0F * Settings.scale;
            y_ += 256.0F * Settings.scale;
            float interval_ = 100.0F * Settings.scale;
            int amount = min(size-1, count-1);
            y_ += amount*interval_;

            for(int i = amount; i >= 0; i--) {
                AbstractCard expectCard = group_.group.get(i).makeStatEquivalentCopy();
                expectCard.current_x = x_;
                expectCard.current_y = y_;
                expectCard.drawScale = 0.5f;
                //for(AbstractCardModifier m : CardModifierManager.modifiers(group_.group.get(i))) {
                //    CardModifierManager.addModifier(expectCard, m);
                //}

                y_ -= interval_;
                expectCards.add(expectCard);
            }
        }

        public static void Postfix(DiscardPilePanel __instance, SpriteBatch sb) {
            boolean render = false;
            for(AbstractCard c : AbstractDungeon.player.hand.group) {
                if(c instanceof CollectCard) {
                    CollectCard collectCard = (CollectCard) c;
                    if(collectCard.expectCollect() > 0) {
                        Boolean hovered = (Boolean) ReflectionHacks.getPrivate(c, AbstractCard.class, "hovered");
                        if(hovered) {
                            if(c != prevRenderCard) {
                                createExpectCards(__instance, collectCard.expectCollect(), c instanceof TeaTime);
                            }

                            for (AbstractCard expectCard : expectCards) {
                                expectCard.render(sb);
                            }

                            render = true;
                            prevRenderCard = c;
                            break;
                        }
                    }

                }
            }
            if(!render) {
                expectCards.clear();
                prevRenderCard = null;
            }
        }
    }


    private static Texture collected = new Texture("BlueArchive_HifumiResources/images/512/collected.png");


    private static Texture cannon = new Texture("BlueArchive_HifumiResources/images/512/cannon.png");

    private static Texture itazura = new Texture("BlueArchive_HifumiResources/images/512/itazura.png");


    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCard",
            paramtypez = {SpriteBatch.class, boolean.class, boolean.class}
    )
    public static class collectedCardPatcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
            if(__instance != null) {
                if (hasPerfectCollecter && collectedCardSet.contains(__instance.cardID) && !__instance.isLocked && __instance.isSeen) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                    float w = collected.getWidth();
                    float h = collected.getHeight();
                    sb.draw(collected, __instance.current_x-256, __instance.current_y-256, w/2.0f, h/2.0f, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);

                }
                if (__instance.hasTag(EnumPatch.CANNON) && AbstractDungeon.player != null && AbstractDungeon.player.hand != null && AbstractDungeon.player.hand.contains(__instance) && !__instance.isLocked && __instance.isSeen) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                    float w = cannon.getWidth();
                    float h = cannon.getHeight();
                    sb.draw(cannon, __instance.current_x-256, __instance.current_y-256, w/2.0f, h/2.0f, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);
                }
                if(AbstractDungeon.player != null) {
                    for(AbstractPower powerTemp : AbstractDungeon.player.powers) {
                        if(powerTemp instanceof ItazuraStraightPower) {
                            ItazuraStraightPower power_ = (ItazuraStraightPower) powerTemp;

                            if(__instance ==  power_.card && !__instance.isLocked && __instance.isSeen) {
                                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                                float w = itazura.getWidth();
                                float h = itazura.getHeight();
                                sb.draw(itazura, __instance.current_x-256, __instance.current_y-256, w/2.0f, h/2.0f, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);
                            }
                        }

                    }

                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderEnergy");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
