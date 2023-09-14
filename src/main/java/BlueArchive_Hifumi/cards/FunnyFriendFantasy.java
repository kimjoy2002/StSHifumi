package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.rareCardPool;

public class FunnyFriendFantasy extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(FunnyFriendFantasy.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("FunnyFriendFantasy.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;
    public static  ArrayList<AbstractCard> allFreindCard = new ArrayList<AbstractCard>();

    private static final int COST = 1;

    private static final int DAMAGE = 11;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static AbstractCard cardsToPreviewStatic = null;
    private static int viewFriendCountStatic = 0;
    private int viewFriendCount = 0;
    private static final int FRIEND_INTERVAL = 60;

    public FunnyFriendFantasy() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        if(cardsToPreviewStatic == null || viewFriendCountStatic > FRIEND_INTERVAL) {
            this.cardsToPreview = getRandomFriendCardAll(new Random());
            cardsToPreviewStatic = cardsToPreview;
            viewFriendCountStatic = 0;
        } else {
            cardsToPreview = cardsToPreviewStatic;
            viewFriendCountStatic++;
        }
    }

    public void changePreview() {
        viewFriendCount++;
        if(viewFriendCount > FRIEND_INTERVAL) {
            AbstractCard temp = getRandomFriendCardAll(new Random());
            while(temp == cardsToPreview) {
                temp = getRandomFriendCardAll(new Random());
            }
            this.cardsToPreview = temp;
            viewFriendCount = 0;
        }

    }

    @Override
    public void renderCardPreviewInSingleView(SpriteBatch sb) {
        super.renderCardPreviewInSingleView(sb);
        changePreview();
    }

    @Override
    public void renderCardPreview(SpriteBatch sb) {
        super.renderCardPreview(sb);
        changePreview();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }


    public static boolean hasCard(AbstractCard c) {
        for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if(card.cardID == c.cardID)
                return true;
        }
        return false;
    }

    public static AbstractCard getRandomFriendCardAll(Random random_) {
        if(!allFreindCard.isEmpty()) {
            return allFreindCard.get(random_.random(allFreindCard.size() - 1));
        }
        return null;
    }

    public static AbstractCard getRandomFriendCard(Random random_) {
        ArrayList<AbstractCard> remain_friend = new ArrayList<AbstractCard>();
        for(AbstractCard card : rareCardPool.group) {
            if(card.hasTag(EnumPatch.FRIEND) && !hasCard(card)) {
                remain_friend.add(card);
            }
        }
        if(!remain_friend.isEmpty()) {
            return remain_friend.get(random_.random(remain_friend.size() - 1));
        }
        return null;
    }

    public void onRemoveFromMasterDeck() {
        AbstractCard makeCard = getRandomFriendCard(AbstractDungeon.cardRng);
        if(upgraded){
            makeCard.upgrade();
        }
        if(makeCard != null) {
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(makeCard, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    static {
        allFreindCard.add(new TimeOnTarget());
        allFreindCard.add(new HolyHandGrenade());
        allFreindCard.add(new SagittaMortis());
        allFreindCard.add(new GourmetResearchSociety());
        allFreindCard.add(new MaskedSwinsuitGang());
        allFreindCard.add(new CastOff());
        allFreindCard.add(new ItazuraStraight());
    }

}
