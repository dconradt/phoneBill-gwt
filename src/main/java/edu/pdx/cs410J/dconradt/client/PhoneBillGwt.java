package edu.pdx.cs410J.dconradt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.Collection;

/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint {
  public void onModuleLoad() {
      TextBox customerNameField = new TextBox();
      TextBox callerNumber = new TextBox();
      TextBox calleeNumber = new TextBox();
      TextBox startTime = new TextBox();
      TextBox endTime = new TextBox();
      Label customerNameLbl = new Label();
      Label callerNumberLbl = new Label("Caller Number");
      Label calleeNumberLbl = new Label("Callee Number");
      Label startTimeLbl = new Label("Start Time");
      Label endTimeLbl = new Label("End Time");

      LayoutPanel newLayout = new LayoutPanel();

      customerNameLbl.setText("Name");
      customerNameField.setMaxLength(10);
    Button submit = new Button("Submit");
    submit.addClickHandler(new ClickHandler() {
        public void onClick( ClickEvent clickEvent )
        {
            PingServiceAsync async = GWT.create( PingService.class );
            async.ping( new AsyncCallback<AbstractPhoneBill>() {

                public void onFailure( Throwable ex )
                {
                    Window.alert(ex.toString());
                }

                public void onSuccess( AbstractPhoneBill phonebill )
                {
                    StringBuilder sb = new StringBuilder( phonebill.toString() );
                    Collection<AbstractPhoneCall> calls = phonebill.getPhoneCalls();
                    for ( AbstractPhoneCall call : calls ) {
                        sb.append(call);
                        sb.append("\n");
                    }
                    Window.alert( sb.toString() );
                }
            });
        }
    });

      RootPanel rootPanel = RootPanel.get();
      rootPanel.add(submit,200,50);
      rootPanel.add(customerNameField);
      rootPanel.add(customerNameLbl);
      rootPanel.add(callerNumber);
      rootPanel.add(calleeNumber);
      rootPanel.add(startTime);
      rootPanel.add(endTime);
      rootPanel.add(callerNumberLbl);
      rootPanel.add(calleeNumberLbl);
      rootPanel.add(startTimeLbl);
      rootPanel.add(endTimeLbl);
  }
}
