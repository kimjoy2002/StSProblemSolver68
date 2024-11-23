package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddCharacterAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_ProblemSolver.DefaultMod.makeRelicPath;

public class RadioTransceiverRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("RadioTransceiverRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RadioTransceiverRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("RadioTransceiverRelic.png"));

    public RadioTransceiverRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
