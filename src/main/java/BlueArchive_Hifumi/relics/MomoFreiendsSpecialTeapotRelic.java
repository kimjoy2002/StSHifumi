package BlueArchive_Hifumi.relics;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.cards.Flak40;
import BlueArchive_Hifumi.cards.TwoPounderHighExplosives;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hifumi.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_Hifumi.DefaultMod.makeRelicPath;

public class MomoFreiendsSpecialTeapotRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("MomoFreiendsSpecialTeapotRelic");

    public static final int AMOUNT = 1;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("momo_friends_special_teapot_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("momo_friends_special_teapot_relic.png"));

    public MomoFreiendsSpecialTeapotRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }



    public void previewCard(AbstractCard c) {
        Iterator var4 = AbstractDungeon.player.relics.iterator();
        while(var4.hasNext()) {
            AbstractRelic r = (AbstractRelic)var4.next();
            r.onPreviewObtainCard(c);
        }
    }


    public void onEquip() {
        int strike = 0;
        int upgrade_strike = 0;
        int defend = 0;
        int upgrade_defend = 0;
        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

        ArrayList<AbstractCard> cards = new ArrayList<>();
        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                if(c.upgraded)
                    upgrade_strike++;
                else
                    strike++;
                cards.add(c);
            }
            else if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                if(c.upgraded)
                    upgrade_defend++;
                else
                    defend++;
                cards.add(c);
            }
        }

        for(AbstractCard c : cards) {
            AbstractDungeon.player.masterDeck.removeCard(c);
        }

        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for(int i = 0; i < strike; i++) {
            AbstractCard c = new TwoPounderHighExplosives();
            previewCard(c);
            group.addToBottom(c);
        }
        for(int i = 0; i < upgrade_strike; i++) {
            AbstractCard c = new TwoPounderHighExplosives();
            c.upgrade();
            previewCard(c);
            group.addToBottom(c);
        }
        for(int i = 0; i < defend; i++) {
            AbstractCard c = new Flak40();
            previewCard(c);
            group.addToBottom(c);
        }
        for(int i = 0; i < upgrade_defend; i++) {
            AbstractCard c = new Flak40();
            c.upgrade();
            previewCard(c);
            group.addToBottom(c);
        }

        AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, this.DESCRIPTIONS[1]);
    }


    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
