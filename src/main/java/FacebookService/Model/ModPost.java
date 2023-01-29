package FacebookService.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restfb.Facebook;
import com.restfb.types.Post;
import com.restfb.types.Reactions;
public class ModPost extends Post {
    @Facebook("reaction_care")
    @JsonIgnore
    private Reactions reactionCare;
    @Facebook("reaction_like")
    @JsonIgnore
    private Reactions reactionLike;
    @Facebook("reaction_love")
    @JsonIgnore
    private Reactions reactionLove;
    @Facebook("reaction_wow")
    @JsonIgnore
    private Reactions reactionWow;
    @Facebook("reaction_haha")
    @JsonIgnore
    private Reactions reactionHAHA;
    @Facebook("reaction_sad")
    @JsonIgnore
    private Reactions reactionSad;
    @Facebook("reaction_angry")
    @JsonIgnore
    private Reactions reactionAngry;
    private Long totalCare;
    private Long totalLike;
    private Long totalLove;
    private Long totalWow;
    private Long totalHAHA;
    private Long totalSad;
    private Long totalAngry;


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

    public Long getTotalCare() {
        return (reactionCare!=null)?reactionCare.getTotalCount():0L;
    }

    public Long getTotalLike() {
        return (reactionLike!=null)?reactionLike.getTotalCount():0L;

    }

    public Long getTotalLove() {
        return (reactionLove!=null)?reactionLove.getTotalCount():0L;
    }

    public Long getTotalWow() {
        return (reactionWow!=null)?reactionWow.getTotalCount():0L;
    }

    public Long getTotalHAHA() {
        return (reactionHAHA!=null)?reactionHAHA.getTotalCount():0L;
    }

    public Long getTotalSad() {
        return (reactionSad!=null)?reactionSad.getTotalCount():0L;
    }

    public Long getTotalAngry() {
        return (reactionAngry!=null)?reactionAngry.getTotalCount():0L;
    }
}
