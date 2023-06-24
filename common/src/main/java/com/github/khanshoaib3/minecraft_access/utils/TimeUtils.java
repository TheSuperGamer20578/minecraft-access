package com.github.khanshoaib3.minecraft_access.utils;

public class TimeUtils {
    public static class Interval {
        private long lastRunTime;
        private final long delay;
        private final boolean disabled;

        private Interval(long lastRunTime, long delayInNanoTime) {
            this.lastRunTime = lastRunTime;
            this.delay = delayInNanoTime;
            this.disabled = delayInNanoTime == 0;
        }

        /**
         * Build or update instance according to delay config.
         *
         * @param delay    config value
         * @param previous the interval class variable
         */
        public static Interval inMilliseconds(long delay, Interval... previous) {
            if (previous == null || previous[0] == null) {
                // 1 milliseconds = 1*10^6 nanoseconds
                return new Interval(System.nanoTime(), delay * 1000_000);
            } else {
                Interval interval = previous[0];
                boolean configChanged = delay * 1000_000 != interval.delay;
                return configChanged ? Interval.inMilliseconds(delay) : interval;
            }
        }

        public void reset() {
            lastRunTime = System.nanoTime();
        }

        /**
         * Check if the delay has cooled down. (Will auto-reset the timer if true)
         */
        public boolean isReady() {
            if (disabled) return false;

            if (System.nanoTime() - lastRunTime > delay) {
                reset();
                return true;
            } else {
                return false;
            }
        }
    }
}
