package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Nest;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class NestPatch {
    private static final EventStrings strings;
    private static final int DMG = 3;

    public NestPatch() {
    }

    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:Nest");
    }

    @SpirePatch(
            clz = Nest.class,
            method = "buttonEffect"
    )
    public static class NestDamage {
        public NestDamage() {
        }

        @SpireInsertPatch(
                rloc = 25,
                localvars = {"c"}
        )
        public static SpireReturn Insert(Nest __instance, int buttonPressed, AbstractCard c) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                AbstractDungeon.player.damage(new DamageInfo((AbstractCreature)null, DMG));
                ((ProblemSolver68)AbstractDungeon.player).damageAll(DMG);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH * 0.3F, (float)Settings.HEIGHT / 2.0F));
                ReflectionHacks.setPrivate(__instance, Nest.class, "screenNum", 2);
                __instance.imageEventText.updateDialogOption(0, Nest.OPTIONS[4]);
                __instance.imageEventText.clearRemainingOptions();
                return SpireReturn.Return((Object)null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = Nest.class,
            method = "buttonEffect"
    )
    public static class NestOptionDamage {
        public NestOptionDamage() {
        }

        @SpireInsertPatch(
                rloc = 6
        )
        public static void Insert(Nest __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                __instance.imageEventText.updateDialogOption(1, strings.OPTIONS[0] + DMG + Nest.OPTIONS[1], new RitualDagger());
            }

        }
    }
}
