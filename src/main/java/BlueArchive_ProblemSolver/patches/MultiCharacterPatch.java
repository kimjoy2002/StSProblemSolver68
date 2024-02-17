package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import javassist.CtBehavior;

public class MultiCharacterPatch {

    @SpirePatch(
            clz = BattleStartEffect.class,
            method = "update"
    )
    public static class GetPurgeablePatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(BattleStartEffect __instance) {
            ProblemSolver68.BattleStartEffectForProblemSolver68(AbstractDungeon.player);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "showHealthBar");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "loadPlayerSave",
            paramtypez= {
            AbstractPlayer.class
    }
    )
    public static class loadPlayerSavePatch {
        public static void Postfix(CardCrawlGame __instance, AbstractPlayer _3) {
            ProblemSolver68.afterLoad();
        }
    }
}
