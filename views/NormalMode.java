package views;

public class NormalMode implements ColorblindModeState{
    @Override
    public void toggleColorblindMode(TouchGrassView view) {
        // Switch to colorblind mode
        view.setState(new ColorblindMode());
        view.updateToColorblindMode(); // A method in TouchGrassView to update images for colorblind mode

        // Update view to reflect colorblind mode (e.g., change colors)
    }
}
