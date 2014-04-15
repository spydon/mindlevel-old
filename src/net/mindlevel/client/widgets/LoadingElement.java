package net.mindlevel.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoadingElement extends Composite {

    private final VerticalPanel loadingPanel;
    public static String loadingPath = "../images/loading.gif";;

    /**
     * Constructs an CommentBox with the given caption on the check.
     *
     * @param caption the caption to be displayed with the check box
     */
    public LoadingElement() {
        loadingPanel = new VerticalPanel();
        loadingPanel.add(new HTML("LOAAAAADING!"));
        loadingPanel.add(new Image(loadingPath));

        // All composites must call initWidget() in their constructors.
        initWidget(loadingPanel);
    }
}