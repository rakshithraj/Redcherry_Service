package com.app.redcherry.Model;

import java.io.Serializable;

/**
 * Created by rakshith raj on 09-07-2016.
 */
public class DistanceInfo  implements Serializable {
    private Distance distance;
    private Duration duration;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
