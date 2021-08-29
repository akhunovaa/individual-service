package com.botmasterzzz.individual.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserApplicationSecretDTO extends AbstractDto {

    private String name;
    private String value;

    @JsonProperty("banned")
    private boolean isBanned;

    @JsonProperty("created")
    private long createdTimestamp;

    public class Builder {

        private Builder() {

        }

        public UserApplicationSecretDTO.Builder setId(Long id) {
            UserApplicationSecretDTO.super.id = id;
            return this;
        }

        public UserApplicationSecretDTO.Builder setName(String name) {
            UserApplicationSecretDTO.this.name = name;
            return this;
        }

        public UserApplicationSecretDTO.Builder setValue(String value) {
            UserApplicationSecretDTO.this.value = value;
            return this;
        }

        public UserApplicationSecretDTO.Builder setIsBanned(boolean isBanned) {
            UserApplicationSecretDTO.this.isBanned = isBanned;
            return this;
        }

        public UserApplicationSecretDTO.Builder setCreatedTimeStamp(long createdTimestamp) {
            UserApplicationSecretDTO.this.createdTimestamp = createdTimestamp;
            return this;
        }

        public UserApplicationSecretDTO build() {
            return UserApplicationSecretDTO.this;
        }

    }

    public static UserApplicationSecretDTO.Builder newBuilder() {
        return new UserApplicationSecretDTO().new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public String toString() {
        return "UserApplicationSecretDTO{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", isBanned=" + isBanned +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }
}
