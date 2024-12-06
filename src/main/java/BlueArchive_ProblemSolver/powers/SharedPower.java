package BlueArchive_ProblemSolver.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PenNibPower;

import java.util.ArrayList;

public interface SharedPower {

    static boolean moreShared(AbstractPower power, AbstractPlayer newPlayer, ArrayList<AbstractPower> powerlist) {
        if( power instanceof PenNibPower) {
            powerlist.add(new PenNibPower(newPlayer, power.amount));
            return true;
        }
        return false;
    }
}
