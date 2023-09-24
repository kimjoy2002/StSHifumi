package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.AcqireRelicAction;
import BlueArchive_Hifumi.powers.MomoFriendPower;
import BlueArchive_Hifumi.relics.TwinPeroroRelic;
import BlueArchive_Hifumi.relics.peroro.PeroroGoodsRelic;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;
import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public class PeroroGoods extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(PeroroGoods.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("PeroroGoods.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;


    public AbstractRelic relic;
    public int add;
    public int amount;
    public int plus_add = 0;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = -2;

    public PeroroGoods() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        relic = null;
        add = 0;
        amount = 0;
    }

    public PeroroGoods(AbstractRelic relic, int add, int amount) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.relic = relic;
        this.add = add;
        this.name = languagePack.getRelicStrings(relic.relicId).NAME;
        this.amount = amount;

        afterInit();
    }


    public PeroroGoods(AbstractRelic relic, int add, int amount, int plus_add) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.relic = relic;
        this.add = add;
        this.name = languagePack.getRelicStrings(relic.relicId).NAME;
        this.amount = amount;
        this.plus_add = plus_add;

        afterInit();
    }
    public void afterInit() {
    if(relic !=null) {
            this.portrait = new TextureAtlas.AtlasRegion(relic.img, 0, 0, relic.img.getWidth(), relic.img.getHeight());


            if (relic instanceof PeroroGoodsRelic) {
                boolean isTempRelic = false;
                if (amount==-1 && !AbstractDungeon.player.hasRelic(relic.relicId) ) {
                    isTempRelic = true;
                }
                magicNumber = baseMagicNumber = ((PeroroGoodsRelic) relic).getMagic(isTempRelic?0:(add > 0 ? add : 0));
                secondMagicNumber = baseSecondMagicNumber = ((PeroroGoodsRelic) relic).getMagic2(isTempRelic?0:(add > 0 ? add : 0));
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[((PeroroGoodsRelic) relic).getIndex()];
                initializeDescription();
            }
        }


    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void onChoseThisOption() {
        if(amount == -2) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MomoFriendPower(relic)));
        }
        else if(amount == -1) {
            if (AbstractDungeon.player.hasRelic(relic.relicId) ) {
                relic.flash();
                if(add > 0) {
                    ((PeroroGoodsRelic)relic).addTempCounter(add);
                }

                if(add == -1) {
                    relic.grayscale = true;
                    TwinPeroroRelic.checkTwinPeroro();
                }
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new AcqireRelicAction(relic));
            }

            for(int i = 0; i < plus_add; i++) {
                ((PeroroGoodsRelic) relic).use();
            }
        }
        else if(relic instanceof PeroroGoodsRelic) {
            relic.flash();
            if(add > 0) {
                ((PeroroGoodsRelic)relic).addTempCounter(add);
            }
            for(int i = 0; i < amount; i++) {
                ((PeroroGoodsRelic) relic).use();
            }
            if(add == -1) {
                relic.grayscale = true;
                TwinPeroroRelic.checkTwinPeroro();
            }
        }
    }
    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        if(card instanceof PeroroGoods) {
            ((PeroroGoods)card).relic = relic;
            ((PeroroGoods)card).add = add;
            ((PeroroGoods)card).amount = amount;
            ((PeroroGoods)card).afterInit();
        }
        return card;
    }
}
