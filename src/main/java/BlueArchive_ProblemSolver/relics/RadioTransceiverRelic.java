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

    public static final int CHANGE_LIMIT = 2;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RadioTransceiverRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("RadioTransceiverRelic.png"));

    public RadioTransceiverRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.SOLID);
    }

    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    public void atBattleStart() {
        this.counter = 0;
    }

    public void atTurnStart() {
        this.counter = 0;
    }
    public void onVictory() {
        this.counter = -1;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + CHANGE_LIMIT + this.DESCRIPTIONS[1];
    }
}
