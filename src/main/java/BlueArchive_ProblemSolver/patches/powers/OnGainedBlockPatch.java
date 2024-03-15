package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.powers.OnGainedBlockModifierPower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "addBlock"
)
public class OnGainedBlockPatch {
    @SpireInsertPatch(
            rloc = 1,
            localvars = {"tmp"}
    )
    public static void Insert(AbstractCreature __instance, int blockAmount, @ByRef float[] tmp) {
        Iterator var3 = __instance.powers.iterator();

        while(var3.hasNext()) {
            AbstractPower p = (AbstractPower)var3.next();
            if (p instanceof OnGainedBlockModifierPower) {
                tmp[0] = (float)((OnGainedBlockModifierPower)p).onGainedBlockModifier(tmp[0]);
            }
        }

    }
}
