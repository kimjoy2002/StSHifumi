package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.relics.peroro.UncommonPeroroDamageRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class UpdatePeroroDamageAction extends AbstractGameAction {
    private UncommonPeroroDamageRelic relic;

    public UpdatePeroroDamageAction(UncommonPeroroDamageRelic relic) {
        this.relic = relic;
    }

    public void update() {
        if (this.relic != null) {
            relic.current_damage += relic.val2;
            relic.updateDescription();
        }

        this.isDone = true;
    }
}