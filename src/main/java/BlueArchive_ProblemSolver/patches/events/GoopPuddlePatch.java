package BlueArchive_ProblemSolver.patches.events;


import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.GoopPuddle;
import com.megacrit.cardcrawl.localization.EventStrings;

public class GoopPuddlePatch {
    private static final EventStrings strings;

    public GoopPuddlePatch() {
    }

    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:GoopPuddle");
    }

    @SpirePatch(
            clz = GoopPuddle.class,
            method = "buttonEffect"
    )
    public static class GoopDamage {
        public GoopDamage() {
        }

        @SpireInsertPatch(
                rloc = 6
        )
        public static void Insert(GoopPuddle __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer) ReflectionHacks.getPrivate(__instance, GoopPuddle.class, "damage");
                ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
            }

        }
    }

    @SpirePatch(
            clz = GoopPuddle.class,
            method = "<ctor>"
    )
    public static class GoopConstructior {
        public GoopConstructior() {
        }

        public static void Postfix(GoopPuddle __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer)ReflectionHacks.getPrivate(__instance, GoopPuddle.class, "damage");
                dmg = dmg / 2;
                ReflectionHacks.setPrivate(__instance, GoopPuddle.class, "damage", dmg);
                int gold = (Integer)ReflectionHacks.getPrivate(__instance, GoopPuddle.class, "gold");
                __instance.imageEventText.updateDialogOption(0, GoopPuddle.OPTIONS[0] + gold + strings.OPTIONS[0] + dmg + GoopPuddle.OPTIONS[2]);
            }

        }
    }
}
