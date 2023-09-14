package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.ui.HifumiTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_Hifumi.DefaultMod.*;

public class HifumiTutorialAction extends AbstractGameAction {

    public HifumiTutorialAction() {
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (activeTutorial) {
            AbstractDungeon.ftue = new HifumiTutorial();
            activeTutorial = false;

            try {
                SpireConfig config = new SpireConfig("BlueArchive_Hifumi", "BlueArchiveConfig", hifumiSettings);
                config.setBool(ACTIVE_TUTORIAL, activeTutorial);
                config.save();
            } catch (Exception ignore) {
            }
        }
        this.isDone = true;
    }
}
