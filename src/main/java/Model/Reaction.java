package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reaction {


    @JsonProperty("reactionTypeId")
    public int getReactionTypeId() {
        return this.reactionTypeId;
    }

    public void setReactionTypeId(int reactionTypeId) {
        this.reactionTypeId = reactionTypeId;
    }

    int reactionTypeId;




    String postId;

    @JsonProperty("count")
    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    long count;

    @JsonProperty("status")
    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    boolean status;

    public Reaction(int reactionTypeId, String postId, long count) {
        this.reactionTypeId = reactionTypeId;
        this.postId = postId;
        this.count = count;
        this.status = true;
    }

    @JsonProperty("postId")
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
