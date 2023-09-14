package BlueArchive_Hifumi.reward;

import com.megacrit.cardcrawl.rewards.RewardSave;

import java.util.UUID;

public class EvoveSave extends RewardSave {
    public UUID uuid;
    public EvoveSave(String type, UUID uuid){
        super(type, null, 0, 0);
        this.uuid = uuid;
    }
}
