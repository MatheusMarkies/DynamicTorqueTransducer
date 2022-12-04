package com.matheusmarkies.objects;

public class ColorStats {
        private double Red;
        private double Green;
        private double Blue;

    private double position;

    public ColorStats(double red, double green, double blue) {
        Red = red;
        Green = green;
        Blue = blue;
    }

    public ColorStats(){}

    public double getRed() {
            return Red;
        }

        public void setRed(double red) {
            Red = red;
        }

        public double getGreen() {
            return Green;
        }

        public void setGreen(double green) {
            Green = green;
        }

        public double getBlue() {
            return Blue;
        }

        public void setBlue(double blue) {
            Blue = blue;
        }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    @Override
        public String toString() {
            return "ColorStats{" +
                    "Red=" + Red +
                    ", Green=" + Green +
                    ", Blue=" + Blue +
                    '}';
        }
}
