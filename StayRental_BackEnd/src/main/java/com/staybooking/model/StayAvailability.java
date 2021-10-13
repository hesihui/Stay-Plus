//申明某一个field为join column，而不用产生第三张表
//因为stay_id已经被定义在StayAvailability中，所以不需要用joincolumn（name = "stay_id"）
//many to one指的是在一个table里生成指向其他table的foreign key
package com.staybooking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stay_availability")
@JsonDeserialize(builder = StayAvailability.Builder.class)
public class StayAvailability implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private com.laioffer.staybooking.model.StayAvailabilityKey id;

    @MapsId("stay_id")
    @ManyToOne
    private Stay stay;

    private com.laioffer.staybooking.model.StayAvailabilityState state;

    public StayAvailability() {}

    public StayAvailability(Builder builder) {
        this.id = builder.id;
        this.stay = builder.stay;
        this.state = builder.state;
    }

    public com.laioffer.staybooking.model.StayAvailabilityKey getId() {
        return id;
    }

    public Stay getStay() {
        return stay;
    }

    public com.laioffer.staybooking.model.StayAvailabilityState getState() {
        return state;
    }

    public static class Builder {
        @JsonProperty("id")
        private com.laioffer.staybooking.model.StayAvailabilityKey id;

        @JsonProperty("stay")
        private Stay stay;

        @JsonProperty("state")
        private com.laioffer.staybooking.model.StayAvailabilityState state;

        public Builder setId(com.laioffer.staybooking.model.StayAvailabilityKey id) {
            this.id = id;
            return this;
        }

        public Builder setStay(Stay stay) {
            this.stay = stay;
            return this;
        }

        public Builder setState(com.laioffer.staybooking.model.StayAvailabilityState state) {
            this.state = state;
            return this;
        }

        public StayAvailability build() {
            return new StayAvailability(this);
        }
    }
}