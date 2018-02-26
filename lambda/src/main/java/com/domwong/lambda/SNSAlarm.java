package com.domwong.lambda;

import com.google.gson.annotations.SerializedName;

public class SNSAlarm {
    @SerializedName("AlarmName")
    private String alarmName;
    @SerializedName("NewStateValue")
    private String newStateValue;

    @SerializedName("Trigger")
    private Trigger trigger;

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getNewStateValue() {
        return newStateValue;
    }

    public void setNewStateValue(String newStateValue) {
        this.newStateValue = newStateValue;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    class Trigger {
        @SerializedName("Dimensions")
        private Dimension[] dimensions;

        public Dimension[] getDimensions() {
            return dimensions;
        }

        public void setDimensions(Dimension[] dimensions) {
            this.dimensions = dimensions;
        }
    }

    class Dimension {
        private String name;

        private String value;

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
    }
}
