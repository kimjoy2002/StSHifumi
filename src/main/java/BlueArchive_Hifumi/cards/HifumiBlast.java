package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.CollectAction;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.evacipated.cardcrawl.mod.stslib.patches.GravePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class HifumiBlast extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(HifumiBlast.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("HifumiBlast.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 2;
    private static final int MAGIC = 7;


    public HifumiBlast() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.isInnate = true;
        this.baseDamage = 0;
        baseMagicNumber = magicNumber = MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal)));
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void applyPowers() {
        int count = 0;
        for(AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if(c.hasTag(EnumPatch.LEARNED) || GraveField.grave.get(c)) {
                count++;
            }
        }
        this.baseDamage = count*magicNumber;
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }
    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);
            initializeDescription();
        }
    }
}
