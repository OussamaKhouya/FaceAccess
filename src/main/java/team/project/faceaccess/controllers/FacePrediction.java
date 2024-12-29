package team.project.faceaccess.controllers;

import org.bytedeco.opencv.opencv_core.Rect;

class FacePrediction {
    private final int label;
    private final double confidence;
    private final Rect face;

    public FacePrediction(int label, double confidence, Rect face) {
        this.label = label;
        this.confidence = confidence;
        this.face = face;
    }

    public int getLabel() {
        return label;
    }

    public double getConfidence() {
        return confidence;
    }

    public Rect getFace() {
        return face;
    }
}