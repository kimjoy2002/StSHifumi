package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.screens.EvoveScreen;
import BlueArchive_Hifumi.screens.LearendScreen;
import BlueArchive_Hifumi.shop.GachaShop;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.overlayMenu;
import static java.lang.Math.min;

public class ShopPatch {

    private static final Logger logger = LogManager.getLogger(ShopPatch.class.getName());




    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method=SpirePatch.CLASS
    )
    public static class GridFieldPatcher {
        public static SpireField<Boolean> forLearned = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method=SpirePatch.CLASS
    )
    public static class CardRewardFieldPatcher {
        public static SpireField<Boolean> forEvove = new SpireField<>(() -> false);
    }


    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "callOnOpen",
            paramtypez= {
            }
    )
    public static class CallOnOpenPatcher {
        public static void Prefix(GridCardSelectScreen __instance) {
            GridFieldPatcher.forLearned.set(__instance, false);
        }
    }


    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "open",
            paramtypez= {
                    ArrayList.class,
                    RewardItem.class,
                    String.class
            }
    )
    public static class CardRewardOpenPatcher {
        public static void Prefix(CardRewardScreen __instance,ArrayList _1, RewardItem _2, String _3) {
            CardRewardFieldPatcher.forEvove.set(__instance, false);
        }
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "customCombatOpen",
            paramtypez= {
                    ArrayList.class,
                    String.class,
                    boolean.class,
            }
    )
    public static class CardRewardOpen2Patcher {
        public static void Prefix(CardRewardScreen __instance,ArrayList _1, String _2, boolean _3) {
            CardRewardFieldPatcher.forEvove.set(__instance, false);
        }
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "chooseOneOpen",
            paramtypez= {
                    ArrayList.class
            }
    )
    public static class CardRewardOpen3Patcher {
        public static void Prefix(CardRewardScreen __instance,ArrayList _1) {
            CardRewardFieldPatcher.forEvove.set(__instance, false);
        }
    }
    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "draftOpen",
            paramtypez= {
            }
    )
    public static class CardRewardOpen4Patcher {
        public static void Prefix(CardRewardScreen __instance) {
            CardRewardFieldPatcher.forEvove.set(__instance, false);
        }
    }
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "closeCurrentScreen",
            paramtypez= {
            }
    )
    public static class ShopClosePatcher {
        public static SpireReturn Prefix() {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID && GridFieldPatcher.forLearned.get(AbstractDungeon.gridSelectScreen)){
                PeekButton.isPeeking = false;
                AbstractDungeon.previousScreen = LearendScreen.Enum.LEAREND_SCREEN;
                ReflectionHacks.RStaticMethod method_ = ReflectionHacks.privateStaticMethod(AbstractDungeon.class, "genericScreenOverlayReset");
                method_.invoke();

                AbstractDungeon.screen = AbstractDungeon.previousScreen;
                AbstractDungeon.previousScreen = null;
                AbstractDungeon.isScreenUp = true;
                ReflectionHacks.RMethod method2_ = ReflectionHacks.privateStaticMethod(AbstractDungeon.class, "openPreviousScreen", AbstractDungeon.CurrentScreen.class);
                method2_.invoke( (Object) null, AbstractDungeon.screen);

                return SpireReturn.Return();
            }

            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD && CardRewardFieldPatcher.forEvove.get(AbstractDungeon.cardRewardScreen)){
                PeekButton.isPeeking = false;
                AbstractDungeon.previousScreen = EvoveScreen.Enum.EVOVE_SCREEN;

                overlayMenu.cancelButton.hide();
                AbstractDungeon.dynamicBanner.hide();
                ReflectionHacks.RStaticMethod method_ = ReflectionHacks.privateStaticMethod(AbstractDungeon.class, "genericScreenOverlayReset");
                method_.invoke();
                if (!AbstractDungeon.screenSwap) {
                    AbstractDungeon.cardRewardScreen.onClose();
                }
                AbstractDungeon.screen = AbstractDungeon.previousScreen;
                AbstractDungeon.previousScreen = null;
                AbstractDungeon.isScreenUp = true;
                ReflectionHacks.RMethod method2_ = ReflectionHacks.privateStaticMethod(AbstractDungeon.class, "openPreviousScreen", AbstractDungeon.CurrentScreen.class);
                method2_.invoke( (Object) null, AbstractDungeon.screen);

                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }
    @SpirePatch(
            clz = ShopRoom.class,
            method=SpirePatch.CLASS
    )
    public static class FieldPatcher {
        public static SpireField<GachaShop> gachaShop = new SpireField<>(() -> null);
    }
    @SpirePatch(
            clz = ShopRoom.class,
            method = "onPlayerEntry",
            paramtypez= {
            }
    )
    public static class ShopRoomOnPlayerEntryPatcher {
        public static void Postfix(ShopRoom __instance) {
            if (AbstractDungeon.player instanceof Hifumi) {
                FieldPatcher.gachaShop.set(__instance, new GachaShop());
            }
        }
    }

    @SpirePatch(
            clz = ShopRoom.class,
            method = "update",
            paramtypez= {
            }
    )
    public static class ShopRoomUpdatePatcher {
        public static void Postfix(ShopRoom __instance) {
            if(FieldPatcher.gachaShop.get(__instance) != null)
                FieldPatcher.gachaShop.get(__instance).update();
        }
    }
    @SpirePatch(
            clz = ShopRoom.class,
            method = "render",
            paramtypez= {
                SpriteBatch.class
            }
    )
    public static class ShopRoomRenderPatcher {
        public static void Prefix(ShopRoom __instance, SpriteBatch sb) {
            if(FieldPatcher.gachaShop.get(__instance) != null)
                FieldPatcher.gachaShop.get(__instance).render(sb);
        }
    }

    @SpirePatch(
            clz = ShopRoom.class,
            method = "dispose",
            paramtypez= {
            }
    )
    public static class ShopRoomDisposePatcher {
        public static void Prefix(ShopRoom __instance) {
            if(FieldPatcher.gachaShop.get(__instance) != null)
                FieldPatcher.gachaShop.get(__instance).dispose();
        }
    }

}
