package edu.pdx.cs410J.dconradt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.Collection;

/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint {

    private TextBox txtCustomerName;

    public void onModuleLoad() {
        txtCustomerName = new TextBox();
        txtCustomerName.setMaxLength(20);
        Button btnAddCall = new Button("Submit");
        btnAddCall.addClickHandler(createNewPhoneBillOnServer());
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(btnAddCall);
        rootPanel.add(txtCustomerName);
  }

    private ClickHandler createNewPhoneBillOnServer() {
        return new ClickHandler() {
          public void onClick( ClickEvent clickEvent )
          {
              String customerName = txtCustomerName.getText();

              PingServiceAsync async = GWT.create(PingService.class);
              async.ping(customerName, new AsyncCallback<AbstractPhoneBill>() {

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
      };
    }
}
