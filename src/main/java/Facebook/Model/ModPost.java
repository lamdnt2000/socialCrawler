package Facebook.Model;

import com.restfb.Facebook;
import com.restfb.types.Post;
import com.restfb.types.Reactions;
public class ModPost extends Post {
    @Facebook("reaction_care")
    private Reactions reactionCare;
    @Facebook("reaction_like")
    private Reactions reactionLike;
    @Facebook("reaction_love")
    private Reactions reactionLove;
    @Facebook("reaction_wow")
    private Reactions reactionWow;
    @Facebook("reaction_haha")
    private Reactions reactionHAHA;
    @Facebook("reaction_sad")
    private Reactions reactionSad;
    @Facebook("reaction_angry")
    private Reactions reactionAngry;



    public Reactions getReactionCare() {
        return reactionCare;
    }

    public void setReactionCare(Reactions reactionCare) {
        this.reactionCare = reactionCare;
    }

    public Reactions getReactionLike() {
        return reactionLike;
    }

    public void setReactionLike(Reactions reactionLike) {
        this.reactionLike = reactionLike;
    }

    public Reactions getReactionLove() {
        return reactionLove;
    }

    public void setReactionLove(Reactions reactionLove) {
        this.reactionLove = reactionLove;
    }

    public Reactions getReactionWow() {
        return reactionWow;
    }

    public void setReactionWow(Reactions reactionWow) {
        this.reactionWow = reactionWow;
    }

    public Reactions getReactionHAHA() {
        return reactionHAHA;
    }

    public void setReactionHAHA(Reactions reactionHAHA) {
        this.reactionHAHA = reactionHAHA;
    }

    public Reactions getReactionSad() {
        return reactionSad;
    }

    public void setReactionSad(Reactions reactionSad) {
        this.reactionSad = reactionSad;
    }

    public Reactions getReactionAngry() {
        return reactionAngry;
    }

    public void setReactionAngry(Reactions reactionAngry) {
        this.reactionAngry = reactionAngry;
    }
}
