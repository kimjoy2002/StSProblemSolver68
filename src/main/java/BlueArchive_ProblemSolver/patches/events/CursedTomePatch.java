package BlueArchive_ProblemSolver.patches.events;


import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.CursedTome;
import com.megacrit.cardcrawl.localization.EventStrings;

public class CursedTomePatch {
    private static final EventStrings strings;

    public CursedTomePatch() {
    }

    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:CursedTome");
    }

    @SpirePatch(
            clz = CursedTome.class,
            method = "buttonEffect"
    )
    public static class TomeDamage {
        public TomeDamage() {
        }

        @SpireInsertPatch(
                rloc = 49
        )
        public static void Insert(CursedTome __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer) ReflectionHacks.getPrivate(__instance, CursedTome.class, "finalDmg");
                ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
            }

        }
    }



    @SpirePatch(
            clz = CursedTome.class,
            method = "buttonEffect"
    )
    public static class TomeOptionDamage1 {

        @SpireInsertPatch(
                rloc = 7
        )
        public static void Insert(CursedTome __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0]);
            }

        }
    }


    @SpirePatch(
            clz = CursedTome.class,
            method = "buttonEffect"
    )
    public static class TomeOptionDamage2 {
        @SpireInsertPatch(
                rloc = 24
        )
        public static void Insert(CursedTome __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                ((ProblemSolver68)AbstractDungeon.player).damageAll(1);
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[1]);
            }

        }
    }

    @SpirePatch(
            clz = CursedTome.class,
            method = "buttonEffect"
    )
    public static class TomeOptionDamage3 {
        @SpireInsertPatch(
                rloc = 34
        )
        public static void Insert(CursedTome __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                ((ProblemSolver68)AbstractDungeon.player).damageAll(2);
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[2]);
            }

        }
    }


    @SpirePatch(
            clz = CursedTome.class,
            method = "buttonEffect"
    )
    public static class TomeOptionDamage4 {
        @SpireInsertPatch(
                rloc = 59
        )
        public static void Insert(CursedTome __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                ((ProblemSolver68)AbstractDungeon.player).damageAll(3);
            }

        }
    }

    @SpirePatch(
            clz = CursedTome.class,
            method = "buttonEffect"
    )
    public static class TomeOptionDamage {
        public TomeOptionDamage() {
        }

        @SpireInsertPatch(
                rloc = 43
        )
        public static void Insert(CursedTome __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                ((ProblemSolver68)AbstractDungeon.player).damageAll(3);
                int dmg = (Integer)ReflectionHacks.getPrivate(__instance, CursedTome.class, "finalDmg");
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[4] + dmg + CursedTome.OPTIONS[6]);
            }

        }
    }

    @SpirePatch(
            clz = CursedTome.class,
            method = "<ctor>"
    )
    public static class TomeConstructior {
        public TomeConstructior() {
        }

        public static void Postfix(CursedTome __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer)ReflectionHacks.getPrivate(__instance, CursedTome.class, "finalDmg");
                dmg = dmg / 3;
                ReflectionHacks.setPrivate(__instance, CursedTome.class, "finalDmg", dmg);
            }

        }
    }
}
