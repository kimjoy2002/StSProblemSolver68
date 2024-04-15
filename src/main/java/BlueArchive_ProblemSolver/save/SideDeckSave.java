package BlueArchive_ProblemSolver.save;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SideDeckSave implements CustomSavable<ArrayList<SaveData2>> {
    public static HashMap<Integer, ArrayList<AbstractCard>> decks = new HashMap<Integer, ArrayList<AbstractCard>>();

    @Override
    public ArrayList<SaveData2> onSave() {
        ArrayList<SaveData2> save_ = new ArrayList<SaveData2>();
        for(Map.Entry<Integer, ArrayList<AbstractCard>> deck_entry : decks.entrySet()) {
            for(AbstractCard card : deck_entry.getValue()) {
                save_.add(new SaveData2(deck_entry.getKey(), card));
            }
        }
        return save_;
    }
    @Override
    public void onLoad(ArrayList<SaveData2> datas) {
        decks.clear();
        if(datas != null) {
            for(SaveData2 data : datas) {
                AbstractCard card_base = CardLibrary.getCard(data.id);
                if(card_base != null) {
                    AbstractCard card_ = card_base.makeCopy();
                    for(int i = 0 ; i < data.upgrades; i++)
                        card_.upgrade();
                    card_.misc = data.misc;
                    addDeck(data.side_deck_misc, card_);
                }
            }
        }
    }

    public static void clear() {
        decks.clear();
    }
    public static void addDeck(int misc, AbstractCard card) {
        if(!decks.containsKey(misc)) {
            decks.put(misc, new ArrayList<>());
        }
        decks.get(misc).add(card);
    }
}