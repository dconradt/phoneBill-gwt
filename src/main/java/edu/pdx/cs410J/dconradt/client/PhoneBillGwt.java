package edu.pdx.cs410J.dconradt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
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
    private TextBox txtCallerNumber;
    private TextBox txtCalleeNumber;
    private TextBox txtStartTime;
    private TextBox txtEndTime;
    private Label lblCustomerName;
    private Label lblCallerNumber;
    private Label lblCalleeNumber;
    private Label lblStartTime;
    private Label lblEndTime;

    public void onModuleLoad() {

        int txtFromLeft = 125;
        int txtFromTop = 100;
        int lblFromLeft = 20;
        int lblFromTop = 100;
        txtCustomerName = new TextBox();
        txtCustomerName.setMaxLength(20);
        txtCallerNumber = new TextBox();
        txtCalleeNumber = new TextBox();
        txtStartTime = new TextBox();
        txtEndTime = new TextBox();
        lblCustomerName = new Label("Customer Name");
        lblCallerNumber = new Label("Caller Number");
        lblCalleeNumber = new Label("Callee Number");
        lblStartTime = new Label("Start Time");
        lblEndTime = new Label("End Time");
        Button btnAddCall = new Button("Submit");
        btnAddCall.addClickHandler(createNewPhoneBillOnServer());

        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(txtCustomerName);
        rootPanel.add(txtCallerNumber);
        rootPanel.add(txtCalleeNumber);
        rootPanel.add(txtStartTime);
        rootPanel.add(txtEndTime);
        rootPanel.add(lblCustomerName, lblFromLeft, lblFromTop);
        rootPanel.add(txtCustomerName, txtFromLeft, txtFromTop);
        rootPanel.add(lblCallerNumber, lblFromLeft, lblFromTop + 40);
        rootPanel.add(txtCallerNumber, txtFromLeft, txtFromTop + 40);
        rootPanel.add(lblCalleeNumber, lblFromLeft, lblFromTop + 60);
        rootPanel.add(txtCalleeNumber, txtFromLeft, txtFromTop + 60);
        rootPanel.add(lblStartTime, lblFromLeft, lblFromTop + 80);
        rootPanel.add(txtStartTime, txtFromLeft, txtFromTop + 80);
        rootPanel.add(lblEndTime, lblFromLeft, lblFromTop + 100);
        rootPanel.add(txtEndTime, txtFromLeft, txtFromTop + 100);
        rootPanel.add(btnAddCall, lblFromLeft, lblFromTop + 120);
  }

    private ClickHandler createNewPhoneBillOnServer() {
        return new ClickHandler() {
          public void onClick( ClickEvent clickEvent )
          {

              String customerName = txtCustomerName.getText();
              String callerNumber = txtCallerNumber.getText();
              String calleeNumber = txtCalleeNumber.getText();
              String startTime = txtStartTime.getText();
              String endTime = txtEndTime.getText();


              PingServiceAsync async = GWT.create(PingService.class);
              async.ping(customerName, callerNumber, calleeNumber, startTime, endTime, new AsyncCallback<AbstractPhoneBill>() {
                  public void onFailure( Throwable ex )
                  {
                      Window.alert(ex.toString());
                  }

                  public void onSuccess( AbstractPhoneBill phonebill )
                  {


                      //StringBuilder sb = new StringBuilder( phonebill.toString() );
                      Collection<AbstractPhoneCall> calls = phonebill.getPhoneCalls();
                      for ( AbstractPhoneCall call : calls ) {
                          Window.alert(phonebill.getCustomer() + "'s phone call from " +
                                  call.getCaller() + " to " + call.getCallee() + " between " +
                                  call.getStartTimeString() + " and " + call.getEndTimeString() + " was added" +
                                  " to the phone bill.");
                      }
                     // Window.alert( sb.toString() );*/
                  }
              });
          }
      };
    }
}
