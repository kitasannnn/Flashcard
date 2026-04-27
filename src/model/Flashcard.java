package model;

public class Flashcard {
    private String id;
    private String word;
    private String meaning;
    private int level;
    private int failCount;

    public Flashcard() {
    }

    public Flashcard(String id, String word, String meaning) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.level = 1;
        this.failCount = 0;
    }

    public Flashcard(String id, String word, String meaning, int level, int failCount) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.level = Math.max(level, 1);
        this.failCount = Math.max(failCount, 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(level, 1);
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = Math.max(failCount, 0);
    }

    @Override
    public String toString() {
        return id + "," + word + "," + meaning + "," + level + "," + failCount;
    }
}
