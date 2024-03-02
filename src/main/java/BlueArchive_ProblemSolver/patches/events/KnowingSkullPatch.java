package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.KnowingSkull;
import com.megacrit.cardcrawl.localization.EventStrings;

public class KnowingSkullPatch {

    private static final EventStrings strings;

    public KnowingSkullPatch() {
    }

    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:KnowingSkull");
    }

    @SpirePatch(
            clz = KnowingSkull.class,
            method = "buttonEffect"
    )
    public static class SkullDamage {
        public SkullDamage() {
        }

        @SpireInsertPatch(
                rloc = 12
        )
        public static void Insert(KnowingSkull __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer) ReflectionHacks.getPrivate(__instance, KnowingSkull.class,
                        buttonPressed==0?"potionCost":
                        buttonPressed==1?"goldCost":
                        buttonPressed==2?"cardCost":
                        "leaveCost");
                ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
            }

        }
    }

    @SpirePatch(
            clz = KnowingSkull.class,
            method = "buttonEffect"
    )
    public static class SkullOptionDamage {
        public SkullOptionDamage() {
        }

        @SpireInsertPatch(
                rloc = 8
        )
        public static void Insert(KnowingSkull __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof Aru) {
                int potionCost = (Integer)ReflectionHacks.getPrivate(__instance, KnowingSkull.class, "potionCost");
                int goldCost = (Integer)ReflectionHacks.getPrivate(__instance, KnowingSkull.class, "goldCost");
                int cardCost = (Integer)ReflectionHacks.getPrivate(__instance, KnowingSkull.class, "cardCost");
                int leaveCost = (Integer)ReflectionHacks.getPrivate(__instance, KnowingSkull.class, "leaveCost");
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[2] + potionCost + KnowingSkull.OPTIONS[1]);
                __instance.imageEventText.updateDialogOption(1, KnowingSkull.OPTIONS[5] + 90 + strings.OPTIONS[3] + goldCost + KnowingSkull.OPTIONS[1]);
                __instance.imageEventText.updateDialogOption(2, strings.OPTIONS[1] + cardCost + KnowingSkull.OPTIONS[1]);
                __instance.imageEventText.updateDialogOption(3, strings.OPTIONS[4] + leaveCost + KnowingSkull.OPTIONS[1]);
            }

        }
    }

    @SpirePatch(
            clz = KnowingSkull.class,
            method = "<ctor>"
    )
    public static class SkullConstructior {
        public SkullConstructior() {
        }

        public static void Postfix(KnowingSkull __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                ReflectionHacks.setPrivate(__instance, KnowingSkull.class, "leaveCost", 3);
                ReflectionHacks.setPrivate(__instance, KnowingSkull.class, "cardCost", 3);
                ReflectionHacks.setPrivate(__instance, KnowingSkull.class, "potionCost", 3);
                ReflectionHacks.setPrivate(__instance, KnowingSkull.class, "goldCost", 3);
            }

        }
    }
}
