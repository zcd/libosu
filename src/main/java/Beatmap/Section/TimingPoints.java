package Beatmap.Section;

import Beatmap.DataTypes.TimingPoint;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class TimingPoints {
    public abstract List<TimingPoint> timingPoints();

    public static TimingPoints create(List<TimingPoint> timingPoints) {
        return new AutoValue_TimingPoints(timingPoints);
    }
}
