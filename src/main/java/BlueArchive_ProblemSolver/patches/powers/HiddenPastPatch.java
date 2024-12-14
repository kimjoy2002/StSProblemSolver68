package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.effects.ShowCardAndToHandIndexEffect;
import BlueArchive_ProblemSolver.powers.HiddenPastPower;
import BlueArchive_ProblemSolver.powers.TroubleMakerPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class HiddenPastPatch {

    static public void triggerHiddenPast(AbstractCard card) {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for (AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.hasPower(HiddenPastPower.POWER_ID)) {
                    ps.getPower(HiddenPastPower.POWER_ID).onCardDraw(card);
                }
                if(ps.hasPower(TroubleMakerPower.POWER_ID)) {
                    ps.getPower(TroubleMakerPower.POWER_ID).onCardDraw(card);
                }
            }
        }
        else {
            if(AbstractDungeon.player.hasPower(HiddenPastPower.POWER_ID)) {
                AbstractDungeon.player.getPower(HiddenPastPower.POWER_ID).onCardDraw(card);
            }
            if(AbstractDungeon.player.hasPower(TroubleMakerPower.POWER_ID)) {
                AbstractDungeon.player.getPower(TroubleMakerPower.POWER_ID).onCardDraw(card);
            }
        }
    }


    @SpirePatch(
            clz = ShowCardAndAddToHandEffect.class,
            method = "<ctor>",
            paramtypez= {
                    AbstractCard.class,
                    float.class,
                    float.class
            }
    )
    public static class showCardAndAddToHandEffectPatch {

        public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card, float x, float y) {
            triggerHiddenPast(card);
        }
    }


    @SpirePatch(
            clz = ShowCardAndAddToHandEffect.class,
            method = "<ctor>",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class showCardAndAddToHandEffect2Patch {

        public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card) {
            triggerHiddenPast(card);
        }
    }




    @SpirePatch(
            clz = ShowCardAndToHandIndexEffect.class,
            method = "<ctor>",
            paramtypez= {
                    AbstractCard.class,
                    float.class,
                    float.class,
                    int.class
            }
    )
    public static class showCardAndToHandIndexEffectPatch {

        public static void Postfix(ShowCardAndToHandIndexEffect __instance, AbstractCard card, float x, float y, int index) {
            triggerHiddenPast(card);
        }
    }



    @SpirePatch(
            clz = ShowCardAndToHandIndexEffect.class,
            method = "<ctor>",
            paramtypez= {
                    AbstractCard.class,
                    int.class
            }
    )
    public static class showCardAndToHandIndexEffect2Patch {

        public static void Postfix(ShowCardAndToHandIndexEffect __instance, AbstractCard card, int index) {
            triggerHiddenPast(card);
        }
    }
}
