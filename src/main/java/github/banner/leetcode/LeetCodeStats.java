package github.banner.leetcode;

public class LeetCodeStats {
    private String streak;
    private String easySolved;
    private String mediumSolved;
    private String hardSolved;
    private String totalSolved;

    public LeetCodeStats(String streak, String easySolved, String mediumSolved, String hardSolved, String totalSolved) {
        this.streak = streak;
        this.easySolved = easySolved;
        this.mediumSolved = mediumSolved;
        this.hardSolved = hardSolved;
        this.totalSolved = totalSolved;
    }

    public String getStreak() {
        return streak;
    }

    public String getEasySolved() {
        return easySolved;
    }

    public String getMediumSolved() {
        return mediumSolved;
    }

    public String getHardSolved() {
        return hardSolved;
    }

    public String getTotalSolved() {
        return totalSolved;
    }
}
