package main.java.main.java.guiUtil;

import org.controlsfx.control.Notifications;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.util.Duration;

public class AlertNotification {

	public void showSuccessMessage(String msg) 
	{
		
		try {

		Notifications message = Notifications.create()
				.title("Success")
				.text(msg)
				.hideAfter(Duration.seconds(5))
				.position(Pos.TOP_LEFT)
				.onAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
					}
				});
		//message.darkStyle();

		message.showInformation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	public void showErrorMessage(String msg)
	{
		Notifications message = Notifications.create()
				.title("Error")
				.text(msg)
				.hideAfter(Duration.seconds(5))
				.position(Pos.TOP_LEFT)
				.onAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
					}
				});
		message.darkStyle();
		message.showError();
	}
	public void showConfirmMessage(String msg)
	{
		Notifications message = Notifications.create()
				.title("Error")
				.text(msg)
				.hideAfter(Duration.seconds(5))
				.position(Pos.TOP_LEFT)
				.onAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
					}
				});
		message.darkStyle();
		message.showError();
	}
}
