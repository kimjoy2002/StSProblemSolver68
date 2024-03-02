package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import com.megacrit.cardcrawl.localization.EventStrings;

public class SensoryStonePatch {
    private static final EventStrings strings;
    private static final int DMG_A = 3;
    private static final int DMG_B = 5;

    public SensoryStonePatch() {
    }

    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:SensoryStone");
    }

    @SpirePatch(
            clz = SensoryStone.class,
            method = "buttonEffect"
    )
    public static class SensoryStoneDamageB {
        public SensoryStoneDamageB() {
        }

        @SpireInsertPatch(
                rloc = 31
        )
        public static SpireReturn Insert(SensoryStone __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                AbstractDungeon.player.damage(new DamageInfo((AbstractCreature)null, DMG_B));
                ((ProblemSolver68)AbstractDungeon.player).damageAll(DMG_B);
                __instance.imageEventText.updateDialogOption(0, SensoryStone.OPTIONS[4]);
                __instance.imageEventText.clearRemainingOptions();
                return SpireReturn.Return((Object)null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = SensoryStone.class,
            method = "buttonEffect"
    )
    public static class SensoryStoneDamageA {
        public SensoryStoneDamageA() {
        }

        @SpireInsertPatch(
                rloc = 23
        )
        public static SpireReturn Insert(SensoryStone __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                AbstractDungeon.player.damage(new DamageInfo((AbstractCreature)null, DMG_A));
                ((ProblemSolver68)AbstractDungeon.player).damageAll(DMG_A);
                __instance.imageEventText.updateDialogOption(0, SensoryStone.OPTIONS[4]);
                __instance.imageEventText.clearRemainingOptions();
                return SpireReturn.Return((Object)null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = SensoryStone.class,
            method = "buttonEffect"
    )
    public static class SensoryStoneOptionDamage {
        public SensoryStoneOptionDamage() {
        }

        @SpireInsertPatch(
                rloc = 6
        )
        public static void Insert(SensoryStone __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                __instance.imageEventText.updateDialogOption(1, strings.OPTIONS[0] + DMG_A + SensoryStone.OPTIONS[3]);
                __instance.imageEventText.updateDialogOption(2, strings.OPTIONS[1] + DMG_B + SensoryStone.OPTIONS[3]);
            }

        }
    }
}
