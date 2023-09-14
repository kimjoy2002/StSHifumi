package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.cards.CollectCard;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.DiscardPilePanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static java.lang.Math.min;

public class LearnedCardPatch {

    private static final Logger logger = LogManager.getLogger(LearnedCardPatch.class.getName());


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfCombatPreDrawLogic",
            paramtypez= {
            }
    )
    public static class addToHandPatcher {

        public static void Postfix(AbstractPlayer __instance) {
            boolean render = false;
            for(AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if(c.hasTag(EnumPatch.LEARNED)) {
                    AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(c, AbstractDungeon.player.drawPile));

                }
            }
        }
    }
}
