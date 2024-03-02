package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.GoldenWing;
import com.megacrit.cardcrawl.events.exordium.GoopPuddle;
import com.megacrit.cardcrawl.localization.EventStrings;

public class WingStatuePatch {
    private static final EventStrings strings;

    public WingStatuePatch() {
    }

    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:WingStatue");
    }

    @SpirePatch(
            clz = GoldenWing.class,
            method = "buttonEffect"
    )
    public static class WingDamage {
        public WingDamage() {
        }

        @SpireInsertPatch(
                rloc = 6
        )
        public static void Insert(GoldenWing __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer) ReflectionHacks.getPrivate(__instance, GoldenWing.class, "damage");
                ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
            }

        }
    }

    @SpirePatch(
            clz = GoldenWing.class,
            method = "<ctor>"
    )
    public static class WingConstructior {
        public WingConstructior() {
        }

        public static void Postfix(GoldenWing __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer)ReflectionHacks.getPrivate(__instance, GoldenWing.class, "damage");
                dmg = dmg / 2;
                ReflectionHacks.setPrivate(__instance, GoldenWing.class, "damage", dmg);
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + dmg + GoldenWing.OPTIONS[1] );
            }

        }
    }
}
