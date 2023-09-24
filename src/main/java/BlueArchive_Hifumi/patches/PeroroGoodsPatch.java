package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.powers.PeroroHologramPower;
import BlueArchive_Hifumi.powers.PerorodzillaPower;
import BlueArchive_Hifumi.powers.RenderPower;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.DiscardPilePanel;
import javassist.CtBehavior;

import java.util.ArrayList;

import static BlueArchive_Hifumi.DefaultMod.disablePeroroGoodsBar;

public class PeroroGoodsPatch {


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render",
            paramtypez = {
                    SpriteBatch.class
            }
    )
    public static class RenderPowerPatcher {

        public static void Prefix(AbstractPlayer instance, SpriteBatch sb){
            if (instance.hasPower(PerorodzillaPower.POWER_ID)) {
                ((PerorodzillaPower)instance.getPower(PerorodzillaPower.POWER_ID)).render(sb);
            }
            if (instance.hasPower(PeroroHologramPower.POWER_ID)) {
                ((PeroroHologramPower)instance.getPower(PeroroHologramPower.POWER_ID)).render(sb);
            }
        }
    }



    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderRelics",
            paramtypez = {
                    SpriteBatch.class
            }
    )
    public static class RenderRelicsPatcher {

        public static void Prefix(AbstractPlayer instance, SpriteBatch sb){
            if(!disablePeroroGoodsBar) {
                for(int i = 0; i < instance.relics.size(); ++i) {
                    if (i / AbstractRelic.MAX_RELICS_PER_PAGE != AbstractRelic.relicPage && ((AbstractRelic)instance.relics.get(i)) instanceof PeroroGoodsRelic) {
                        ((AbstractRelic)instance.relics.get(i)).renderInTopPanel(sb);
                    }
                }
            }
        }
    }



    @SpirePatch(
            clz = AbstractRelic.class,
            method = "update",
            paramtypez = {
            }
    )
    public static class RelicPagePatcher {

        public static SpireReturn Prefix(AbstractRelic instance){
            if(!disablePeroroGoodsBar) {
                if (instance instanceof PeroroGoodsRelic && instance.isDone && AbstractDungeon.player != null && AbstractDungeon.player.relics.indexOf(instance) / instance.MAX_RELICS_PER_PAGE != instance.relicPage) {

                    ReflectionHacks.RMethod method_ = ReflectionHacks.privateMethod(AbstractRelic.class, "updateFlash");
                    method_.invoke(instance);

                    instance.hb.update();

                    if (instance.hb.hovered && AbstractDungeon.topPanel.potionUi.isHidden) {
                        instance.scale = Settings.scale * 1.25F;
                        CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
                    } else {
                        instance.scale = MathHelper.scaleLerpSnap(instance.scale, Settings.scale);
                    }

                    method_ = ReflectionHacks.privateMethod(AbstractRelic.class, "updateRelicPopupClick");
                    method_.invoke(instance);
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractRelic.class,
            method = "instantObtain",
            paramtypez = {
                    AbstractPlayer.class,
                    int.class,
                    boolean.class
            }
    )
    public static class InstantObtainPatcher {

        public static SpireReturn Prefix(AbstractRelic __instance, AbstractPlayer p, int slot, boolean callOnEquip) {
            if (__instance instanceof PeroroGoodsRelic && p != null && p.hasRelic(__instance.relicId)) {
                AbstractRelic peroro = p.getRelic(__instance.relicId);
                peroro.setCounter(peroro.counter+1);
                peroro.flash();
                ((PeroroGoodsRelic)peroro).updateDescription();

                __instance.isDone = true;
                __instance.isObtained = true;
                __instance.discarded = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractRelic.class,
            method = "instantObtain",
            paramtypez = {
                    AbstractPlayer.class,
                    int.class,
                    boolean.class
            }
    )
    public static class InstantObtain2Patcher {

        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(AbstractRelic __instance, AbstractPlayer p, int slot, boolean callOnEquip) {
            if(!disablePeroroGoodsBar) {
                int peroro_count = 0;
                for (AbstractRelic relic : p.relics) {
                    if (relic == __instance)
                        break;
                    if (relic instanceof PeroroGoodsRelic)
                        peroro_count++;
                }

                if (__instance instanceof PeroroGoodsRelic) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");
                    __instance.currentX = START_X + (float) peroro_count * PAD_X;
                    __instance.currentY = __instance.currentY - 64;
                    __instance.targetX = __instance.currentX;
                    __instance.targetY = __instance.currentY;
                } else if (peroro_count > 0) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");

                    __instance.currentX = START_X + ((float) slot - peroro_count) * PAD_X;
                    __instance.targetX = __instance.currentX;
                    __instance.targetY = __instance.currentY;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "move");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractRoom.class,
            method = "spawnRelicAndObtain",
            paramtypez = {
                    float.class,
                    float.class,
                    AbstractRelic.class
            }
    )
    public static class SpawnRelicAndObtainPatcher {

        public static SpireReturn Prefix(AbstractRoom __instance, float x, float y, AbstractRelic relic) {
            if (relic instanceof PeroroGoodsRelic && AbstractDungeon.player.hasRelic(relic.relicId)) {
                AbstractRelic peroro = AbstractDungeon.player.getRelic(relic.relicId);
                peroro.setCounter(peroro.counter+1);
                peroro.flash();
                ((PeroroGoodsRelic)peroro).updateDescription();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }




    @SpirePatch(
            clz = AbstractRelic.class,
            method = "instantObtain",
            paramtypez = {
            }
    )
    public static class InstantObtain2_2Patcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractRelic __instance) {
            if(!disablePeroroGoodsBar) {
                int slot = AbstractDungeon.player.relics.size() - 1;
                int peroro_count = 0;
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic == __instance)
                        break;
                    if (relic instanceof PeroroGoodsRelic)
                        peroro_count++;
                }

                if (__instance instanceof PeroroGoodsRelic) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");
                    __instance.currentX = START_X + (float) peroro_count * PAD_X;
                    __instance.currentY = __instance.currentY - 64;
                    __instance.targetX = __instance.currentX;
                    __instance.targetY = __instance.currentY;
                } else if (peroro_count > 0) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");

                    __instance.currentX = START_X + ((float) slot - peroro_count) * PAD_X;
                    __instance.targetX = __instance.currentX;
                    __instance.targetY = __instance.currentY;
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "move");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractRelic.class,
            method = "obtain",
            paramtypez = {
            }
    )
    public static class InstantObtain2_3Patcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractRelic __instance) {
            if(!disablePeroroGoodsBar) {
                int slot = AbstractDungeon.player.relics.size() - 1;
                int peroro_count = 0;
                for (AbstractRelic relic : AbstractDungeon.player.relics) {
                    if (relic == __instance)
                        break;
                    if (relic instanceof PeroroGoodsRelic)
                        peroro_count++;
                }

                if (__instance instanceof PeroroGoodsRelic) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");
                    __instance.targetX = START_X + (float) peroro_count * PAD_X;
                    __instance.targetY = __instance.targetY - 64;
                } else if (peroro_count > 0) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");

                    __instance.targetX = START_X + ((float) slot - peroro_count) * PAD_X;
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "relicTip");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractRelic.class,
            method = "reorganizeObtain",
            paramtypez = {
                    AbstractPlayer.class,
                    int.class,
                    boolean.class,
                    int.class,
            }
    )
    public static class ReorganizeObtain2Patcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractRelic __instance, AbstractPlayer p, int slot, boolean callOnEquip, int relicAmount) {

            if(!disablePeroroGoodsBar) {
                int peroro_count = 0;
                for (AbstractRelic relic : p.relics) {
                    if (relic == __instance)
                        break;
                    if (relic instanceof PeroroGoodsRelic)
                        peroro_count++;
                }

                if (__instance instanceof PeroroGoodsRelic) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");
                    __instance.currentX = START_X + (float) peroro_count * PAD_X;
                    __instance.currentY = __instance.currentY - 64;
                    __instance.targetX = __instance.currentX;
                    __instance.targetY = __instance.currentY;
                } else if (peroro_count > 0) {
                    float START_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "START_X");
                    float PAD_X = (float) ReflectionHacks.getPrivate(__instance, AbstractRelic.class, "PAD_X");

                    __instance.currentX = START_X + ((float) slot - peroro_count) * PAD_X;
                    __instance.targetX = __instance.currentX;
                    __instance.targetY = __instance.currentY;
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "move");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}