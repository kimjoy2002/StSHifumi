package BlueArchive_Hifumi.reward;

import BlueArchive_Hifumi.actions.AddLearnedAction;
import BlueArchive_Hifumi.actions.ModifyLearnedAction;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.screens.EvoveScreen;
import BlueArchive_Hifumi.screens.LearendScreen;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hifumi.DefaultMod.getModID;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class LearndReward extends CustomReward {
    private static final Texture ICON =  TextureLoader.getTexture(getModID() + "Resources/images/empty.png");

    public LearndReward() {
        super(ICON, AddLearnedAction.TEXT[0], EnumPatch.REWORD_LEARNED);
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            BaseMod.openCustomScreen(LearendScreen.Enum.LEAREND_SCREEN);
            CustomScreen customScreen = BaseMod.getCustomScreen(LearendScreen.Enum.LEAREND_SCREEN);
            if (customScreen != null && customScreen instanceof LearendScreen) {
                ((LearendScreen)customScreen).init();
            }
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return true;
    }
}
