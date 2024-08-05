package views;

public class ColorblindMode implements ColorblindModeState {
    @Override
    public void toggleColorblindMode(TouchGrassView view) {
        view.setState(new NormalMode());
        view.updateToNormalMode(); // A method in TouchGrassView to revert images to normal mode
    }
}
