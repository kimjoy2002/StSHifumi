package BlueArchive_Hifumi.cards;

import BlueArchive_Hifumi.DefaultMod;
import BlueArchive_Hifumi.actions.AttackRandomAction;
import BlueArchive_Hifumi.actions.CannonAfterAction;
import BlueArchive_Hifumi.actions.NimbleCollectorEnergyAction;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.effects.TimeOnTargetEffect;
import BlueArchive_Hifumi.patches.CannonPatch;
import BlueArchive_Hifumi.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BlizzardEffect;

import static BlueArchive_Hifumi.DefaultMod.makeCardPath;

public class TimeOnTarget extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(TimeOnTarget.class.getSimpleName());
    public static final String IMG = makeCardPath("TimeOnTarget.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Hifumi.Enums.COLOR_YELLOW;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 3;



    public TimeOnTarget() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.tags.add(EnumPatch.NONCOLLECTABLE);
        this.tags.add(EnumPatch.FRIEND);
        this.tags.add(EnumPatch.CANNON);
    }


    public void applyPowers() {
        target = CardTarget.ENEMY;
        super.applyPowers();
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m == null) {
            if (Settings.FAST_MODE) {
                this.addToBot(new VFXAction(new TimeOnTargetEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.25F));
            } else {
                this.addToBot(new VFXAction(new TimeOnTargetEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 1.0F));
            }

            DamageInfo cannon_damage = new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.NORMAL);
            CannonPatch.CannonPatcher.isCannon.set(cannon_damage, true);
            AbstractDungeon.actionManager.addToBottom(new AttackRandomAction(cannon_damage, AbstractGameAction.AttackEffect.FIRE));
        }
        else {
            if (Settings.FAST_MODE) {
                this.addToBot(new VFXAction(new TimeOnTargetEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.25F));
            } else {
                this.addToBot(new VFXAction(new TimeOnTargetEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 1.0F));
            }
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.FIRE));
        }
        AbstractDungeon.actionManager.addToBottom(new CannonAfterAction(this, CardTarget.ENEMY));
    }
    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
