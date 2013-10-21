package net.mindlevel.client.pages;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class Admin {
	private RootPanel appArea;
	
	public Admin(RootPanel appArea) {
		this.appArea = appArea;
		init();
	}
	
	private void init() {
		MenuBar validationMenu = new MenuBar(true);
		validationMenu.setAutoOpen(true);
		validationMenu.setAnimationEnabled(true);
		validationMenu.addItem("Missions", new Command() {
			public void execute() {
				clearScreen();
				new Missions(appArea, false);
			}
		});
		validationMenu.addItem("Pictures", new Command() {
			public void execute() {
				clearScreen();
				new Picture(appArea, 0, false);
			}
		});

		// Make a menu bar, adding a few cascading menus to it.
		MenuBar menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setAnimationEnabled(true);
		menu.addItem("Dashboard", new Command() {
			public void execute() {
				clearScreen();

			}
		});
		menu.addItem("Ban", new Command() {
			public void execute() {
				clearScreen();

			}
		});
		menu.addItem("Validation", validationMenu);

		RootPanel.get("adminmenu").add(menu);
	}
	
	private void clearScreen() {
		appArea.clear();
	}
}