package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.characters.Aru;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractDynamicCard extends AbstractDefaultCard {

    private boolean solverType[] = new boolean[4];


    public AbstractDynamicCard(final String id,
                               final String img,
                               final int cost,
                               final CardType type,
                               final CardColor color,
                               final CardRarity rarity,
                               final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);

    }

    public void setSolverType(Aru.ProblemSolver68Type type) {
        solverType[type.ordinal()-1] = true;
    }

    public boolean isSolverType(Aru.ProblemSolver68Type type) {
        if(type.ordinal() <= 0 || type.ordinal() > 4)
            return false;
        return solverType[type.ordinal()-1] == true;
    }

    public boolean isGenericType() {
        return !isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU) &&
                !isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI) &&
                !isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO) &&
                !isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);
    }

    public boolean ableSolverType(boolean[] testTypes) {
        for(int i =0; i < 4; i++) {
            if(testTypes[i] == false && solverType[i] == true) {
                return false;
            }
        }
        return true;
    }
    public boolean isSpecificCard() {
        for(int i =0; i < 4; i++) {
            if(solverType[i] == true) {
                return true;
            }
        }
        return false;
    }

    public void atStartOfCombat() {

    }
}