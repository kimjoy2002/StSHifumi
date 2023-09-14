package BlueArchive_Hifumi.patches;

import BlueArchive_Hifumi.cards.FunnyFriendFantasy;
import BlueArchive_Hifumi.cards.PeroroTicketUncommon;
import BlueArchive_Hifumi.cards.Reisa;
import BlueArchive_Hifumi.relics.ReisaRelic;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.PandorasBox;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_Hifumi.cards.FunnyFriendFantasy.getRandomFriendCard;

public class FriendPatch {
    //상점에서 나오지 않게하기

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomCardFromAvailable",
            paramtypez= {
                    AbstractCard.class,
                    Random.class
            }
    )
    public static class RandomCardPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"list"}

        )
        public static void Insert(AbstractCard unuse0, Random unuse1,  @ByRef ArrayList<AbstractCard>[] list) {
            if(list!=null) {
                ArrayList<AbstractCard> removeList = new ArrayList();
                for(AbstractCard card : list[0]) {
                    if(card.hasTag(EnumPatch.FRIEND)) {
                        if(AbstractDungeon.player.hasRelic(ReisaRelic.ID)) {
                            removeList.add(card);
                        } else {
                            Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();
                            boolean change = false;
                            while (var2.hasNext()) {
                                AbstractCard c = (AbstractCard) var2.next();
                                if (c.cardID == card.cardID) {
                                    removeList.add(c);
                                }
                            }
                        }
                    }
                }
                for(AbstractCard card : removeList) {
                    list[0].remove(card);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



    @SpirePatch(
            clz = PandorasBox.class,
            method = "onEquip"
    )
    public static class PandorasBoxPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"card", "group"}
        )
        public static void Insert(PandorasBox __instance, @ByRef AbstractCard[] card, CardGroup group) {
            while(card != null && card[0].hasTag(EnumPatch.FRIEND)) {
                if(AbstractDungeon.player.hasRelic(ReisaRelic.ID)) {
                    card[0] = AbstractDungeon.returnTrulyRandomCard().makeCopy();
                    continue;
                } else {
                    Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();
                    boolean change = false;
                    while (var2.hasNext()) {
                        AbstractCard c = (AbstractCard) var2.next();
                        if (c.cardID == card[0].cardID) {
                            card[0] = AbstractDungeon.returnTrulyRandomCard().makeCopy();
                            change = true;
                        }
                    }
                    if(!change) {
                        for (AbstractCard c : group.group) {
                            if (c.cardID == card[0].cardID) {
                                card[0] = AbstractDungeon.returnTrulyRandomCard().makeCopy();
                                change = true;
                            }
                        }
                    }
                    if(change)
                        continue;
                }
                break;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(UnlockTracker.class, "markCardAsSeen");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = Merchant.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez= {
                    float.class,
                    float.class,
                    int.class
            }
    )
    public static class MerchantPatcher {
        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(Merchant __instance, float x, float y, int newShopScreen) {
            ArrayList<AbstractCard> card1 = (ArrayList<AbstractCard>) ReflectionHacks.getPrivate(__instance, Merchant.class, "cards1");
            ArrayList<AbstractCard> needtoChange = new ArrayList<>();

            for(AbstractCard card : card1) {
                if(card.hasTag(EnumPatch.FRIEND)) {
                    if(AbstractDungeon.player.hasRelic(ReisaRelic.ID)) {
                        //needtoChange.add(card);
                    } else {
                        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();
                        while (var2.hasNext()) {
                            AbstractCard c = (AbstractCard) var2.next();
                            if (c.cardID == card.cardID) {
                                needtoChange.add(card);
                            }
                        }
                    }
                }
            }
            for(AbstractCard card : needtoChange) {
                card1.remove(card);
                AbstractCard c;
                for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), card.type, true).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS || c.hasTag(EnumPatch.FRIEND); c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), card.type, true).makeCopy()) {
                }
                card1.add(c);
            }
            ReflectionHacks.setPrivate(__instance, Merchant.class,"cards1", card1);
        }




        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ShopScreen.class, "init");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class getRewardPatcher {


        @SpireInsertPatch(
                loc=1837,
                localvars={"card", "rarity", "containsDupe"}
        )
        public static void Insert(@ByRef AbstractCard[] card, AbstractCard.CardRarity rarity, @ByRef boolean[] containsDupe) {
            if(card != null && containsDupe != null) {
                if(card[0] != null && card[0].hasTag(EnumPatch.FRIEND)) {
                    if(AbstractDungeon.player.hasRelic(ReisaRelic.ID)) {
                        //containsDupe[0] = true;
                    } else {
                        Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

                        while(var2.hasNext()) {
                            AbstractCard c = (AbstractCard)var2.next();
                            if (c.cardID == card[0].cardID) {
                                containsDupe[0] = true;
                            }
                        }
                    }

                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCard");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }

    }



    @SpirePatch(
            clz = Soul.class,
            method = "obtain",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class CardObtainPatcher {

        public static void Postfix(Soul __instance, AbstractCard c) {
            if(c.hasTag(EnumPatch.FRIEND)) {
                if(AbstractDungeon.player.hasRelic(ReisaRelic.ID)) {
                    AbstractDungeon.player.getRelic(ReisaRelic.ID).flash();
                    AbstractDungeon.topLevelEffectsQueue.add(new PurgeCardEffect(new Reisa()));
                    AbstractDungeon.player.masterDeck.removeCard(c);
                }
                else {
                    Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

                    while(var2.hasNext()) {
                        AbstractCard card = (AbstractCard)var2.next();
                        if (c != card && c.cardID == card.cardID) {
                            AbstractDungeon.player.masterDeck.removeCard(c);
                            break;
                        }
                    }
                }
            }

        }
    }
}
