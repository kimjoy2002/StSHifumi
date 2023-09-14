package BlueArchive_Hifumi.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;

import java.util.HashSet;
import java.util.Set;

public class GameActionManagerPatch {

    public static Set<String> collectedCardSet = new HashSet<String>();

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ShuffleActionPatch {
        public static void Postfix(GameActionManager __instance)
        {
            collectedCardSet.clear();
        }
    }





}
