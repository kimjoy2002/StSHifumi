package BlueArchive_Hifumi.actions;

import BlueArchive_Hifumi.cards.AbstractDefaultCard;
import BlueArchive_Hifumi.patches.CannonPatch;
import BlueArchive_Hifumi.reward.EvoveReward;
import BlueArchive_Hifumi.reward.LearndReward;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Iterator;

public class EvoveAttackAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private DamageInfo info;
    private AbstractCard card;
    private static final float DURATION = 0.1F;
    public int[] damage;

    public EvoveAttackAction(AbstractCard card, AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.card = card;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }
    public EvoveAttackAction(AbstractCard card, AbstractCreature source, int[] amount, DamageInfo.DamageType type, AbstractGameAction.AttackEffect effect) {
        this.info = null;
        this.damage = amount;
        this.source = source;
        this.damageType = type;
        this.card = card;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    public void afterMonsterDamage(AbstractMonster target_) {

        if ((target_.isDying || target_.currentHealth <= 0) && !target_.halfDead && !target_.hasPower("Minion")) {

            if(card.magicNumber > 0 ) {
                for(AbstractCard card_ : AbstractDungeon.player.masterDeck.group) {
                    if(card_.misc == card.misc) {
                        if(card_.magicNumber <= 0) {
                            return;
                        }
                    }
                }

                boolean isExist = false;
                for( RewardItem rewardItem : AbstractDungeon.getCurrRoom().rewards) {
                    if(rewardItem instanceof EvoveReward) {
                        if(((EvoveReward)rewardItem).misc == card.misc) {
                            isExist = true;
                            break;
                        }
                    }
                }
                if(!isExist) {
                    RewardItem rewardItem = new EvoveReward(card.misc);
                    AbstractDungeon.getCurrRoom().rewards.add(rewardItem);
                }
            }
        }
    }

    public void update() {
        if (this.duration == 0.1F) {
            if (info != null){
                if(this.target != null) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
                    this.target.damage(this.info);
                    afterMonsterDamage(((AbstractMonster) this.target));
                }
            }
            else { //multi
                int mons_size = AbstractDungeon.getCurrRoom().monsters.monsters.size();

                boolean playedMusic = false;
                for(int i = 0; i < mons_size; i++) {
                    if (!((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDying && ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).currentHealth > 0 && !((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isEscaping) {
                        if (playedMusic) {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX, ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect, true));
                        } else {
                            playedMusic = true;
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX, ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect));
                        }
                    }
                }


                Iterator var4 = AbstractDungeon.player.powers.iterator();

                while(var4.hasNext()) {
                    AbstractPower p = (AbstractPower)var4.next();
                    p.onDamageAllEnemies(this.damage);
                }

                int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

                for(int i = 0; i < temp; ++i) {
                    if (!((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDeadOrEscaped()) {
                        DamageInfo damage_ = new DamageInfo(this.source, this.damage[i], this.damageType);
                        if(card instanceof AbstractDefaultCard) {
                            if(((AbstractDefaultCard)card).triggerEndturn) {
                                CannonPatch.CannonPatcher.isCannon.set(damage_, true);
                            }
                        }
                        ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).damage(damage_);
                        afterMonsterDamage(((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)));
                    }
                }
            }



            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_Hifumi:EvoveAttackAction");
        TEXT = uiStrings.TEXT;
    }
}