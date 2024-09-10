package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.powers.FearPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.powers.MinionPower;

import java.util.Iterator;

public class FearEscapeAction extends AbstractGameAction {
    private static final MonsterStrings monsterStrings;
    public static final String[] DIALOG;
    public FearEscapeAction(AbstractMonster source) {
        this.setValues(source, source);
        this.duration = 0.5F;
        this.actionType = ActionType.TEXT;
    }

    public void update() {
        if (this.duration == 0.5F && !source.isEscaping && source.currentHealth > 0) {
            AbstractMonster m = (AbstractMonster)this.source;
            m.escape();



            Iterator var2 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            boolean all_minion = true;
            boolean is_gremlin_leader = (source instanceof GremlinLeader);

            while(var2.hasNext()) {
                AbstractMonster m_ = (AbstractMonster)var2.next();
                if (!m_.isEscaping && !m_.isDying) {
                    if(m_.currentHealth > 0 &&
                            (m_.hasPower(FearPower.POWER_ID) ? m_.getPower(FearPower.POWER_ID).amount < m_.currentHealth: true) &&
                            (!m_.hasPower(MinionPower.POWER_ID) && !is_gremlin_leader)) {
                        all_minion = false;
                    }
                }
            }
            if(m.id == "Darkling") {
                m.halfDead = true;
                all_minion = true;
                var2 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
                while(var2.hasNext()) {
                    AbstractMonster m_ = (AbstractMonster)var2.next();
                    if (m_.id.equals("Darkling") && !m_.halfDead) {
                        all_minion = false;
                    }
                }
                if(all_minion) {
                    AbstractDungeon.getCurrRoom().cannotLose = false;

                    var2 = AbstractDungeon.getMonsters().monsters.iterator();

                    while(var2.hasNext()) {
                        AbstractMonster m_ = (AbstractMonster)var2.next();
                        if (m_.id.equals("Darkling")) {
                            m_.die();
                        }
                    }
                }
            }

            boolean first = true;
            if(all_minion) {
                var2 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
                while(var2.hasNext()) {
                    AbstractMonster m_ = (AbstractMonster) var2.next();
                    if (!m_.isEscaping && !m_.isDying) {
                        if (m_.currentHealth > 0 && (m_.hasPower(MinionPower.POWER_ID) || is_gremlin_leader)) {
                            if(is_gremlin_leader) {
                                if (first) {
                                    AbstractDungeon.actionManager.addToBottom(new ShoutAction(m_, DIALOG[3], 0.5F, 1.2F));
                                    first = false;
                                } else {
                                    AbstractDungeon.actionManager.addToBottom(new ShoutAction(m_, DIALOG[4], 0.5F, 1.2F));
                                }
                            }
                            AbstractDungeon.actionManager.addToBottom(new EscapeAction(m_));
                        }
                    }
                }
            }
        }

        this.tickDuration();
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("GremlinLeader");
        DIALOG = monsterStrings.DIALOG;
    }
}
