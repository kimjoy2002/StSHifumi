package BlueArchive_Hifumi;

import BlueArchive_Hifumi.actions.HifumiTutorialAction;
import BlueArchive_Hifumi.cards.Wanted;
import BlueArchive_Hifumi.characters.Hifumi;
import BlueArchive_Hifumi.events.PeroroEvent;
import BlueArchive_Hifumi.events.PeroroShopEvent;
import BlueArchive_Hifumi.events.SpotCheckEvent;
import BlueArchive_Hifumi.events.SupplementaryLessonsDepartmentEvent;
import BlueArchive_Hifumi.patches.EnumPatch;
import BlueArchive_Hifumi.potions.CollectPotion;
import BlueArchive_Hifumi.potions.PeroroPotion;
import BlueArchive_Hifumi.relics.*;
import BlueArchive_Hifumi.relics.peroro.*;
import BlueArchive_Hifumi.reward.EvoveReward;
import BlueArchive_Hifumi.reward.EvoveSave;
import BlueArchive_Hifumi.reward.LearndReward;
import BlueArchive_Hifumi.screens.EvoveScreen;
import BlueArchive_Hifumi.screens.GachaShopScreen;
import BlueArchive_Hifumi.screens.LearendScreen;
import BlueArchive_Hifumi.variables.SecondMagicNumber;
import BlueArchive_Hifumi.variables.ThirdMagicNumber;
import basemod.*;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import BlueArchive_Hifumi.util.IDCheckDontTouchPls;
import BlueArchive_Hifumi.util.TextureLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;

import static basemod.eventUtil.EventUtils.eventIDs;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 4 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault", and change to "yourmodname" rather than "thedefault".
// You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories, and press alt+c to make the replace case sensitive (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class DefaultMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber,
        OnStartBattleSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties hifumiSettings = new Properties();
    public static final String ACTIVE_TUTORIAL = "activeTutorial";
    public static final String DISABLE_PEROROGOODS_PANEL = "disablePerorogoodsPanel";
    public static boolean activeTutorial = true;
    public static boolean disablePeroroGoodsBar = false;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "BlueArchive Hifumi";
    private static final String AUTHOR = "joy1999"; // And pretty soon - You!
    private static final String DESCRIPTION = "BlueArchive Ajitani Hifumi mod";
    ModLabeledToggleButton activeTutorialButton = null;
    ModLabeledToggleButton disablePeroroGoodsBarButton = null;
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color DEFAULT_YELLOW = CardHelper.getColor(234.0f, 189.0f, 131.0f);
    
    // Potion Colors in RGB
    public static final Color PERORO_POTION_LIQUID = CardHelper.getColor(240.0f, 240.0f, 240.0f);
    public static final Color PERORO_POTION_HYBRID = CardHelper.getColor(240.0f, 240.0f, 240.0f);
    public static final Color PERORO_POTION_SPOTS = CardHelper.getColor(30.0f, 30.0f, 30.0f);

    public static final Color COLLECT_POTION_LIQUID = CardHelper.getColor(234.0f, 189.0f, 131.0f);
    public static final Color COLLECT_POTION_HYBRID= CardHelper.getColor(234.0f, 189.0f, 131.0f);
    public static final Color COLLECT_POTION_SPOTS= CardHelper.getColor(234.0f, 189.0f, 131.0f);

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!


    private static final String ATTACK_HIFUMI = "BlueArchive_HifumiResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_HIFUMI = "BlueArchive_HifumiResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_HIFUMI = "BlueArchive_HifumiResources/images/512/bg_power_default_gray.png";


    private static final String ENERGY_ORB_HIFUMI = "BlueArchive_HifumiResources/images/512/card_default_gray_orb.png";
    private static final String CARD_HIFUMI_ENERGY_ORB = "BlueArchive_HifumiResources/images/512/card_small_orb.png";

    private static final String ATTACK_HIFUMI_PORTRAIT = "BlueArchive_HifumiResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_HIFUMI_PORTRAIT = "BlueArchive_HifumiResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_HIFUMI_PORTRAIT = "BlueArchive_HifumiResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_HIFUMI_PORTRAIT = "BlueArchive_HifumiResources/images/1024/card_default_gray_orb.png";

    // Character assets
    private static final String HIFUMI_BUTTON = "BlueArchive_HifumiResources/images/charSelect/HifumiButton.png";
    private static final String HIFUMI_PORTRAIT = "BlueArchive_HifumiResources/images/charSelect/HifumiPortraitBG.png";
    public static final String HIFUMI_SHOULDER_1 = "BlueArchive_HifumiResources/images/char/hifumi/shoulder.png";
    public static final String HIFUMI_SHOULDER_2 = "BlueArchive_HifumiResources/images/char/hifumi/shoulder2.png";
    public static final String HIFUMI_CORPSE = "BlueArchive_HifumiResources/images/char/hifumi/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "BlueArchive_HifumiResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String HIFUMI_SKELETON_ATLAS = "BlueArchive_HifumiResources/images/char/hifumi/Hifumi2.atlas";
    public static final String HIFUMI_SKELETON_JSON = "BlueArchive_HifumiResources/images/char/hifumi/Hifumi2.json";

    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    public static String makeCharPath(String resourcePath) {
        return getModID() + "Resources/images/char/other" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public DefaultMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("BlueArchive_Hifumi");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project) and press alt+c (or mark the match case option)
        // replace all instances of theDefault with yourModID, and all instances of thedefault with yourmodid (the same but all lowercase).
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // It's important that the mod ID prefix for keywords used in the cards descriptions is lowercase!

        // 3. Scroll down (or search for "ADD CARDS") till you reach the ADD CARDS section, and follow the TODO instructions

        // 4. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");

        logger.info("Creating the color " + Hifumi.Enums.COLOR_YELLOW.toString());
        BaseMod.addColor(Hifumi.Enums.COLOR_YELLOW, DEFAULT_YELLOW, DEFAULT_YELLOW, DEFAULT_YELLOW,
                DEFAULT_YELLOW, DEFAULT_YELLOW, DEFAULT_YELLOW, DEFAULT_YELLOW,
                ATTACK_HIFUMI, SKILL_HIFUMI, POWER_HIFUMI, ENERGY_ORB_HIFUMI,
                ATTACK_HIFUMI_PORTRAIT, SKILL_HIFUMI_PORTRAIT, POWER_HIFUMI_PORTRAIT,
                ENERGY_ORB_HIFUMI_PORTRAIT, CARD_HIFUMI_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        hifumiSettings.setProperty(ACTIVE_TUTORIAL, "TRUE");
        hifumiSettings.setProperty(DISABLE_PEROROGOODS_PANEL, "FALSE");

        try {
            SpireConfig config = new SpireConfig("BlueArchive_Hifumi", "BlueArchiveConfig", hifumiSettings);
            config.load();
            activeTutorial = config.getBool(ACTIVE_TUTORIAL);
            disablePeroroGoodsBar = config.getBool(DISABLE_PEROROGOODS_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = DefaultMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        DefaultMod defaultmod = new DefaultMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Hifumi.Enums.HIFUMI.toString());

        BaseMod.addCharacter(new Hifumi("Hifumi", Hifumi.Enums.HIFUMI),
                HIFUMI_BUTTON, HIFUMI_PORTRAIT, Hifumi.Enums.HIFUMI);
        logger.info("Added " + Hifumi.Enums.HIFUMI.toString());
        receiveEditPotions();
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        activeTutorialButton = new ModLabeledToggleButton("Active Hifumi Tutorial.",
                350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                activeTutorial,
                settingsPanel,
                (label) -> {},
                (button) -> {
                    activeTutorial = button.enabled;
                    try {
                        SpireConfig config = new SpireConfig("BlueArchive_Hifumi", "BlueArchiveConfig", hifumiSettings);
                        config.setBool(ACTIVE_TUTORIAL, activeTutorial);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


        disablePeroroGoodsBarButton = new ModLabeledToggleButton("Do not display perorogoods on another line. (for mode conflict)",
                350.0f, 550.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                disablePeroroGoodsBar,
                settingsPanel,
                (label) -> {},
                (button) -> {
                    disablePeroroGoodsBar = button.enabled;
                    try {
                        SpireConfig config = new SpireConfig("BlueArchive_Hifumi", "BlueArchiveConfig", hifumiSettings);
                        config.setBool(DISABLE_PEROROGOODS_PANEL, disablePeroroGoodsBar);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(activeTutorialButton);
        settingsPanel.addUIElement(disablePeroroGoodsBarButton);
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        BaseMod.registerCustomReward(
                EnumPatch.REWORD_LEARNED,
                (rewardSave) -> { // this handles what to do when this quest type is loaded.
                    return new LearndReward();
                },
                (customReward) -> { // this handles what to do when this quest type is saved.
                    return new RewardSave(customReward.type.toString(), null, 0, 0);
                });

        BaseMod.registerCustomReward(
                EnumPatch.REWORD_EVOVE,
                (rewardSave) -> { // this handles what to do when this quest type is loaded.
                    return new EvoveReward((rewardSave).amount);
                },
                (customReward) -> { // this handles what to do when this quest type is saved.
                    return new RewardSave(customReward.type.toString(), null, ((EvoveReward)customReward).misc, 0);
                });


        BaseMod.addCustomScreen(new LearendScreen());
        BaseMod.addCustomScreen(new EvoveScreen());
        BaseMod.addCustomScreen(new GachaShopScreen());
        
        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events


        {
            AddEventParams eventParams = new AddEventParams.Builder(PeroroShopEvent.ID, PeroroShopEvent.class)
                    .dungeonID(TheCity.ID)
                    .playerClass(Hifumi.Enums.HIFUMI)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(SupplementaryLessonsDepartmentEvent.ID, SupplementaryLessonsDepartmentEvent.class)
                    .dungeonID(Exordium.ID)
                    .playerClass(Hifumi.Enums.HIFUMI)
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(PeroroEvent.ID, PeroroEvent.class)
                    .playerClass(Hifumi.Enums.HIFUMI)
                    .overrideEvent("BlueArchive_Hoshino:PeroroEvent")
                    .bonusCondition(() -> {
                        AbstractRelic peroro_ = RelicLibrary.getRelic("BlueArchive_Hoshino:PeroroRelic");
                        if(peroro_ instanceof Circlet)
                            return false;
                        return true;
                    })
                    .create();
            BaseMod.addEvent(eventParams);
        }
        {
            AddEventParams eventParams = new AddEventParams.Builder(SpotCheckEvent.ID, SpotCheckEvent.class)
                    .dungeonID(TheBeyond.ID)
                    .playerClass(Hifumi.Enums.HIFUMI)
                    .bonusCondition(() -> {
                        if(AbstractDungeon.player ==null)
                            return false;
                        for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                            if(card.cardID == Wanted.ID) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .create();
            BaseMod.addEvent(eventParams);
        }

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }


    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if (AbstractDungeon.player instanceof Hifumi) {
            if (activeTutorial) {
                AbstractDungeon.actionManager.addToBottom(new HifumiTutorialAction());
            }
        }
    }


    // =============== / POST-INITIALIZE/ =================
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");



        BaseMod.addPotion(PeroroPotion.class, PERORO_POTION_LIQUID, PERORO_POTION_HYBRID, PERORO_POTION_SPOTS, PeroroPotion.POTION_ID, Hifumi.Enums.HIFUMI);
        BaseMod.addPotion(CollectPotion.class, COLLECT_POTION_LIQUID, COLLECT_POTION_HYBRID, COLLECT_POTION_SPOTS, CollectPotion.POTION_ID, Hifumi.Enums.HIFUMI);


        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        BaseMod.addRelicToCustomPool(new HifumiBaseRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new HifumiBaseRelicPlus(), Hifumi.Enums.COLOR_YELLOW);

        BaseMod.addRelicToCustomPool(new PeroroCoverRelic(), Hifumi.Enums.COLOR_YELLOW);

        BaseMod.addRelicToCustomPool(new BottledPeroro(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new PeroroMembershipCardRelic(), Hifumi.Enums.COLOR_YELLOW);

        BaseMod.addRelicToCustomPool(new DangerousSwimsuitRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new UnidentifiedBagRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new FigureStorageRelic(), Hifumi.Enums.COLOR_YELLOW);
        //
        BaseMod.addRelicToCustomPool(new ReisaRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new MomoFreiendsSpecialTeapotRelic(), Hifumi.Enums.COLOR_YELLOW);

        BaseMod.addRelicToCustomPool(new TwinPeroroRelic(), Hifumi.Enums.COLOR_YELLOW);

        BaseMod.addRelicToCustomPool(new CommonPeroroBlockRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new CommonPeroroDamageRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new CommonPeroroMultiDamageRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new UncommonPeroroBlockRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new UncommonPeroroDamageRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new UncommonPeroroSpecialRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new RarePeroroBlockRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new RarePeroroDamageRelic(), Hifumi.Enums.COLOR_YELLOW);
        BaseMod.addRelicToCustomPool(new RarePeroroEnergyRelic(), Hifumi.Enums.COLOR_YELLOW);

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        
        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards so you don't have to manually load them 1 by 1
        // For more specific info, including how to exclude cards from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        //TODO: Rename the "DefaultMod" with the modid in your ModTheSpire.json file
        //TODO: The artifact mentioned in ModTheSpire.json is the artifactId in pom.xml you should've edited earlier
        new AutoAdd("BlueArchive_Hifumi") // ${project.artifactId}
                .packageFilter("BlueArchive_Hifumi.cards") // filters to any class in the same package as AbstractDefaultCard, nested packages included
                .setDefaultSeen(true)
                .cards();
        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        String pathByLanguage;
        switch(Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                break;
            case ZHS:
                pathByLanguage = getModID() + "Resources/localization/" + "zhs/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
        }

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                pathByLanguage + "Hifumi-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                pathByLanguage + "Hifumi-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                pathByLanguage + "Hifumi-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                pathByLanguage + "Hifumi-Event-Strings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                pathByLanguage + "Hifumi-Potion-Strings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                pathByLanguage + "Hifumi-Character-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                pathByLanguage + "Hifumi-UI-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                pathByLanguage + "Hifumi-Tutorials-Strings.json");

        
        logger.info("Done edittting strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String pathByLanguage;
        switch (Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                break;
            case ZHS:
                pathByLanguage = getModID() + "Resources/localization/" + "zhs/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
        }

        {
            String json = Gdx.files.internal(pathByLanguage + "Hifumi-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
            com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

            if (keywords != null) {
                for (Keyword keyword : keywords) {
                    BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("BlueArchive_Hifumi:HifumiSelect", getModID() + "Resources/sound/Hifumi_select.mp3");
    }
}
