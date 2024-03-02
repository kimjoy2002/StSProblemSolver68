package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.ScrapOoze;
import com.megacrit.cardcrawl.localization.EventStrings;

public class ScrapOozePatch {
    private static final EventStrings strings;
    private static int realDmg;

    public ScrapOozePatch() {
    }

    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:ScrapOoze");
        realDmg = 0;
    }

    @SpirePatch(
            clz = ScrapOoze.class,
            method = "buttonEffect"
    )
    public static class OozeOption {
        public OozeOption() {
        }

        @SpireInsertPatch(
                rloc = 28
        )
        public static void Insert(ScrapOoze __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer) ReflectionHacks.getPrivate(__instance, ScrapOoze.class, "dmg");
                dmg = dmg / 2;
                int relicObtainChance = (Integer)ReflectionHacks.getPrivate(__instance, ScrapOoze.class, "relicObtainChance");
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[1] + dmg + ScrapOoze.OPTIONS[1] + relicObtainChance + ScrapOoze.OPTIONS[2]);
            }

        }
    }

    @SpirePatch(
            clz = ScrapOoze.class,
            method = "buttonEffect"
    )
    public static class OozePostDamage {
        public OozePostDamage() {
        }

        @SpireInsertPatch(
                rloc = 7
        )
        public static void Insert(ScrapOoze __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                ReflectionHacks.setPrivate(__instance, ScrapOoze.class, "dmg", realDmg);
            }

        }
    }

    @SpirePatch(
            clz = ScrapOoze.class,
            method = "buttonEffect"
    )
    public static class OozePreDamage {
        public OozePreDamage() {
        }

        @SpireInsertPatch(
                rloc = 4
        )
        public static void Insert(ScrapOoze __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer)ReflectionHacks.getPrivate(__instance, ScrapOoze.class, "dmg");
                realDmg = dmg;
                dmg = dmg / 2;
                ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
                ReflectionHacks.setPrivate(__instance, ScrapOoze.class, "dmg", dmg);
            }

        }
    }

    @SpirePatch(
            clz = ScrapOoze.class,
            method = "<ctor>"
    )
    public static class OozeConstructior {
        public OozeConstructior() {
        }

        public static void Postfix(ScrapOoze __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer)ReflectionHacks.getPrivate(__instance, ScrapOoze.class, "dmg");
                dmg = dmg / 2;
                int relicObtainChance = (Integer)ReflectionHacks.getPrivate(__instance, ScrapOoze.class, "relicObtainChance");
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + dmg + ScrapOoze.OPTIONS[1] + relicObtainChance + ScrapOoze.OPTIONS[2]);
            }

        }
    }
}
