package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddTemporaryHPToAllAllyAction;
import BlueArchive_ProblemSolver.cards.ChooseHaruka;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.effects.LoseRelicEffect;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static BlueArchive_ProblemSolver.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_ProblemSolver.DefaultMod.makeRelicPath;

public class MoreProblemSolverRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("MoreProblemSolverRelic");

    private static final Texture IMG_ARU = TextureLoader.getTexture(makeRelicPath("ProblemSolverAru.png"));
    private static final Texture IMG_MUTSUKI = TextureLoader.getTexture(makeRelicPath("ProblemSolverMutsuki.png"));
    private static final Texture IMG_KAYOKO = TextureLoader.getTexture(makeRelicPath("ProblemSolverKayoko.png"));
    private static final Texture IMG_HARUKA = TextureLoader.getTexture(makeRelicPath("ProblemSolverHaruka.png"));
    private static final Texture IMG_UNKNOWN = TextureLoader.getTexture(makeRelicPath("ProblemSolverUnknown.png"));

    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ProblemSolverBaseRelic.png"));

    Aru.ProblemSolver68Type type;

    static Texture getTex(Aru.ProblemSolver68Type type) {
        switch (type) {
            case PROBLEM_SOLVER_68_ARU:
                return IMG_ARU;
            case PROBLEM_SOLVER_68_MUTSUKI:
                return IMG_MUTSUKI;
            case PROBLEM_SOLVER_68_KAYOKO:
                return IMG_KAYOKO;
            case PROBLEM_SOLVER_68_HARUKA:
                return IMG_HARUKA;
            default:
                return IMG_UNKNOWN;
        }
    }

    public MoreProblemSolverRelic(Aru.ProblemSolver68Type type) {
        super(ID, getTex(type), OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        this.type = type;
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public void onEquip() {
        ProblemSolver68.addCharacter(type);
        AbstractDungeon.effectList.add(new LoseRelicEffect(ID));
    }

    public String getUpdatedDescription() {
        if(type != null) {
            int i = type.ordinal()-1;
            if(i >= 0 && i < 4) {
                return this.DESCRIPTIONS[i];
            } else {
                return this.DESCRIPTIONS[4];
            }
        }
        return "";
    }
}
