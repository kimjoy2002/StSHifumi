package BlueArchive_Hifumi.reward;

import BlueArchive_Hifumi.actions.AddLearnedAction;
import BlueArchive_Hifumi.actions.EvoveAttackAction;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.screens.EvoveScreen;
import BlueArchive_Hifumi.screens.LearendScreen;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.UUID;

import static BlueArchive_Hifumi.DefaultMod.getModID;

public class EvoveReward extends CustomReward {
    private static final Texture ICON =  TextureLoader.getTexture(getModID() + "Resources/images/empty.png");

    public int misc;
    public EvoveReward(int misc) {
        super(ICON, EvoveAttackAction.TEXT[0], EnumPatch.REWORD_EVOVE);
        this.misc = misc;
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            BaseMod.openCustomScreen(EvoveScreen.Enum.EVOVE_SCREEN);
            EvoveScreen.misc = misc;
            CustomScreen customScreen = BaseMod.getCustomScreen(EvoveScreen.Enum.EVOVE_SCREEN);
            if (customScreen != null && customScreen instanceof EvoveScreen) {
                ((EvoveScreen)customScreen).init();
            }
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return true;
    }
}
