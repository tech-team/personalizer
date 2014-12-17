package server;

import content.PersonCard;
import content.PersonList;

import java.util.ArrayList;
import java.util.List;

public class BufferedContentReceiver implements ContentReceiver {
    private List<PersonCard> postedPersonCards = new ArrayList<>();
    private int postedPersonCardsReadPos = 0;

    private List<PersonList> postedPersonLists = new ArrayList<>();
    private int postedPersonListsReadPos = 0;

    private PersonList postedResults = null;

    private boolean finishedListsRetrieval;
    private boolean finishedAutomaticMerge;
    private boolean finishedRemoval;
    private boolean finishedMerge;

    @Override
    public void postPersonCard(PersonCard card) {
        postedPersonCards.add(card);
    }

    @Override
    public void postPersonList(PersonList list) {
        postedPersonLists.add(list);
    }

    @Override
    public void postResults(PersonList list) {
        postedResults = list;
    }

    @Override
    public void onFinishedListsRetrieval() {
        finishedListsRetrieval = true;
    }

    @Override
    public void onFinishedAutomaticMerge() {
        finishedAutomaticMerge = true;
    }

    @Override
    public void onFinishedRemoval() {
        finishedRemoval = true;
    }

    @Override
    public void onFinishedMerge() {
        finishedMerge = true;
    }

    public List<PersonCard> getPostedPersonCards(boolean fromBegin) {
        if (fromBegin)
            postedPersonCardsReadPos = 0;

        return getPostedPersonCards();
    }

    public List<PersonCard> getPostedPersonCards() {
        if (postedPersonCardsReadPos == 0 || postedPersonCards.size() == 0)
            return postedPersonCards;
        else
            return postedPersonCards.subList(postedPersonCardsReadPos, postedPersonCards.size());
    }

    public List<PersonList> getPostedPersonLists(boolean fromBegin) {
        if (fromBegin)
            postedPersonListsReadPos = 0;

        return getPostedPersonLists();
    }

    public List<PersonList> getPostedPersonLists() {
        if (postedPersonListsReadPos == 0 || postedPersonLists.size() == 0)
            return postedPersonLists;
        else
            return postedPersonLists.subList(postedPersonListsReadPos, postedPersonLists.size());
    }

    public PersonList getPostedResults() {
        return postedResults;
    }

    public void setPostedResults(PersonList postedResults) {
        this.postedResults = postedResults;
    }

    public boolean isFinishedListsRetrieval() {
        return finishedListsRetrieval;
    }

    public boolean isFinishedAutomaticMerge() {
        return finishedAutomaticMerge;
    }

    public boolean isFinishedRemoval() {
        return finishedRemoval;
    }

    public boolean isFinishedMerge() {
        return finishedMerge;
    }
}
