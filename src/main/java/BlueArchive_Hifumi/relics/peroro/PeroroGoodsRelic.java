package BlueArchive_Hifumi.relics.peroro;

import BlueArchive_Hifumi.actions.AcqireRelicAction;
import BlueArchive_Hifumi.cards.Reisa;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;

public interface PeroroGoodsRelic {
    public static AbstractRelic getRandomPeroroGoods(AbstractRelic.RelicTier tier) {

        ArrayList<AbstractRelic> relics = new ArrayList<>();

        switch (tier) {
            case COMMON:
            default:
                relics.add(new CommonPeroroBlockRelic());
                relics.add(new CommonPeroroMultiDamageRelic());
                relics.add(new CommonPeroroDamageRelic());
                break;
            case UNCOMMON:
                relics.add(new UncommonPeroroDamageRelic());
                relics.add(new UncommonPeroroBlockRelic());
                relics.add(new UncommonPeroroSpecialRelic());
                break;
            case RARE:
                relics.add(new RarePeroroEnergyRelic());
                relics.add(new RarePeroroBlockRelic());
                relics.add(new RarePeroroDamageRelic());
                break;
        }
        AbstractRelic pRelic = relics.get(AbstractDungeon.relicRng.random(relics.size() - 1));
        return pRelic;
    }
    static void obtainCommonPeroroGoods() {
        obtainPeroroGoods(getRandomPeroroGoods(AbstractRelic.RelicTier.COMMON));
    }
    static void obtainPeroroGoods(AbstractRelic relic) {
        if(AbstractDungeon.player.hasRelic(relic.relicId)) {
            AbstractRelic hasRelic = AbstractDungeon.player.getRelic(relic.relicId);
            hasRelic.setCounter(hasRelic.counter+1);
        } else {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, relic);
        }
    }

    public void updateDescription();

    public int getIndex();
    public default int getMagic(int add){return 0;};
    public default int getMagic2(int add){return 0;};
    public void use();
    public void addTempCounter(int counter);
}
