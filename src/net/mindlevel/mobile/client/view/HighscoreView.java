package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.HighscoreSection;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HighscoreView extends MPage {
    protected VerticalPanel main;

    public HighscoreView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
        init();
    }

    public void init() {
        HighscoreSection highscore = new HighscoreSection(5);
        main.add(highscore);
    }

    @Override
    public Widget asWidget() {
        onLoad();
        return main;
    }
}