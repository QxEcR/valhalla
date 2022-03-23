package com.mobileproject.valhalla.utils;

// this class is for holding the data of the leaderboard
public class Rank {
    private String rankHolder;
    private String rank;

    public Rank(String rankHolder, String rank) {
        this.rankHolder = rankHolder;
        this.rank = rank;
    }

    public void setRankHolder(String rankHolder) {
        this.rankHolder = rankHolder;
    }

    public String getRankHolder() {
        return rankHolder;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }
}
