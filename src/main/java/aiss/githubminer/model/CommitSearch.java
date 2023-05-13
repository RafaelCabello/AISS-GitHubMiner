
package aiss.githubminer.model;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sha",
    "commit",
    "html_url",
})
@Generated("jsonschema2pojo")
public class CommitSearch {
    @JsonProperty("sha")
    private String sha;
    @JsonProperty("commit")
    private CommitUnit commit;
    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("sha")
    public String getSha() {
        return sha;
    }

    @JsonProperty("sha")
    public void setSha(String sha) { this.sha = sha; }

    @JsonProperty("commit")
    public CommitUnit getCommit() {
        return commit;
    }

    @JsonProperty("commit")
    public void setCommit(CommitUnit commit) {
        this.commit = commit;
    }

    @JsonProperty("html_url")
    public String getHtmlUrl() {
        return htmlUrl;
    }

    @JsonProperty("html_url")
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CommitSearch.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sha");
        sb.append('=');
        sb.append(((this.sha == null)?"<null>":this.sha));
        sb.append(',');
        sb.append("commit");
        sb.append('=');
        sb.append(((this.commit == null)?"<null>":this.commit));
        sb.append(',');
        sb.append("htmlUrl");
        sb.append('=');
        sb.append(((this.htmlUrl == null)?"<null>":this.htmlUrl));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
